package cuet.shcms.app.service;

import cuet.shcms.app.controller.ChatbotController.ChatMessage;
import cuet.shcms.app.controller.ChatbotController.ChatResponse;
import cuet.shcms.app.controller.ChatbotController.ComplaintSuggestion;
import cuet.shcms.app.entity.Complaint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class GeminiChatService {

    private static final String SYSTEM_INSTRUCTION = """
        You are the friendly assistant for a university residence complaint management system.
        Answer ordinary questions naturally and helpfully. When a user describes a residence issue,
        ask a short follow-up if important details are missing. Once there is enough information to
        draft a complaint, provide a concise reply asking whether to fill the form and include a
        complaint suggestion. Do not claim that a complaint was submitted; the user must review and
        submit it.
        Return JSON with exactly these fields: "reply" (string) and "complaint" (object or null).
        A complaint object must have "title", "description", "category", and "urgency".
        Preserve the user's facts in the description. category must be one of ELECTRICAL, PLUMBING,
        FURNITURE, CLEANLINESS, SECURITY, INTERNET, OTHERS. urgency must be one of LOW, MEDIUM,
        HIGH, CRITICAL. For ordinary chat or when details are not enough, set complaint to null.
        """;

    private static final Map<String, Object> RESPONSE_SCHEMA = Map.of(
        "type", "OBJECT",
        "properties", Map.of(
            "reply", Map.of("type", "STRING"),
            "complaint", Map.of(
                "type", "OBJECT",
                "nullable", true,
                "properties", Map.of(
                    "title", Map.of("type", "STRING"),
                    "description", Map.of("type", "STRING"),
                    "category", Map.of(
                        "type", "STRING",
                        "enum", List.of("ELECTRICAL", "PLUMBING", "FURNITURE", "CLEANLINESS",
                            "SECURITY", "INTERNET", "OTHERS")
                    ),
                    "urgency", Map.of(
                        "type", "STRING",
                        "enum", List.of("LOW", "MEDIUM", "HIGH", "CRITICAL")
                    )
                ),
                "required", List.of("title", "description", "category", "urgency")
            )
        ),
        "required", List.of("reply", "complaint")
    );

    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;

    public GeminiChatService(
        ObjectMapper objectMapper,
        @Value("${gemini.api-key:}") String apiKey,
        @Value("${gemini.model:gemini-2.5-flash}") String model
    ) {
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.model = model;
    }

    public ChatResponse chat(List<ChatMessage> messages) {
        if (apiKey.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Gemini is not configured. Set the GEMINI_API_KEY environment variable."
            );
        }

        List<Map<String, Object>> contents = messages.stream()
            .map(message -> Map.<String, Object>of(
                "role", message.role(),
                "parts", List.of(Map.of("text", message.text()))
            ))
            .toList();

        Map<String, Object> request = Map.of(
            "systemInstruction", Map.of("parts", List.of(Map.of("text", SYSTEM_INSTRUCTION))),
            "contents", contents,
            "generationConfig", Map.of(
                "responseMimeType", "application/json",
                "responseSchema", RESPONSE_SCHEMA
            )
        );

        try {
            JsonNode response = restClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent", model)
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(JsonNode.class);

            String text = response == null
                ? null
                : response.path("candidates").path(0).path("content").path("parts").path(0).path("text")
                    .textValue();
            if (text == null || text.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Gemini returned an empty response.");
            }

            GeminiReply reply = objectMapper.readValue(text, GeminiReply.class);
            if (reply.reply() == null || reply.reply().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Gemini returned an invalid response.");
            }
            return new ChatResponse(reply.reply(), normalizeComplaint(reply.complaint()));
        } catch (RestClientException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Gemini request failed.", exception);
        } catch (JacksonException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Gemini returned an invalid response.", exception);
        }
    }

    private ComplaintSuggestion normalizeComplaint(GeminiComplaint complaint) {
        if (complaint == null) {
            return null;
        }
        try {
            String title = requireText(complaint.title());
            String description = requireText(complaint.description());
            Complaint.ComplaintCategory category = Complaint.ComplaintCategory.valueOf(
                requireText(complaint.category()).toUpperCase(Locale.ROOT)
            );
            Complaint.UrgencyLevel urgency = Complaint.UrgencyLevel.valueOf(
                requireText(complaint.urgency()).toUpperCase(Locale.ROOT)
            );
            return new ComplaintSuggestion(
                title,
                description,
                category.name().toLowerCase(Locale.ROOT),
                urgency.name().toLowerCase(Locale.ROOT)
            );
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Gemini returned an invalid complaint suggestion.", exception);
        }
    }

    private String requireText(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Required complaint field is empty.");
        }
        return value;
    }

    private record GeminiReply(String reply, GeminiComplaint complaint) {}

    private record GeminiComplaint(String title, String description, String category, String urgency) {}
}

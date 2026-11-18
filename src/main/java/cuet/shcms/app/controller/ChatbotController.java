package cuet.shcms.app.controller;

import cuet.shcms.app.service.GeminiChatService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chatbot")
@Validated
public class ChatbotController {

    private final GeminiChatService geminiChatService;

    public ChatbotController(GeminiChatService geminiChatService) {
        this.geminiChatService = geminiChatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(geminiChatService.chat(request.messages()));
    }

    public record ChatRequest(
        @NotEmpty @Size(max = 20) List<@Valid ChatMessage> messages
    ) {}

    public record ChatMessage(
        @NotBlank @Pattern(regexp = "user|model") String role,
        @NotBlank @Size(max = 2000) String text
    ) {}

    public record ChatResponse(String reply, ComplaintSuggestion complaint) {}

    public record ComplaintSuggestion(
        String title,
        String description,
        String category,
        String urgency
    ) {}
}

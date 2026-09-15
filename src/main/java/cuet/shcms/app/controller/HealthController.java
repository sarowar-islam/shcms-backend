package cuet.shcms.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    private final DataSource dataSource;
    private Instant startTime = Instant.now();

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        boolean dbConnected = false;

        try (Connection conn = dataSource.getConnection()) {
            conn.isValid(5);
            health.put("database", "Connected");
            health.put("databaseType", conn.getMetaData().getDatabaseProductName());
            dbConnected = true;
        } catch (Exception e) {
            health.put("database", "Disconnected");
            health.put("error", e.getMessage());
        }

        // Always return 200 for uptime robot ping checks
        health.put("status", dbConnected ? "UP" : "DEGRADED");
        health.put("timestamp", Instant.now().toEpochMilli());
        health.put("uptimeSeconds", Instant.now().getEpochSecond() - startTime.getEpochSecond());
        health.put("service", "CUET-SHCMS Backend");
        health.put("version", "1.0.0");

        return ResponseEntity.ok(health);
    }

    @GetMapping("/health/ping")
    public ResponseEntity<Map<String, Object>> ping(@RequestParam(required = false, defaultValue = "pong") String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", message);
        response.put("timestamp", Instant.now().toEpochMilli());
        return ResponseEntity.ok(response);
    }
}
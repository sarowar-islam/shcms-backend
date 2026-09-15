package cuet.shcms.app.controller;

import cuet.shcms.app.entity.SystemConfig;
import cuet.shcms.app.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
@CrossOrigin(origins = "*")
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @Autowired
    public SystemConfigController(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }

    @GetMapping
    public ResponseEntity<SystemConfig> getConfig() {
        SystemConfig config = systemConfigService.getConfig();
        return ResponseEntity.ok(config);
    }

    @PutMapping
    public ResponseEntity<SystemConfig> updateConfig(@RequestBody SystemConfig config) {
        SystemConfig updated = systemConfigService.updateConfig(config);
        return ResponseEntity.ok(updated);
    }
}
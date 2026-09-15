package cuet.shcms.app.service;

import cuet.shcms.app.entity.SystemConfig;
import cuet.shcms.app.repository.SystemConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    @Autowired
    public SystemConfigService(SystemConfigRepository systemConfigRepository) {
        this.systemConfigRepository = systemConfigRepository;
    }

    public SystemConfig getConfig() {
        Optional<SystemConfig> configOpt = systemConfigRepository.findById(1L);
        if (configOpt.isPresent()) {
            return configOpt.get();
        }

        // Create default config if not exists
        SystemConfig defaultConfig = new SystemConfig();
        defaultConfig.setId(1L);
        defaultConfig.setHallName("Kabi Kazi Nazrul Islam Hall of Residence");
        defaultConfig.setTotalRooms(240);
        defaultConfig.setWardenName("Dr. Anita Krishnan");
        defaultConfig.setContactEmail("kknih@cuet.ac.bd");
        defaultConfig.setNotificationsEnabled(true);
        defaultConfig.setAutoAssign(false);
        defaultConfig.setMaxComplaintsPerDay(5);

        return systemConfigRepository.save(defaultConfig);
    }

    public SystemConfig updateConfig(SystemConfig config) {
        config.setId(1L);
        return systemConfigRepository.save(config);
    }
}
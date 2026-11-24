package org.ftm.api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ftm.api.role.AdminPermissionInitializer;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminPermissionRunner implements CommandLineRunner {
    private final AdminPermissionInitializer initializer;

    @Override
    public void run(String @NonNull ... args) {
        log.info("Initializing ADMIN permissions...");

        initializer.assignAllPermissions();

        log.info("ADMIN permissions initialization completed.");
    }
}

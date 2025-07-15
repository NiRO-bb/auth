package com.example.AuthService;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseSetup implements BeforeAllCallback {

    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testDatabase")
            .withUsername("testUser")
            .withPassword("testPass");

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        container.start();
        updateProperties(container);
    }

    private void updateProperties(PostgreSQLContainer<?> container) {
        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
        System.setProperty("token.secret.key", "1af312f5365fdb661334102f81c41582c04cb64048a9bf2fe802b1a04ea4bbc7");
        System.setProperty("token.expirations", "600000");
    }

}

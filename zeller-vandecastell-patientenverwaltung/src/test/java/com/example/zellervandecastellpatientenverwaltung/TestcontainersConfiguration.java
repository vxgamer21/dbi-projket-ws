package com.example.zellervandecastellpatientenverwaltung;

import org.springframework.boot.test.context.TestConfiguration;

/**
 * Test configuration - uses Docker Compose MongoDB instance
 * Start MongoDB with: docker compose up mongo -d
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {
    // MongoDB is started via Docker Compose
    // Connection configured in application.properties
}

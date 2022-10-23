package com.jetbrains.productregistry.common

import org.junit.jupiter.api.AfterAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest
abstract class AbstractIntegrationTest(

) {
    companion object {
        val postgresContainer = PostgreSQLContainer("postgres:14.1")
            .withDatabaseName(DB_NAME)
            .withUsername(DB_USER_NAME)
            .withPassword(DB_PASS)

        init {
            postgresContainer.start()
        }

        fun shutDownPostgres() {
            postgresContainer.stop()
        }

        @DynamicPropertySource
        @JvmStatic
        fun dynamicProperties(
            registry: DynamicPropertyRegistry
        ) {
            registry.add("spring.datasource.url") { postgresContainer.jdbcUrl }
        }

        @AfterAll
        fun shutdown() {
            shutDownPostgres()
        }
    }
}
private const val DB_NAME = "product_registry"
private const val DB_USER_NAME = "postgres"
private const val DB_PASS = "postgres"
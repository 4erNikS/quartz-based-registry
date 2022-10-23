package com.jetbrains.productregistry.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration

@Configuration
@ConstructorBinding
@ConfigurationProperties(prefix = "schedule")
data class ScheduleProperties(
    val productLoadSecInterval: Int = 3600,
    val versionLoadSecInterval: Int = 1800,
    val distrInfoSecInterval: Int = 600
)
package com.jetbrains.productregistry.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "client")
class ClientProperties{
    lateinit var productsInfoUrl: String
    lateinit var productReleaseInfoUrl: String
}
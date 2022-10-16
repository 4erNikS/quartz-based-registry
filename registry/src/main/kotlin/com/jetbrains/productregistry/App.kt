package com.jetbrains.productregistry

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
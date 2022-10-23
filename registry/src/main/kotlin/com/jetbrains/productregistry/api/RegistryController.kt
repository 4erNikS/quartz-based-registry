package com.jetbrains.productregistry.api

import com.jetbrains.productregistry.service.processing.ProductProcessingService
import org.springframework.web.bind.annotation.RestController

//TODO move to swagger based controller
@RestController
class RegistryController(
    private val productProcessingService: ProductProcessingService
) {

}
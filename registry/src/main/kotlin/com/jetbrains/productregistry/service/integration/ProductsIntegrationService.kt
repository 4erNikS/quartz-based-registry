package com.jetbrains.productregistry.service.integration

import com.jetbrains.productregistry.model.logic.ProductInfoDto
import com.jetbrains.productregistry.model.logic.ProductVersionDto

/**
 * Service that gets information
 * about all possible products and their builds
 */
interface ProductsIntegrationService {
    fun getProductList(): List<ProductInfoDto>
    fun getProductVersions(productCode: String): List<ProductVersionDto>
}
package com.jetbrains.productregistry.service.processing.impl

import com.jetbrains.productregistry.model.persitence.Product
import com.jetbrains.productregistry.service.data.ProductDataService
import com.jetbrains.productregistry.service.integration.ProductsIntegrationService
import com.jetbrains.productregistry.service.processing.ProductProcessingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProductProcessingServiceImpl(
    private val productDataService: ProductDataService,
    private val productsIntegrationService: ProductsIntegrationService
): ProductProcessingService {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun registerNewProducts(): Set<Product> {
        log.debug("Product synchronisation method called")
        val availableProducts = productsIntegrationService.getProductList()
        val addedProducts = productDataService.createNewProducts(availableProducts)
        log.info("Added ${addedProducts.size} new products to the registry")
        return addedProducts
    }

    override fun getProductForVersioningScan(): Product? {
        return productDataService.getProductForVersioningScan()
    }

    override fun freeProductForVersioningScan(productId: Long) {
        productDataService.freeProductForVersioningScan(productId)
    }

}
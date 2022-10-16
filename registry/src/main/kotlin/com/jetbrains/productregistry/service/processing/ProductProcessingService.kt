package com.jetbrains.productregistry.service.processing

import com.jetbrains.productregistry.model.persitence.Product

/**
 * Service that responsible for logic related with updates
 * of product info
 */
interface ProductProcessingService {

    fun registerNewProducts(): Set<Product>

    fun getProductForVersioningScan(): Product?

    fun freeProductForVersioningScan(productId: Long)

}
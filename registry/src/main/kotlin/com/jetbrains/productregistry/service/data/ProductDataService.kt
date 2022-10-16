package com.jetbrains.productregistry.service.data

import com.jetbrains.productregistry.model.logic.ProductInfoDto
import com.jetbrains.productregistry.model.persitence.Product

interface ProductDataService {
    fun getProductForVersioningScan(): Product?

    fun freeProductForVersioningScan(productId: Long)

    /**
     * Checks if products exists
     * if not creates new product and returns
     */
    fun createNewProducts(availableProducts: List<ProductInfoDto>): Set<Product>
}
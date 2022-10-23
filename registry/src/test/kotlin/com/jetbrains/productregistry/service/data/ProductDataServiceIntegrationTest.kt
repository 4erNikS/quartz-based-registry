package com.jetbrains.productregistry.service.data

import com.jetbrains.productregistry.common.AbstractIntegrationTest
import com.jetbrains.productregistry.data.buildTestProduct
import com.jetbrains.productregistry.persistance.ProductRepository
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ProductDataServiceIntegrationTest: AbstractIntegrationTest() {
    @Autowired
    lateinit var productDataService: ProductDataService

    @Autowired
    lateinit var productRepository: ProductRepository

    @Test
    @Order(1)
    fun getProductForVersioningScanTest() {
        val product1 = buildTestProduct("A") {
            lock(true)
        }
        val product2 = buildTestProduct("B")
        val product3 = buildTestProduct("C")
        productRepository.save(product1)
        productRepository.save(product2)
        productRepository.save(product3)
        val productToScan = productDataService.getProductForVersioningScan()
        Assertions.assertTrue(productToScan?.code == product2.code)
    }

    @Test
    @Order(2)
    fun freeProductForVersioningScan() {
        val product = buildTestProduct("D") {
            lock(true)
        }
        val savedProduct = productRepository.save(product)
        Assertions.assertTrue(savedProduct.lock)
        Assertions.assertFalse(
            productRepository.findProductByCode("D")?.lock == false
        )

    }
}
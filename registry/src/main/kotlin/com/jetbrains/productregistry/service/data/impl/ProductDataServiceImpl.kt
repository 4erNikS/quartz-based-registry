package com.jetbrains.productregistry.service.data.impl

import com.jetbrains.productregistry.model.logic.ProductInfoDto
import com.jetbrains.productregistry.model.persitence.Product
import com.jetbrains.productregistry.persistance.ProductRepository
import com.jetbrains.productregistry.service.data.ProductDataService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class ProductDataServiceImpl(
    private val productRepository: ProductRepository
): ProductDataService {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun getProductForVersioningScan(): Product? {
        val product = productRepository.findFirstByLockOrderByLastUpdatedDesc(false)
        product?.let {
            if(it.lock) {
                log.warn("Product is locked by another instance. Skipping...")
                return null
            }
            it.lock = true
            return productRepository.save(it)
        }
        log.debug("Cannot find product for update")
        return null
    }

    override fun freeProductForVersioningScan(productId: Long) {
        val product = productRepository.findById(productId)
        product.ifPresent {
            it.lock = false
            productRepository.save(it)
        }
    }

    override fun createNewProducts(availableProducts: List<ProductInfoDto>): Set<Product> {
        return availableProducts
            .asSequence()
            .filter { productRepository.findProductByCode(it.code) == null }
            .map { productRepository.save(
                Product(
                    code = it.code,
                    name = it.name
                )
            )}.toSet()
    }


}
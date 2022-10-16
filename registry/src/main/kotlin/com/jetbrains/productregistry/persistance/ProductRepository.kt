package com.jetbrains.productregistry.persistance

import com.jetbrains.productregistry.model.persitence.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository
import javax.persistence.LockModeType

@Repository
interface ProductRepository: JpaRepository<Product, Long> {

    fun findProductByCode(code: String): Product?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findFirstByLockOrderByLastUpdatedDesc(lock: Boolean): Product?
}
package com.jetbrains.productregistry.persistance

import com.jetbrains.productregistry.model.persitence.ProductVersion
import com.jetbrains.productregistry.model.persitence.enums.VersionProcessingStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository
import javax.persistence.LockModeType

@Repository
interface ProductVersionRepository: JpaRepository<ProductVersion, Long> {

    fun findProductVersionByVersionCode(versionCode: String): ProductVersion?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findFirstByLockOrderAndStatusByLastUpdatedDesc(lock: Boolean, status: VersionProcessingStatus): ProductVersion?
}
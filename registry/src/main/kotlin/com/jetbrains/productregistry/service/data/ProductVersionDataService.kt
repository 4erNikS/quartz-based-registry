package com.jetbrains.productregistry.service.data

import com.jetbrains.productregistry.model.logic.VersionInfoDto
import com.jetbrains.productregistry.model.persitence.Product
import com.jetbrains.productregistry.model.persitence.ProductVersion

interface ProductVersionDataService {

    /**
     * Method that checks if version is in registry
     * and if it's require update
     */
    fun checkProductVersion(versionInfo: VersionInfoDto, product: Product)

    fun getVersionForProcessing(): ProductVersion?

    fun finishVersionProcessing(distrInfo: String, version: ProductVersion)
}
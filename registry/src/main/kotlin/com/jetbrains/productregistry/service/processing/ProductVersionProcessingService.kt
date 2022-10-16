package com.jetbrains.productregistry.service.processing

import com.jetbrains.productregistry.model.persitence.Product
import com.jetbrains.productregistry.model.persitence.ProductVersion

/**
 * Service that responsible for logic related with updates
 * of product version
 */
interface ProductVersionProcessingService {

    /**
     * Method for registering information about all versions
     * of the product
     */
    fun registerNewProductVersions(product: Product)

    /**
     * Method that get finds the oldest possible version
     * that requires update
     * Method set lock on entity to avoid
     * several instances to check the same version
     */
    fun getVersionForProcessing(): ProductVersion?

    /**
     * Method that loads distr
     * retrieves information file
     * and updates information in db
     */
    fun loadDistrInfoForVersion(version: ProductVersion)

}
package com.jetbrains.productregistry.service.processing.impl

import com.jetbrains.productregistry.model.persitence.Product
import com.jetbrains.productregistry.model.persitence.ProductVersion
import com.jetbrains.productregistry.service.data.ProductVersionDataService
import com.jetbrains.productregistry.service.integration.DistrLoadingService
import com.jetbrains.productregistry.service.integration.ProductsIntegrationService
import com.jetbrains.productregistry.service.processing.ProductVersionProcessingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.net.URL

@Service
class ProductVersionProcessingServiceImpl(
    private val productVersionDataService: ProductVersionDataService,
    private val productsIntegrationService: ProductsIntegrationService,
    private val distrLoadingService: DistrLoadingService
): ProductVersionProcessingService {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun registerNewProductVersions(product: Product) {
        log.debug("Updating available versions for product with code: $product")
        val availableVersions = productsIntegrationService
            .getProductVersions(product.code).get(0).releases
        //have here separate call for each one to have change to split proccesing in different threads
        availableVersions.forEach{
            productVersionDataService.checkProductVersion(it, product)
        }
    }

    override fun getVersionForProcessing(): ProductVersion? {
        return productVersionDataService.getVersionForProcessing()
    }

    override fun loadDistrInfoForVersion(version: ProductVersion) {
        try {
            productVersionDataService.finishVersionProcessing(
                distrLoadingService.getLinuxDistributiveInfoJson(
                    URL(version.distrLink)
                ),
                version
            )
        } catch (e:Exception) {
            log.error("Error happened during processing version ${version.versionCode}", e)
            productVersionDataService.freeVersionForProcessingInCaseOfError(version)

        }
    }

}
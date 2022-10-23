package com.jetbrains.productregistry.jobs

import com.jetbrains.productregistry.service.processing.ProductProcessingService
import com.jetbrains.productregistry.service.processing.ProductVersionProcessingService
import org.quartz.DisallowConcurrentExecution
import org.quartz.Job;
import org.quartz.JobExecutionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * Job that pools information from jetbrain service
 * to get available all versions and marks ones
 * that should be updated
 * Criteria for update:
 * - size info changed
 * - checksum links differs from the one in db
 * -
 */
class ProductVersionScanJob: Job {
    @Autowired
    private lateinit var productProcessingService: ProductProcessingService

    @Autowired
    private lateinit var versionProcessingService: ProductVersionProcessingService

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    //TODO missing part with exception handling and calling for rollback logic
    override fun execute(context: JobExecutionContext?) {
        log.info("Starting job for retrieving available information about versions")
        productProcessingService.getProductForVersioningScan()?.let {
            log.info("Updating information about verions for product with code: ${it.code}")
            try {
                versionProcessingService.registerNewProductVersions(it)
                productProcessingService.freeProductForVersioningScan(it.id)
            } catch (e: Exception) {
                productProcessingService.freeProductForVersioningScan(it.id)
                throw e
            }
            return
        }
        log.warn("Have no available products for scan. Skipping this this schedule run")
    }
}
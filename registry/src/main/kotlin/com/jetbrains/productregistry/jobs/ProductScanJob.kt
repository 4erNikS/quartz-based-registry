package com.jetbrains.productregistry.jobs

import com.jetbrains.productregistry.service.processing.ProductProcessingService
import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * Job for checking list of products
 * One common job - that checking only available products
 */
@DisallowConcurrentExecution
class ProductScanJob: Job {
    @Autowired
    private lateinit var productService: ProductProcessingService

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    //TODO missing part with exception handling and calling for rollback logic
    override fun execute(context: JobExecutionContext?) {
        productService.registerNewProducts()
    }
}
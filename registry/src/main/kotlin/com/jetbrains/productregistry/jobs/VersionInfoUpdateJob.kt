package com.jetbrains.productregistry.jobs

import com.jetbrains.productregistry.service.processing.ProductVersionProcessingService
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * Job for loading information about one specific version
 * that is latest of non-processed yet
 * "Sub job" of [ProductVersionScanJob]
 */
class VersionInfoUpdateJob: Job {
    @Autowired
    private lateinit var versionProcessingService: ProductVersionProcessingService

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(context: JobExecutionContext?) {
        versionProcessingService.getVersionForProcessing()?.let{
            versionProcessingService.loadDistrInfoForVersion(it)
        }
    }
}
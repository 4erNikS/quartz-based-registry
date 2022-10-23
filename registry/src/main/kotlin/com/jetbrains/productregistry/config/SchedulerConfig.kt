package com.jetbrains.productregistry.config

import com.jetbrains.productregistry.jobs.ProductScanJob
import com.jetbrains.productregistry.jobs.ProductVersionScanJob
import com.jetbrains.productregistry.jobs.VersionInfoUpdateJob
import com.jetbrains.productregistry.properties.ScheduleProperties
import liquibase.integration.spring.SpringLiquibase
import org.quartz.*
import org.quartz.SimpleScheduleBuilder.simpleSchedule
import org.quartz.spi.JobFactory
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import java.io.IOException
import java.util.*
import javax.sql.DataSource


/**
 * Class for configuring Quartz scheduled jobs
 * We're setting jobs' datasource as our database
 * in order to have multi instances support
 */
@Configuration
@EnableAsync
@EnableScheduling
class SchedulerConfig(
    private val schedulerProperties: ScheduleProperties
) {
    @Bean
    fun jobFactory(
        applicationContext: ApplicationContext?,
        springLiquibase: SpringLiquibase?
    ): JobFactory? {
        val jobFactory = AutowiringSpringBeanJobFactory()
        jobFactory.setApplicationContext(applicationContext!!)
        return jobFactory
    }

    @Bean
    @Throws(IOException::class)
    fun schedulerFactoryBean(
        dataSource: DataSource,
        jobFactory: JobFactory,
    ): SchedulerFactoryBean? {
        val factory = SchedulerFactoryBean()
        factory.setDataSource(dataSource)
        factory.setJobFactory(jobFactory)
        factory.setQuartzProperties(quartzProperties())
        return factory
    }

    @Bean
    @Throws(IOException::class)
    fun quartzProperties(): Properties {
        val propertiesFactoryBean = PropertiesFactoryBean()
        propertiesFactoryBean.setLocation(ClassPathResource("quartz.properties"))
        propertiesFactoryBean.afterPropertiesSet()
        return propertiesFactoryBean.getObject()!!
    }


    @Bean
    @Throws(SchedulerException::class)
    fun scheduler(factory: SchedulerFactoryBean): Scheduler? {
        val scheduler: Scheduler = factory.scheduler
        scheduler.start()
        scheduler.scheduleProductProcessingJob()
        scheduler.scheduleProductVersionsProcessingJob()
        return scheduler
    }

    /**
     * Configuration for non-concurrent
     * job for loading information about possible products
     */
    private fun Scheduler.scheduleProductProcessingJob() {
        scheduleRegistryJob(schedulerProperties.productLoadSecInterval, PRODUCT_JOB_GROUP, ProductScanJob::class.java)
    }

    /**
     * Configuration job for loading information
     * about possible one of the product's versions
     */
    private fun Scheduler.scheduleProductVersionsProcessingJob() {
        scheduleRegistryJob(schedulerProperties.versionLoadSecInterval, PRODUCT_VERSIONS_JOB_GROUP, ProductVersionScanJob::class.java)
    }

    /**
     * Job configuration for loading information
     * about one specific distributive
     */
    private fun Scheduler.scheduleProductDistrProcessingJob() {
        scheduleRegistryJob(schedulerProperties.distrInfoSecInterval, PRODUCT_DISTR_JOB_GROUP, VersionInfoUpdateJob::class.java)
    }

    private fun <T: Job> Scheduler.scheduleRegistryJob(
        interval: Int, group: String,
        jobClass: Class<T>
    ) {
        val id = UUID.randomUUID().toString()

        val job = JobBuilder.newJob(jobClass)
            .withIdentity(
                id,
                group
            )
            .requestRecovery(true)
            .build()

        val trigger: Trigger = TriggerBuilder.newTrigger()
            .forJob(job)
            .withIdentity("$id-trigger", group)
            .startNow()
            .withSchedule(
                simpleSchedule()
                    .repeatForever()
                    .withIntervalInSeconds(interval)
            )
            .build()
        this.scheduleJob(job, trigger)
    }
}
private const val PRODUCT_JOB_GROUP = "productJobs"
private const val PRODUCT_VERSIONS_JOB_GROUP = "versionJobs"
private const val PRODUCT_DISTR_JOB_GROUP = "distrJobs"
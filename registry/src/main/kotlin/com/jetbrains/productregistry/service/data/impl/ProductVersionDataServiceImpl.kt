package com.jetbrains.productregistry.service.data.impl

import com.jetbrains.productregistry.model.logic.VersionInfoDto
import com.jetbrains.productregistry.model.logic.VersionLoadInfo
import com.jetbrains.productregistry.model.persitence.Product
import com.jetbrains.productregistry.model.persitence.ProductVersion
import com.jetbrains.productregistry.model.persitence.enums.VersionProcessingStatus
import com.jetbrains.productregistry.model.persitence.enums.VersionType
import com.jetbrains.productregistry.persistance.ProductVersionRepository
import com.jetbrains.productregistry.service.data.ProductVersionDataService
import com.jetbrains.productregistry.service.data.exception.DistributiveLoadInfoMissingException
import com.jetbrains.productregistry.service.data.exception.NonSupportedReleaseTypeException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.lang.Exception
import java.sql.Timestamp
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Service
@Transactional
class ProductVersionDataServiceImpl(
    private val productVersionRepository: ProductVersionRepository
): ProductVersionDataService {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun checkProductVersion(versionInfo: VersionInfoDto, product: Product) {
        productVersionRepository.findProductVersionByVersionCode(
            versionInfo.getProductVersionCode(product.code)
        )?.let {
            return it.updateVersionIfNeeded(versionInfo)
        }
        versionInfo.registerNewProductVersion(product)
    }

    override fun getVersionForProcessing(): ProductVersion? {
        productVersionRepository
            .findFirstByLockOrderAndStatusByLastUpdatedDesc(
                false, VersionProcessingStatus.NEED_UPDATE
            )?.let{
            if(it.lock) {
                log.warn("Entity is locked by another instance. Skipping...")
                return null
            }
            it.lock = true
            return productVersionRepository.save(it)
        }
        return null
    }

    override fun finishVersionProcessing(distrInfo: String, version: ProductVersion) {
        version.lock = false
        version.status = VersionProcessingStatus.UPLOADED
        version.lastUpdated = Timestamp.from(ZonedDateTime.now().toInstant())
    }

    private fun ProductVersion.updateVersionIfNeeded(versionInfo: VersionInfoDto) {
        if (isSameVersion(versionInfo)) {
            log.debug("Version with id: ${id} don't need any update")
            return
        }
        log.debug("Version with ${id} needs update distr info")
        val loadInfo = versionInfo.getRegisterDistro()
        status = VersionProcessingStatus.NEED_UPDATE
        checksumCheckLink = loadInfo.checksumLink
        distrLink = loadInfo.link
        lastUpdated = Timestamp.from(ZonedDateTime.now().toInstant())
        productVersionRepository.save(this)
    }

    private fun ProductVersion.isSameVersion(versionInfo: VersionInfoDto): Boolean {
        val loadInfo = versionInfo.getRegisterDistro()
        //TODO pass here actually values
        return checksumCheckLink == loadInfo.checksumLink && sizeInfo == loadInfo.size
    }

    private fun VersionInfoDto.getRegisterDistro(): VersionLoadInfo {
        return downloads[DISTR_NAME] ?: throw DistributiveLoadInfoMissingException()
    }

    private fun VersionInfoDto.registerNewProductVersion(product: Product) {
        val versionCode = getProductVersionCode(product.code)
        log.info("Saving new version for product with code $versionCode")
        val loadInfo = getRegisterDistro()
        val version = ProductVersion(
            product = product,
            versionCode = versionCode,
            type = getReleaseType(),
            distrLink = loadInfo.link,
            checksumCheckLink = loadInfo.checksumLink,
            releaseDate = date,
            sizeInfo = loadInfo.size
        )
        productVersionRepository.save(version)
    }

    private fun VersionInfoDto.getProductVersionCode(productCode: String): String {
        return productCode + DELIMITER + this.date.toInstant().epochSecond +
                DELIMITER + getReleaseType()
    }

    private fun VersionInfoDto.getReleaseType(): VersionType {
        return try {
            VersionType.valueOf(type.uppercase())
        } catch (e: Exception) {
            throw NonSupportedReleaseTypeException()
        }
    }
}
private const val DELIMITER = "_"
private const val DISTR_NAME = "linux"
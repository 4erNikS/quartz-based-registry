package com.jetbrains.productregistry.service.integration.impl

import com.jetbrains.productregistry.service.integration.DistrLoadingService
import com.jetbrains.productregistry.service.integration.exception.DistrUnzipException
import com.jetbrains.productregistry.service.integration.exception.LoadingDistrException
import com.jetbrains.productregistry.service.integration.exception.InfoDistrFileIsMissingException
import com.jetbrains.productregistry.service.integration.exception.TempFileCreationException
import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.io.FileUtils
import org.springframework.stereotype.Service
import java.io.*
import java.net.URL
import javax.annotation.PostConstruct


@Service
class DistrLoadingServiceImpl: DistrLoadingService {
//    @PostConstruct
//    fun test() {
//        getLinuxDistributiveInfoJson(File("/Users/ncheredn/Documents/chns/dev/product-builds-registry/registry/src/test/resources/ideaIC-2022.2.3.tar.gz").toURI().toURL())
//    }

    override fun getLinuxDistributiveInfoJson(downloadUrl: URL): String {
        return createTempFileForLoadingDistr()
            .loadDistrToFile(downloadUrl)
            .extractDistrJsonInfoFile()
            .processDistrJsonInfoFile()
    }

    /**
     * Method for creating temporal file to store
     * loaded distributive for further processing
     */
    private fun createTempFileForLoadingDistr(): File {
        val tempFile = try {
            File.createTempFile(DISTR_FILES_PREFIX, ".tar.gz");
        } catch (e: Exception) {
            throw TempFileCreationException()
        }
        tempFile.deleteOnExit()
        return tempFile
    }

    /**
     * Simple loading using Apache Commons lib
     */
    private fun File.loadDistrToFile(downloadUrl: URL): File {
        try {
            FileUtils.copyURLToFile(
                downloadUrl,
                this,
                CONNECT_TIMEOUT,
                READ_TIMEOUT
            )
        } catch (e: Exception) {
            throw LoadingDistrException()
        }
        return this
    }

    /**
     * Method that process distr file
     * unzip required info file,
     * saves it as temp file
     * and returns it for further processing
     */
    private fun File.extractDistrJsonInfoFile(): File {
        try {
            val gZipStream = GzipCompressorInputStream(FileInputStream(this))
            val tarStream = TarArchiveInputStream(gZipStream)
            val searchName = tarStream.nextEntry.name + REQUIRED_DISTR_INFO_FILE_NAME
            var tarEntry: ArchiveEntry?
            while (tarStream.nextEntry.also { tarEntry = it } != null) {
                if (tarEntry?.name == searchName) {
                    val jsonInfoFile = extractInfoFile(tarStream)
                    this.delete()
                    return jsonInfoFile
                }
            }
        } catch (e: Exception) {
            throw DistrUnzipException()
        }
        throw InfoDistrFileIsMissingException()
    }

    /**
     * Explicit method for processing
     * Added to have more convinient way
     * to adjust logic for processing file to
     * specific business cases
     * For now just returning whole file to be saved
     */
    private fun File.processDistrJsonInfoFile(): String {
        return this.readText()
    }



    /**
     * Method for temporal saving
     * specific file from distributive
     *
     * Explicit method is used
     * in order to have a change to add
     * long processing operations
     * or to have another task
     * for processing in another thread
     */
    private fun extractInfoFile(inputStream: InputStream): File {
        val jsonInfoFile = File.createTempFile(INFO_FILES_PREFIX, ".json")
        val outputStream = BufferedOutputStream(FileOutputStream(jsonInfoFile))
        val bytesIn = ByteArray(READ_BUFFER_SIZE)
        var read: Int
        while (inputStream.read(bytesIn).also { read = it } != -1) {
            outputStream.write(bytesIn, 0, read)
        }
        outputStream.close()
        return jsonInfoFile
    }
}

private const val DISTR_FILES_PREFIX = "distr_registry_distr"
private const val INFO_FILES_PREFIX = "distr_registry_inf"
private const val REQUIRED_DISTR_INFO_FILE_NAME = "product-info.json"
private const val READ_BUFFER_SIZE = 4096
private const val CONNECT_TIMEOUT = 10000
private const val READ_TIMEOUT = 10000
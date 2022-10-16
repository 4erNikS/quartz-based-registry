package com.jetbrains.productregistry.service.integration.impl

import com.jetbrains.productregistry.properties.ClientProperties
import com.jetbrains.productregistry.model.logic.ProductInfoDto
import com.jetbrains.productregistry.model.logic.ProductVersionDto
import com.jetbrains.productregistry.service.integration.ProductsIntegrationService
import com.jetbrains.productregistry.service.integration.exception.ConnectionException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import javax.annotation.PostConstruct

//TODO add proper client base on swagger spec (ideally) draft version of client
@Service
class ProductsIntegrationServiceImpl(
    private val restTemplate: RestTemplate,
    private val clientProperties: ClientProperties
): ProductsIntegrationService {

//    @PostConstruct
//    fun test() {
////        val result = getProductList()
//        val result = getProductVersions("IIU")
//        result.size
//    }

    override fun getProductList(): List<ProductInfoDto> {
        val response = restTemplate.getForEntity(
            clientProperties.productsInfoUrl,
            Array<ProductInfoDto>::class.java
        )
        if (response.statusCode != HttpStatus.OK) {
            throw ConnectionException()
        }
        if (response.hasBody()) {
            return response.body?.asList() ?: arrayListOf()
        }
        return arrayListOf()
    }

    override fun getProductVersions(productCode: String): List<ProductVersionDto> {
        val response = restTemplate.getForEntity(
            clientProperties.productReleaseInfoUrl+productCode,
            Array<ProductVersionDto>::class.java
        )
        if (response.statusCode != HttpStatus.OK) {
            throw ConnectionException()
        }
        if (response.hasBody()) {
            return response.body?.asList() ?: arrayListOf()
        }
        return arrayListOf()
    }
}
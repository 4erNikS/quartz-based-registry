package com.jetbrains.productregistry.data

import com.jetbrains.productregistry.model.persitence.Product
import com.jetbrains.productregistry.model.persitence.ProductVersion
import com.jetbrains.productregistry.model.persitence.enums.VersionType
import java.util.*

class ProductVersionBuilder internal constructor(
    val product: Product,
    val releaseDate: Date,
    val type: VersionType
) {

    private var distrLink = "distrLink"
    private var checksumCheckLink = "checksumCheckLink"
    private var sizeInfo = 100L

    fun distrLink(distrLink: String) {
        this.distrLink = distrLink
    }

    fun checksumCheckLink(checksumCheckLink: String) {
        this.checksumCheckLink = checksumCheckLink
    }

    fun sizeInfo(sizeInfo: Long) {
        this.sizeInfo = sizeInfo
    }

    fun build() = ProductVersion(
        product = product,
        versionCode = product.code + "_" + releaseDate.toInstant().epochSecond + "_" + type,
        type = type,
        distrLink = distrLink,
        checksumCheckLink = checksumCheckLink,
        releaseDate = releaseDate,
        sizeInfo = sizeInfo
    )
}

fun buildTestProductVersion(
    product: Product, releaseDate: Date = Date(),
    type: VersionType = VersionType.RELEASE,
    lambda: ProductVersionBuilder.() -> Unit = {}
) = ProductVersionBuilder(product, releaseDate, type).apply(lambda).build()
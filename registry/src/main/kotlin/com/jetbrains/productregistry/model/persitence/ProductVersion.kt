package com.jetbrains.productregistry.model.persitence

import com.jetbrains.productregistry.model.persitence.enums.VersionProcessingStatus
import com.jetbrains.productregistry.model.persitence.enums.VersionType
import java.util.Date
import java.sql.Timestamp
import java.time.ZonedDateTime
import javax.persistence.*

/**
 * Entity that keeps information about release version
 * This entity is firstly created once version was received
 * Each version can be updated
 */
@Entity
@Table(name = "products")
data class ProductVersion(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    var product: Product,

    /**
     * Concatenation of date, release type and product code
     */
    @Column(unique = true)
    val versionCode: String,

    var lock: Boolean = false,

    @Enumerated(EnumType.STRING)
    var status: VersionProcessingStatus = VersionProcessingStatus.NEED_UPDATE,

    @Enumerated(EnumType.STRING)
    val type: VersionType,

    var productInfo: String? = null,

    var distrLink: String,

    var checksumCheckLink: String,

    var releaseDate: Date,

    var sizeInfo: Long,

    var lastUpdated: Timestamp = Timestamp.from(ZonedDateTime.now().toInstant())
)
package com.jetbrains.productregistry.model.persitence

import java.sql.Timestamp
import java.time.ZonedDateTime
import javax.persistence.*

/**
 * Entity class that keeps common information about product
 * such as clion, intelij idea etc
 * Entity can be extended with required fields
 * for now added to keep hierarchy of domain model
 */
@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    /**
     * Short codename of a product
     * E.g. AC (AppCode), CL (CLion)
     */
    @Column(unique=true)
    val code: String,

    /**
     * Full name of a product
     */
    val name: String,

    /**
     * Lock attr to avoid several jobs
     * update same entity
     */
    var lock: Boolean = false,

    /**
     * Technical field to keep track
     * for how long records was without update
     */
    var lastUpdated: Timestamp = Timestamp.from(ZonedDateTime.now().toInstant())
)
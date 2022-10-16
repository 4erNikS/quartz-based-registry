package com.jetbrains.productregistry.model.persitence.enums

/**
 * Data layer enum storing possible
 * types of versions' release
 */
enum class VersionType(val code: String) {
    RELEASE("release"),
    RC("rc"),
    EAP("eap")
}
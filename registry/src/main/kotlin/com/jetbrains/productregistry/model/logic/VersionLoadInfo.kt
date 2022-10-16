package com.jetbrains.productregistry.model.logic

import com.fasterxml.jackson.annotation.JsonIncludeProperties

@JsonIncludeProperties
data class VersionLoadInfo(
    val link: String,
    val size: Long,
    val checksumLink: String
)
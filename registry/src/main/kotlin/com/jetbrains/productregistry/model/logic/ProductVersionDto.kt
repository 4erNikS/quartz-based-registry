package com.jetbrains.productregistry.model.logic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties
data class ProductVersionDto(
    val code: String,
    val releases: Array<VersionInfoDto>
)
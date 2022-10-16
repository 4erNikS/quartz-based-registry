package com.jetbrains.productregistry.model.logic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.Date

@JsonIgnoreProperties
data class VersionInfoDto(
    val date: Date,
    val type: String,
    val downloads: Map<String, VersionLoadInfo>
)
package com.jetbrains.productregistry.model.logic

import com.fasterxml.jackson.annotation.JsonIncludeProperties

@JsonIncludeProperties
data class ProductInfoDto(
    val code: String,
    val name: String
)

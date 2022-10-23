package com.jetbrains.productregistry.data

import com.jetbrains.productregistry.model.persitence.Product

class ProductBuilder internal constructor(
    val code: String
){
    private var name: String = code
    private var lock: Boolean = false
    fun name(name: String) {
        this.name = name
    }

    fun lock(lock: Boolean) {
       this.lock = lock
    }

    fun build() = Product(
        code = code,
        name = name,
        lock = lock
    )
}

fun buildTestProduct(
    code: String, lambda: ProductBuilder.() -> Unit = {}
) = ProductBuilder(code).apply(lambda).build()

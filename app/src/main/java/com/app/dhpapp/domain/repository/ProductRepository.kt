package com.app.dhpapp.domain.repository

import com.app.dhpapp.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(
        onSuccess: (List<Product>) -> Unit,
        onError: (String) -> Unit
    )

    suspend fun addProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun updateProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun deleteProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

}
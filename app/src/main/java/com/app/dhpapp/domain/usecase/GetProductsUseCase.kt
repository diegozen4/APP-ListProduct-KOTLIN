package com.app.dhpapp.domain.usecase

import com.app.dhpapp.domain.model.Product
import com.app.dhpapp.domain.repository.ProductRepository

class GetProductsUseCase(private val productRepository: ProductRepository) {
    suspend fun execute(
        onSuccess: (List<Product>) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            productRepository.getProducts(onSuccess, onError)
        } catch (e: Exception) {
            onError(e.message ?: "Error desconocido")
        }
    }
}
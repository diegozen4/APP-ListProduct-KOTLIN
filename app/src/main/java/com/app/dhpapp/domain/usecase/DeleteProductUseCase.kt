package com.app.dhpapp.domain.usecase

import com.app.dhpapp.domain.model.Product
import com.app.dhpapp.domain.repository.ProductRepository

class DeleteProductUseCase(private val productRepository: ProductRepository) {
    suspend fun execute(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        productRepository.deleteProduct(product, onSuccess, onError)
    }
}
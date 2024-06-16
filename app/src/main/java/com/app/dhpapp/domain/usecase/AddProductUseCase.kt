package com.app.dhpapp.domain.usecase

import com.app.dhpapp.domain.model.Product
import com.app.dhpapp.domain.repository.ProductRepository

class AddProductUseCase(private val productRepository: ProductRepository) {
    suspend fun execute(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        productRepository.addProduct(product, onSuccess, onError)
    }
}

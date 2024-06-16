package com.app.dhpapp.data.repository

import com.app.dhpapp.data.datasource.RemoteProductDataSource
import com.app.dhpapp.domain.model.Product
import com.app.dhpapp.domain.repository.ProductRepository

class ProductRepositoryImpl(private val remoteDataSource: RemoteProductDataSource) : ProductRepository {
    override suspend fun getProducts(onSuccess: (List<Product>) -> Unit, onError: (String) -> Unit) {
        remoteDataSource.getProducts(onSuccess, onError)
    }

    override suspend fun addProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        remoteDataSource.addProduct(product, onSuccess, onError)
    }

    override suspend fun updateProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        remoteDataSource.updateProduct(product, onSuccess, onError)
    }

    override suspend fun deleteProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        remoteDataSource.deleteProduct(product, onSuccess, onError)
    }
}

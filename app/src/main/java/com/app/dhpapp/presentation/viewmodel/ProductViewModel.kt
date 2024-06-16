package com.app.dhpapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.dhpapp.data.datasource.RemoteProductDataSource
import com.app.dhpapp.data.repository.ProductRepositoryImpl
import com.app.dhpapp.domain.model.Product
import com.app.dhpapp.domain.usecase.AddProductUseCase
import com.app.dhpapp.domain.usecase.DeleteProductUseCase
import com.app.dhpapp.domain.usecase.GetProductsUseCase
import com.app.dhpapp.domain.usecase.UpdateProductUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val getProductsUseCase =
        GetProductsUseCase(ProductRepositoryImpl(RemoteProductDataSource(application)))
    private val addProductUseCase =
        AddProductUseCase(ProductRepositoryImpl(RemoteProductDataSource(application)))
    private val updateProductUseCase =
        UpdateProductUseCase(ProductRepositoryImpl(RemoteProductDataSource(application)))
    private val deleteProductUseCase =
        DeleteProductUseCase(ProductRepositoryImpl(RemoteProductDataSource(application)))

    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>>
        get() = _productList

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            try {
                val products = getProductsUseCase.execute()
                _productList.value = products
            } catch (e: Exception) {
                // Manejar errores
            }
        }
    }

    fun addProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                addProductUseCase.execute(product)
                onSuccess()
                getProducts() // Update list
            } catch (e: Exception) {
                onError(e.message ?: "Error al agregar el producto")
            }
        }
    }

    fun updateProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                updateProductUseCase.execute(product)
                onSuccess()
                getProducts() // Update list
            } catch (e: Exception) {
                onError(e.message ?: "Error al actualizar el producto")
            }
        }
    }

    fun deleteProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                deleteProductUseCase.execute(product)
                onSuccess()
                getProducts() // Update list
            } catch (e: Exception) {
                onError(e.message ?: "Error al eliminar el producto")
            }
        }
    }
}

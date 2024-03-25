package com.app.dhpapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.dhpapp.model.Product
import com.app.dhpapp.repository.ProductRepository

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository = ProductRepository(application)

    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>>
        get() = _productList

    init {
        getProducts()
    }

    fun getProducts() {
        repository.getProducts(
            onSuccess = { products ->
                _productList.value = products
            },
            onError = { error ->
                // Manejar errores
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        repository.cancelAllRequests()
    }
}

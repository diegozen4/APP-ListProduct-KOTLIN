package com.app.dhpapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.dhpapp.model.Product

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private var appContext: Context? = application.applicationContext

    private val BASE_URL = "http://10.0.2.2"
    private lateinit var queue: RequestQueue

    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>>
        get() = _productList

    init {
        getProducts()
    }

    fun getProducts() {
        appContext?.let { context ->
            queue = Volley.newRequestQueue(context)
            val url = "$BASE_URL/apiDhp/GET_Products.php"

            val request = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    val products = mutableListOf<Product>()
                    val productsArray = response.getJSONArray("response")
                    for (i in 0 until productsArray.length()) {
                        val productObj = productsArray.getJSONObject(i)
                        val product = Product(
                            productObj.getInt("id_Product"),
                            productObj.getString("name"),
                            productObj.getString("description"),
                            productObj.getDouble("price")
                        )
                        products.add(product)
                    }
                    _productList.value = products
                },
                { _ ->
                    // Manejar errores
                })

            queue.add(request)
        }
    }

    override fun onCleared() {
        super.onCleared()
        appContext = null // Limpiar la referencia al contexto
        queue.cancelAll(this)
    }
}

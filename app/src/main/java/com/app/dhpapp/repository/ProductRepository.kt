package com.app.dhpapp.repository

import android.app.Application
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.dhpapp.BaseApi
import com.app.dhpapp.model.Product
import org.json.JSONObject

class ProductRepository(private val application: Application) {

    private lateinit var queue: RequestQueue

    fun getProducts(onSuccess: (List<Product>) -> Unit, onError: (String) -> Unit) {
        queue = Volley.newRequestQueue(application.applicationContext)
        val url = "${BaseApi.BASE_URL}/apiDhp/GET_Products.php"

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
                        productObj.getDouble("price") ,
                        productObj.getString("image") // Agregar la imagen en base64

                    )
                    products.add(product)
                }
                onSuccess.invoke(products)
            },
            { error ->
                onError.invoke(error.message ?: "Error desconocido")
            })

        queue.add(request)
    }

    fun cancelAllRequests() {
        queue.cancelAll(this)
    }
}

package com.app.dhpapp.data.datasource

import android.app.Application
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.dhpapp.data.network.BaseApi
import com.app.dhpapp.domain.model.Product
import org.json.JSONObject

class RemoteProductDataSource(private val application: Application) {

    private val queue: RequestQueue = Volley.newRequestQueue(application.applicationContext)

    fun getProducts(onSuccess: (List<Product>) -> Unit, onError: (String) -> Unit) {
        val products = mutableListOf<Product>()
        val url = "${BaseApi.BASE_URL}GET_Products.php"

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val productsArray = response.getJSONArray("response")
            for (i in 0 until productsArray.length()) {
                val productObj = productsArray.getJSONObject(i)
                val product = Product(
                    productObj.getInt("id_Product"),
                    productObj.getString("name"),
                    productObj.getString("description"),
                    productObj.getString("price"),
                    productObj.getString("image")
                )
                products.add(product)
            }
            onSuccess.invoke(products) // Llama a onSuccess con la lista de productos obtenida
        }, { error ->
            Log.e("ProductRepository", "getProducts: Error - ${error.message ?: "Unknown error"}")
            onError.invoke(error.message ?: "Error desconocido")
        })

        queue.add(request)
    }

    suspend fun addProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val url = "${BaseApi.BASE_URL}ADD_Product.php"

        val params = HashMap<String, String>()
        params["name"] = product.name
        params["description"] = product.description
        params["price"] = product.price
        params["image"] = product.image

        val request = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                Log.d("ProductRepository", "addProduct: Response received - $response")
                onSuccess.invoke()
            },
            Response.ErrorListener { error ->
                Log.e("ProductRepository", "addProduct: Error - ${error.message ?: "Unknown error"}")
                onError.invoke(error.message ?: "Error desconocido")
            }
        ) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }
        queue.add(request)
    }

    suspend fun updateProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val url = "${BaseApi.BASE_URL}UPDATE_Product.php"

        val params = HashMap<String, String>()
        params["id"] = product.id.toString()
        params["name"] = product.name
        params["description"] = product.description
        params["price"] = product.price
        params["image"] = product.image

        val request = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                Log.d("ProductRepository", "updateProduct: Response received - $response")
                onSuccess.invoke()
            },
            Response.ErrorListener { error ->
                Log.e("ProductRepository", "updateProduct: Error - ${error.message ?: "Unknown error"}")
                onError.invoke(error.message ?: "Error desconocido")
            }
        ) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }
        queue.add(request)
    }

    suspend fun deleteProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val url = "${BaseApi.BASE_URL}DELETE_Product.php"

        val params = HashMap<String, String>()
        params["id"] = product.id.toString()

        val request = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                Log.d("ProductRepository", "deleteProduct: Response received - $response")
                onSuccess.invoke()
            },
            Response.ErrorListener { error ->
                Log.e("ProductRepository", "deleteProduct: Error - ${error.message ?: "Unknown error"}")
                onError.invoke(error.message ?: "Error desconocido")
            }
        ) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }
        queue.add(request)
    }
}

package com.app.dhpapp.repository

import android.app.Application
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.dhpapp.BaseApi
import com.app.dhpapp.model.Product
import org.json.JSONObject

class ProductRepository(private val application: Application) {

    private lateinit var queue: RequestQueue

    fun getProducts(onSuccess: (List<Product>) -> Unit, onError: (String) -> Unit) {
        queue = Volley.newRequestQueue(application.applicationContext)
        val url = "${BaseApi.BASE_URL}GET_Products.php"

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val products = mutableListOf<Product>()
            val productsArray = response.getJSONArray("response")
            for (i in 0 until productsArray.length()) {
                val productObj = productsArray.getJSONObject(i)
                val product = Product(
                    productObj.getInt("id_Product"),
                    productObj.getString("name"),
                    productObj.getString("description"),
                    productObj.getString("price"),
                    productObj.getString("image") // Agregar la imagen en base64
                )
                products.add(product)
            }
            Log.d("ProductRepository", "getProducts: Success - Products received: ${products.size}")
            onSuccess.invoke(products)
        }, { error ->
            Log.e("ProductRepository", "getProducts: Error - ${error.message ?: "Unknown error"}")
            onError.invoke(error.message ?: "Error desconocido")
        })

        queue.add(request)
    }

    fun addProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        queue = Volley.newRequestQueue(application.applicationContext)
        val url = "${BaseApi.BASE_URL}ADD_Product.php"

        Log.d("ProductRepository", "addProduct: Sending request to $url with data: $product")

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
    fun updateProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        queue = Volley.newRequestQueue(application.applicationContext)
        val url = "${BaseApi.BASE_URL}UPDATE_Product.php"

        Log.d("ProductRepository", "updateProduct: Sending request to $url with data: $product")

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
    fun deleteProduct(product: Product, onSuccess: () -> Unit, onError: (String) -> Unit) {
        queue = Volley.newRequestQueue(application.applicationContext)
        val url = "${BaseApi.BASE_URL}DELETE_Product.php"

        Log.d("ProductRepository", "deleteProduct: Sending request to $url with data: $product")

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

    fun cancelAllRequests() {
        queue.cancelAll(this)
    }
}

package com.app.dhpapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val BASE_URL = "http://10.0.2.2:5278" // Reemplaza la dirección IP con la de tu servidor

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var productList: MutableList<Product>

    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productList = mutableListOf()
        adapter = ProductAdapter(productList)
        recyclerView.adapter = adapter

        queue = Volley.newRequestQueue(this)

        fetchData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchData() {
        val url = "$BASE_URL/api/Producto/Lista"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    val message = response.getString("mensaje")
                    if (message == "ok") {
                        val productsArray = response.getJSONArray("response")
                        for (i in 0 until productsArray.length()) {
                            val productObj = productsArray.getJSONObject(i)
                            val product = Product(
                                productObj.getInt("id_Producto"),
                                productObj.getString("nombre"),
                                productObj.getString("descripcion"),
                                productObj.getDouble("precio")
                            )
                            productList.add(product)
                        }
                        adapter.notifyDataSetChanged()
                    } else {
                        Log.e("ErrorAPI", "Error al obtener datos: Mensaje no es 'ok'")
                    }
                } catch (e: Exception) {
                    Log.e("ErrorAPI", "Error al analizar la respuesta JSON: ${e.message}", e)
                }
            },
            { error ->
                Log.e("ErrorAPI", "Error al obtener datos: ${error.message}", error)
            })

        queue.add(request)
    }
}

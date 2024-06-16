package com.app.dhpapp.repository

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.dhpapp.data.network.BaseApi
import com.app.dhpapp.model.User
import org.json.JSONObject

class RegisterRepository(private val context: Context) {

    private val TAG = "RegisterRepository"

    fun registerUser(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val url = "${BaseApi.BASE_URL}POST_Register.php"

        // Log para verificar datos que se están enviando
        Log.d(TAG, "URL: $url")
        Log.d(TAG, "Email: ${user.email}, Password: ${user.password}")

        val request = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                // Log para verificar la respuesta recibida
                Log.d(TAG, "Response: $response")

                try {
                    val jsonResponse = JSONObject(response)
                    val message = jsonResponse.optString("message")

                    if (message == "success") {
                        onSuccess.invoke()
                    } else {
                        val errorMessage = jsonResponse.optString("error", "Error desconocido")
                        onError.invoke(errorMessage)
                    }
                } catch (e: Exception) {
                    onError.invoke("Error al procesar la respuesta")
                    Log.e(TAG, "Error parsing response: $response", e)
                }
            },
            Response.ErrorListener { error ->
                // Log para verificar errores
                Log.e(TAG, "Error: ${error.message}", error)
                onError.invoke(error.message ?: "Error desconocido")
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["email"] = user.email
                params["password"] = user.password
                return params
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}
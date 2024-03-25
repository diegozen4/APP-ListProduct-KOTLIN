package com.app.dhpapp.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.dhpapp.BaseApi
import com.app.dhpapp.model.User
import org.json.JSONObject

class LoginRepository(private val context: Context) {
    fun loginUser(user: User, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val url = "${BaseApi.BASE_URL}/apiDhp/POST_Auth.php"

        val request = JsonObjectRequest(Request.Method.POST, url,
            JSONObject().apply {
                put("email", user.email)
                put("password", user.password)
            },
            { _ ->
                // Procesar la respuesta del servidor
                onSuccess.invoke()
            },
            { error ->
                // Manejar errores de la solicitud
                onError.invoke(error.message ?: "Error desconocido")
            })

        Volley.newRequestQueue(context).add(request)
    }
}

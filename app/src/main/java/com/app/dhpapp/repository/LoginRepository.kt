package com.app.dhpapp.repository

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.dhpapp.BaseApi
import com.app.dhpapp.model.User
import org.json.JSONObject

class LoginRepository(private val context: Context) {
    fun loginUser(user: User, onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        val url = "${BaseApi.BASE_URL}POST_Auth.php"

        val request = JsonObjectRequest(Request.Method.POST, url,
            JSONObject().apply {
                put("email", user.email)
                put("password", user.password)
            },
            { response ->
                val message = response.getString("message")
                if (message == "success") {
                    val userJson = response.getJSONObject("user")
                    val userEmail = userJson.getString("email")
                    val userRol = userJson.getString("rol")

                    val newUser = User(userEmail, "", userRol)
                    onSuccess.invoke(newUser)
                } else {
                    onError.invoke(message)
                }
            },
            { error ->
                onError.invoke(error.message ?: "Error desconocido")
            })

        Volley.newRequestQueue(context).add(request)
    }
}


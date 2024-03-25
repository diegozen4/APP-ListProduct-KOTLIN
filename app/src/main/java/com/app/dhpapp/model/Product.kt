package com.app.dhpapp.model

import java.io.Serializable

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double
) : Serializable


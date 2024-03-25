package com.app.dhpapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.dhpapp.model.Product

@Suppress("DEPRECATION")
class ProductDetailActivity : AppCompatActivity() {

    private lateinit var textProductName: TextView
    private lateinit var textProductDescription: TextView
    private lateinit var textProductPrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        textProductName = findViewById(R.id.textProductName)
        textProductDescription = findViewById(R.id.textProductDescription)
        textProductPrice = findViewById(R.id.textProductPrice)

        val product = intent.getSerializableExtra("product") as? Product

        // Verificar si se recibió el producto correctamente
        product?.let {
            textProductName.text = it.name
            textProductDescription.text = it.description
            textProductPrice.text = "S/. ${it.price}"
        }

        // Agregar lógica para el botón de comprar
        val buyButton: Button = findViewById(R.id.buyButton)
        buyButton.setOnClickListener {
            // Agregar aquí la lógica para comprar el producto
            // Por ejemplo, mostrar un mensaje de confirmación
            Toast.makeText(this, "Producto comprado", Toast.LENGTH_SHORT).show()
        }
    }
}

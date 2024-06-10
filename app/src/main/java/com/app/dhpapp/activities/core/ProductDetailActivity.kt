package com.app.dhpapp.activities.core

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.dhpapp.R
import com.app.dhpapp.model.Product
import java.util.Base64

@Suppress("DEPRECATION")
class ProductDetailActivity : AppCompatActivity() {

    private lateinit var textProductName: TextView
    private lateinit var textProductDescription: TextView
    private lateinit var textProductPrice: TextView
    private lateinit var imageViewProduct: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        textProductName = findViewById(R.id.textProductName)
        textProductDescription = findViewById(R.id.textProductDescription)
        textProductPrice = findViewById(R.id.textProductPrice) // Verifica que este ID exista en tu layout
        imageViewProduct = findViewById(R.id.imageViewProduct) // Agregar referencia al ImageView

        val product = intent.getSerializableExtra("product") as? Product

        // Verificar si se recibió el producto correctamente
        product?.let {
            textProductName.text = it.name
            textProductDescription.text = it.description
            textProductPrice.text = "S/. ${it.price} el kilo" // Aquí estamos utilizando el precio
            // Decodificar la imagen en base64 y establecerla en el ImageView
            val decodedImage = Base64.getDecoder().decode(it.image)
            imageViewProduct.setImageBitmap(BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size))

        }

        // Agregar lógica para el botón de comprar
        val buyButton: Button = findViewById(R.id.buyButton)
        buyButton.setOnClickListener {
            // Generar un código de compra aleatorio
            val randomCode = generateRandomCode()

            // Mostrar un AlertDialog personalizado con la información de la compra
            showPurchaseDialog(product?.name ?: "", product?.price.toString(), randomCode) // Verifica que aquí esté correcto
        }

    }

    private fun generateRandomCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..8).map { chars.random() }.joinToString("")
    }

    private fun showPurchaseDialog(productName: String, price: String, purchaseCode: String) {
        val message = "Producto comprado:\nFruta: $productName\nPrecio: S/. $price\n\nCódigo de compra: $purchaseCode"

        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Compra Exitosa!")
            .setMessage(message)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}

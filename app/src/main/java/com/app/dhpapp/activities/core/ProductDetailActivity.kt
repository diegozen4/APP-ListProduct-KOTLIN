package com.app.dhpapp.activities.core

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.dhpapp.R
import com.app.dhpapp.model.Product
import java.util.Base64

@Suppress("DEPRECATION")
class ProductDetailActivity : AppCompatActivity() {

    private lateinit var textProductName: EditText
    private lateinit var textProductDescription: EditText
    private lateinit var textProductPrice: EditText
    private lateinit var imageViewProduct: ImageView
    private lateinit var buyButton: Button
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var acceptButton: Button
    private lateinit var cancelButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        textProductName = findViewById(R.id.textProductName)
        textProductDescription = findViewById(R.id.textProductDescription)
        textProductPrice =
            findViewById(R.id.textProductPrice) // Verifica que este ID exista en tu layout
        imageViewProduct = findViewById(R.id.imageViewProduct) // Agregar referencia al ImageView
        buyButton = findViewById(R.id.buyButton)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.deleteButton)
        acceptButton = findViewById(R.id.acceptButton)
        cancelButton = findViewById(R.id.cancelButton)


        val product = intent.getSerializableExtra("product") as? Product
        val rol = intent.getStringExtra("USER_ROL")

        // Verificar si se recibió el producto correctamente
        product?.let {
            textProductName.setText(it.name)
            textProductDescription.setText(it.description)
            textProductPrice.setText("S/. ${it.price}") // Aquí estamos utilizando el precio
            // Decodificar la imagen en base64 y establecerla en el ImageView
            val decodedImage = Base64.getDecoder().decode(it.image)
            imageViewProduct.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    decodedImage,
                    0,
                    decodedImage.size
                )
            )

        }
        rol?.let {
            if (it == "1") {
                buyButton.visibility = Button.GONE
                editButton.visibility = Button.VISIBLE
                deleteButton.visibility = Button.VISIBLE
                acceptButton.visibility = Button.GONE
                cancelButton.visibility = Button.GONE
            } else {
                buyButton.visibility = Button.VISIBLE
                editButton.visibility = Button.GONE
                deleteButton.visibility = Button.GONE
                acceptButton.visibility = Button.GONE
                cancelButton.visibility = Button.GONE
            }
        }

        // Agregar lógica para el botón de comprar
        buyButton.setOnClickListener {
            // Generar un código de compra aleatorio
            val randomCode = generateRandomCode()

            // Mostrar un AlertDialog personalizado con la información de la compra
            showPurchaseDialog(
                product?.name ?: "",
                product?.price.toString(),
                randomCode
            ) // Verifica que aquí esté correcto
        }
        editButton.setOnClickListener {
            buyButton.visibility = Button.GONE
            editButton.visibility = Button.GONE
            deleteButton.visibility = Button.GONE
            acceptButton.visibility = Button.VISIBLE
            cancelButton.visibility = Button.VISIBLE
        }
        deleteButton.setOnClickListener {

        }
        acceptButton.setOnClickListener {

        }
        cancelButton.setOnClickListener {
            buyButton.visibility = Button.GONE
            editButton.visibility = Button.VISIBLE
            deleteButton.visibility = Button.VISIBLE
            acceptButton.visibility = Button.GONE
            cancelButton.visibility = Button.GONE
        }

    }

    private fun generateRandomCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..8).map { chars.random() }.joinToString("")
    }

    private fun showPurchaseDialog(productName: String, price: String, purchaseCode: String) {
        val message =
            "Producto comprado:\nProducto: $productName\nPrecio: S/. $price\n\nCódigo de compra: $purchaseCode"

        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Compra Exitosa!")
            .setMessage(message)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}

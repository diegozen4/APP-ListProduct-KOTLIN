package com.app.dhpapp.activities.core

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.dhpapp.R
import com.app.dhpapp.model.Product
import com.app.dhpapp.viewmodel.ProductViewModel
import java.io.ByteArrayOutputStream
import java.util.Base64

@Suppress("DEPRECATION")
class ProductDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: ProductViewModel

    private lateinit var textProductName: EditText
    private lateinit var textProductDescription: EditText
    private lateinit var textProductPrice: EditText
    private lateinit var imageViewProduct: ImageView
    private lateinit var buyButton: Button
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var uploadImage: ImageButton
    private lateinit var clearImage: ImageView
    private var imageUri: Uri? = null
    private val REQUEST_IMAGE_PICK = 1001

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
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        uploadImage = findViewById(R.id.imageButtonUploadImage)
        clearImage= findViewById(R.id.clearImage)


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
                saveButton.visibility = Button.GONE
                cancelButton.visibility = Button.GONE
            } else {
                buyButton.visibility = Button.VISIBLE
                editButton.visibility = Button.GONE
                deleteButton.visibility = Button.GONE
                saveButton.visibility = Button.GONE
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
            textProductName.isEnabled = true
            textProductDescription.isEnabled = true
            textProductPrice.isEnabled = true
            buyButton.visibility = Button.GONE
            editButton.visibility = Button.GONE
            deleteButton.visibility = Button.GONE
            saveButton.visibility = Button.VISIBLE
            cancelButton.visibility = Button.VISIBLE
            uploadImage.visibility = View.VISIBLE
            clearImage.visibility = View.VISIBLE
        }
        deleteButton.setOnClickListener {

        }
        saveButton.setOnClickListener {
            textProductName.isEnabled = false
            textProductDescription.isEnabled = false
            textProductPrice.isEnabled = false
            buyButton.visibility = Button.GONE
            editButton.visibility = Button.VISIBLE
            deleteButton.visibility = Button.VISIBLE
            saveButton.visibility = Button.GONE
            cancelButton.visibility = Button.GONE
            uploadImage.visibility = View.GONE
            clearImage.visibility = View.GONE

            // Guardar cambios aquí
            val id = product!!.id
            val name = textProductName.text.toString()
            val description = textProductDescription.text.toString()
            val price = textProductPrice.text.toString()
            val image = imageUri?.let { uri -> convertImageToBase64(uri) } ?: ""

            val product = Product(id, name, description, price, image)

            viewModel.updateProduct(product,
                onSuccess = {
                    Toast.makeText(this, "Producto agregado correctamente", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                },
                onError = { error ->
                    Toast.makeText(this, "Error al agregar el producto: $error", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        }
        cancelButton.setOnClickListener {
            textProductName.isEnabled = false
            textProductDescription.isEnabled = false
            textProductPrice.isEnabled = false
            buyButton.visibility = Button.GONE
            editButton.visibility = Button.VISIBLE
            deleteButton.visibility = Button.VISIBLE
            saveButton.visibility = Button.GONE
            cancelButton.visibility = Button.GONE
            uploadImage.visibility = View.GONE
            clearImage.visibility = View.GONE
        }

        uploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
        clearImage.setOnClickListener {
            imageViewProduct.setImageBitmap(null)
            imageUri = null
        }
    }

    private fun convertImageToBase64(imageUri: Uri): String {
        val inputStream = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val compressedBitmap = compressImage(bitmap)
        val byteArrayOutputStream = ByteArrayOutputStream()
        compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
    }

    private fun compressImage(bitmap: Bitmap): Bitmap {
        var quality = 100
        var streamLength: Int
        val byteArrayOutputStream = ByteArrayOutputStream()

        do {
            byteArrayOutputStream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
            streamLength = byteArrayOutputStream.size()
            quality -= 5
        } while (streamLength >= 50 * 1024 && quality > 0)

        val byteArray = byteArrayOutputStream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
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

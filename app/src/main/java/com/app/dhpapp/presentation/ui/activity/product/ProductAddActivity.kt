package com.app.dhpapp.presentation.ui.activity.product

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.dhpapp.R
import com.app.dhpapp.domain.model.Product
import com.app.dhpapp.presentation.viewmodel.ProductViewModel
import java.io.ByteArrayOutputStream

class ProductAddActivity : AppCompatActivity() {
    private lateinit var viewModel: ProductViewModel
    private lateinit var imageViewProductAdd: ImageView
    private lateinit var imageButtonUploadImage: ImageButton
    private var imageUri: Uri? = null
    private val REQUEST_IMAGE_PICK = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_add)

        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        imageViewProductAdd = findViewById(R.id.imageViewProductAdd)
        imageButtonUploadImage = findViewById(R.id.imageButtonUploadImage)
        val clearImage: ImageView = findViewById(R.id.clearImage)
        val saveButton: Button = findViewById(R.id.saveButton)
        val editTextProductName: EditText = findViewById(R.id.editTextProductName)
        val editTextProductDescription: EditText = findViewById(R.id.editTextProductDescription)
        val editTextProductPrice: EditText = findViewById(R.id.editTextProductPrice)

        imageButtonUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        clearImage.setOnClickListener {
            imageViewProductAdd.setImageBitmap(null)
            imageUri = null
        }

        saveButton.setOnClickListener {
            val name = editTextProductName.text.toString()
            val description = editTextProductDescription.text.toString()
            val price = editTextProductPrice.text.toString()
            val image = imageUri?.let { uri -> convertImageToBase64(uri) } ?: ""

            val product = Product(0, name, description, price, image)

            viewModel.addProduct(product,
                onSuccess = {
                    Toast.makeText(this, "Producto agregado correctamente", Toast.LENGTH_SHORT)
                        .show()
                    finish() // Finaliza la actividad después de agregar el producto
                },
                onError = { error ->
                    Toast.makeText(this, "Error al agregar el producto: $error", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            imageViewProductAdd.setImageURI(imageUri)
        }
    }

    private fun convertImageToBase64(imageUri: Uri): String {
        val inputStream = contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val compressedBitmap = compressImage(bitmap)
        val byteArrayOutputStream = ByteArrayOutputStream()
        compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
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

}
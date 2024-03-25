package com.app.dhpapp.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.dhpapp.R
import com.app.dhpapp.model.Product
import java.util.*

class ProductAdapter(private var productList: List<Product>, private val listener: OnProductClickListener) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    interface OnProductClickListener {
        fun onProductClick(product: Product)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textNombre)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textDescripcion)
        val priceTextView: TextView = itemView.findViewById(R.id.textPrecio)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewProducto)
    }

    fun setData(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.nameTextView.text = product.name
        holder.descriptionTextView.text = product.description
        holder.priceTextView.text = "S/. ${product.price}"

        // Decodificar la imagen en base64 y establecerla en el ImageView
        val decodedImage = Base64.getDecoder().decode(product.image)
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.size))

        holder.itemView.setOnClickListener {
            listener.onProductClick(product)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}

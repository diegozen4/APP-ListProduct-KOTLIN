package com.app.dhpapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textNombre)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textDescripcion)
        val priceTextView: TextView = itemView.findViewById(R.id.textPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.nameTextView.text = product.nombre
        holder.descriptionTextView.text = product.descripcion
        holder.priceTextView.text = "S/. ${product.precio}"
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}
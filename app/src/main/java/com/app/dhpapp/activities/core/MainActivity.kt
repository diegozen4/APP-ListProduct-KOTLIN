package com.app.dhpapp.activities.core

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.dhpapp.R
import com.app.dhpapp.adapter.ProductAdapter
import com.app.dhpapp.model.Product
import com.app.dhpapp.viewmodel.ProductViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.Serializable

class MainActivity : AppCompatActivity(), ProductAdapter.OnProductClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var addProduct: FloatingActionButton

    private lateinit var viewModel: ProductViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userRol = intent.getStringExtra("USER_ROL")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // Cambio aquí
        adapter = ProductAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        addProduct = findViewById(R.id.addProduct)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getProducts()
        }

        if (userRol == "1") {
            addProduct.visibility = View.VISIBLE
        } else {
            addProduct.visibility = View.GONE
        }

        addProduct.setOnClickListener {
            // Redirecciona a la pantalla ProductAdd
            val intent = Intent(this@MainActivity, ProductAddActivity::class.java)
            startActivity(intent)
        }

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        viewModel.productList.observe(this) { products ->
            adapter.setData(products)
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onProductClick(product: Product) {
        val intent = Intent(this, ProductDetailActivity::class.java).apply {
            putExtra("product", product as Serializable)
        }
        startActivity(intent)
    }
}

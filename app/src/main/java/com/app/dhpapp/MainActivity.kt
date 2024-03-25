package com.app.dhpapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.dhpapp.adapter.ProductAdapter
import com.app.dhpapp.model.Product
import com.app.dhpapp.viewmodel.ProductViewModel
import java.io.Serializable

class MainActivity : AppCompatActivity(), ProductAdapter.OnProductClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getProducts()
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

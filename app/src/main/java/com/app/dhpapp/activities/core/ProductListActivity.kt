package com.app.dhpapp.activities.core

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.dhpapp.R
import com.app.dhpapp.activities.auth.LoginActivity
import com.app.dhpapp.adapter.ProductAdapter
import com.app.dhpapp.model.Product
import com.app.dhpapp.viewmodel.ProductViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.Serializable

class ProductListActivity : AppCompatActivity(), ProductAdapter.OnProductClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var addProduct: FloatingActionButton
    private lateinit var exit: ImageView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var searchEditText: EditText
    private lateinit var viewModel: ProductViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        val userRol = intent.getStringExtra("USER_ROL")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = ProductAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        addProduct = findViewById(R.id.addProduct)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        searchView = findViewById(R.id.searchView)
        exit = findViewById(R.id.exit)

        exit.setOnClickListener {
            val intent = Intent(this@ProductListActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

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
            val intent = Intent(this@ProductListActivity, ProductAddActivity::class.java)
            startActivity(intent)
        }

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        viewModel.productList.observe(this) { products ->
            adapter.setData(products)
            swipeRefreshLayout.isRefreshing = false
        }

        // Set up the search view
        searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(
            ContextCompat.getColor(
                this, R.color.black
            )
        ) // Cambiar el color del texto
        searchEditText.setHintTextColor(
            ContextCompat.getColor(
                this, R.color.gray
            )
        ) // Cambiar el color del hint

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })
    }

    private fun filter(text: String) {
        val filteredList = viewModel.productList.value?.filter {
            it.name.contains(text, true) || it.description.contains(text, true)
        }
        filteredList?.let { adapter.setData(it) }
    }

    override fun onProductClick(product: Product) {
        val intent = Intent(this, ProductDetailActivity::class.java).apply {
            putExtra("product", product as Serializable)
            putExtra("USER_ROL", intent.getStringExtra("USER_ROL"))
        }
        startActivity(intent)
    }
}

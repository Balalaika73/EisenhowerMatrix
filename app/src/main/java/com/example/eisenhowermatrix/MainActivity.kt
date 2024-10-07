package com.example.eisenhowermatrix

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.eisenhowermatrix.RecyclerViewAdapter.*
import com.example.eisenhowermatrix.data.AppDatabase
import com.example.eisenhowermatrix.data.categories.CategoryDAO

class MainActivity : AppCompatActivity() {

    private lateinit var btnAddCtg: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var customRecyclerAdapter: CustomRecyclerAdapter


    private lateinit var categoryDao: CategoryDAO

    private val categoryViewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory(categoryDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация базы данных
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).build()
        categoryDao = db.categoryDao()

        btnAddCtg = findViewById<ImageButton>(R.id.btnAddCtg)
        recyclerView = findViewById<RecyclerView>(R.id.lwCtg)

        customRecyclerAdapter = CustomRecyclerAdapter(mutableListOf(), categoryDao,
            object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    // Обработка нажатия на элемент
                    val clickedCategory = customRecyclerAdapter.getCategoryAt(position)
                    // Например, можно отобразить Toast или перейти на другой экран
                    val intent = Intent(this@MainActivity, MatrixActivity::class.java)
                    intent.putExtra("CATEGORY_ID", clickedCategory.id)
                    intent.putExtra("CATEGORY_NAME", clickedCategory.name)

                    // Запускаем новое Activity
                    startActivity(intent)
                }
            })

        // Установка адаптера и менеджера макета для RecyclerView
        recyclerView.adapter = customRecyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val itemTouchHelper = ItemTouchHelper(SimpleItemTouchHelperCallback(customRecyclerAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)


        categoryViewModel.allCategories.observe(this, Observer { categories ->
            categories?.let {
                customRecyclerAdapter.setCategories(it)
            }
        })

        btnAddCtg.setOnClickListener {
            customRecyclerAdapter.showNewCategory()
        }
    }
}

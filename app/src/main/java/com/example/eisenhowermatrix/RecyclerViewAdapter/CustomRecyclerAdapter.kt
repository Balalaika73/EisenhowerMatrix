package com.example.eisenhowermatrix.RecyclerViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eisenhowermatrix.R
import com.example.eisenhowermatrix.data.categories.CategoryDAO
import com.example.eisenhowermatrix.data.categories.CategoryEnt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CustomRecyclerAdapter(private var categories: MutableList<CategoryEnt>,
                            private val categoryDao: CategoryDAO,
                            private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter {

    // Флаг для отображения новой карточки
    private var isNewCategoryVisible: Boolean = false

    override fun getItemViewType(position: Int): Int {
        return if (isNewCategoryVisible && position == categories.size) {
            // Последняя карточка будет для добавления новой категории
            VIEW_TYPE_NEW_CATEGORY
        } else {
            VIEW_TYPE_CATEGORY
        }
    }

    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_NEW_CATEGORY = 1
    }

    //recyclerview cards view
    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTextView: TextView = itemView.findViewById(R.id.nameCtg)
        val editButton: ImageButton = itemView.findViewById(R.id.btnEdtCtg)
    }

    //recyclerview adding card view
    class NewCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val saveButton: ImageButton? = itemView.findViewById(R.id.btnSaveCtg)
        val cancelButton: ImageButton? = itemView.findViewById(R.id.cnclCtg)
        val editText: EditText? = itemView.findViewById(R.id.edtCtg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_NEW_CATEGORY -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.new_category_item, parent, false)
                NewCategoryViewHolder(itemView)
            }
            VIEW_TYPE_CATEGORY -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.category_item, parent, false)
                CategoryViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            val category = categories[position]
            holder.categoryNameTextView.text = category.name

            holder.editButton.setOnClickListener {
                //deleteCategory(category)
            }

            holder.itemView.setOnClickListener {
                itemClickListener.onItemClick(position)
            }
        }
        else if (holder is NewCategoryViewHolder) {
            // Обработка новой карточки добавления категории
            holder.saveButton?.setOnClickListener {
                val newCategoryName = holder.editText?.text.toString()
                if (newCategoryName.isNotBlank()) {
                    val newCategory = CategoryEnt(name = newCategoryName, position = categories.size+1)
                    addCategory(newCategory) // Добавление новой категории
                }
            }

            holder.cancelButton?.setOnClickListener {
                hideNewCategory() // Скрытие карточки при отмене
            }
        }
    }

    fun setCategories(newCategories: List<CategoryEnt>) {
        categories.clear()
        categories.addAll(newCategories)
        notifyDataSetChanged() // Обновление данных в адаптере
    }

    override fun getItemCount(): Int {
        return if (isNewCategoryVisible) {
            categories.size + 1 // +1 для карточки добавления категории
        } else {
            categories.size
        }
    }

    private fun addCategory(newCategory: CategoryEnt) {
        CoroutineScope(Dispatchers.IO).launch {
            categoryDao.insertCategory(newCategory)
            // Возвращаемся в основной поток для обновления списка
            CoroutineScope(Dispatchers.Main).launch {
                categories.add(newCategory)
                setCategories(categories)
                hideNewCategory()
            }
        }
    }

    fun showNewCategory() {
        isNewCategoryVisible = true
        notifyItemInserted(categories.size)
    }

    fun hideNewCategory() {
        if (isNewCategoryVisible) {
            isNewCategoryVisible = false
            notifyItemRemoved(categories.size) // Удаляем последний элемент, если он отображается
        }
    }

    fun getCategoryAt(position: Int): CategoryEnt {
        return categories[position]
    }


    //DRAG & DROP
    override fun onItemDismiss(position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val categoryToDelete = categories[position]
            categoryDao.deleteCategory(categoryToDelete)
            // Возвращаемся в основной поток для обновления списка
            categories.remove(categoryToDelete)
            isNewCategoryVisible = false
            CoroutineScope(Dispatchers.Main).launch {
                setCategories(categories)
            }
        }

        // Удаляем категорию из списка и уведомляем адаптер
        /*categories.removeAt(position)
        notifyItemRemoved(position)*/
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int ){
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(categories, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(categories, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)

        // После перемещения вызываем функцию обновления порядка
        updateCategoryOrder()
    }

    private fun updateCategoryOrder() {
        CoroutineScope(Dispatchers.IO).launch {
            // Обновляем порядок категорий в базе данных
            categories.forEachIndexed { index, category ->
                category.position = index // Предполагаем, что у CategoryEnt есть поле position
                categoryDao.updateCategory(category) // Обновление в базе данных
            }

            // Возвращаемся в основной поток для обновления UI
            CoroutineScope(Dispatchers.Main).launch {
                setCategories(categories)
            }
        }
    }

}
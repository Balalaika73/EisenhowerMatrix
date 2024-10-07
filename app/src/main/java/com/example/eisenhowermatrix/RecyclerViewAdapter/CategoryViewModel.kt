package com.example.eisenhowermatrix.RecyclerViewAdapter

import androidx.lifecycle.*
import com.example.eisenhowermatrix.data.categories.CategoryDAO
import com.example.eisenhowermatrix.data.categories.CategoryEnt

class CategoryViewModel(private val categoryDAO: CategoryDAO): ViewModel() {
    val allCategories: LiveData<List<CategoryEnt>> = categoryDAO.getAllCategories().asLiveData()
}

class CategoryViewModelFactory(private val categoryDAO: CategoryDAO) : ViewModelProvider.Factory {
    // Убираем nullable тип ViewModel
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(categoryDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
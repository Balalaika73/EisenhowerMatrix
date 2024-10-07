package com.example.eisenhowermatrix.data.categories

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEnt)

    @Delete
    suspend fun deleteCategory(category: CategoryEnt)

    @Query("DELETE FROM categories WHERE id = :ctgId")
    suspend fun deleteCategoryById(ctgId: Int)

    @Update
    suspend fun updateCategory(category: CategoryEnt)


    @Query("SELECT * FROM categories ORDER BY position DESC")
    fun getAllCategories(): Flow<List<CategoryEnt>> // Использование Flow для наблюдения за изменениями

    @Query("SELECT * FROM categories WHERE id = :categoryId LIMIT 1")
    suspend fun getCategoryById(categoryId: Int): CategoryEnt?
}
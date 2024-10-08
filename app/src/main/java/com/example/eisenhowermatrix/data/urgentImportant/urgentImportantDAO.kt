package com.example.eisenhowermatrix.data.urgentImportant

import androidx.room.*
import com.example.eisenhowermatrix.data.categories.CategoryEnt
import kotlinx.coroutines.flow.Flow

@Dao
interface urgentImportantDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUrgentImportant(urgentImportant: UrgentImportantEnt)

    @Delete
    suspend fun deleteUrgentImportant(urgentImportant: UrgentImportantEnt)

    @Query("DELETE FROM UrgentImportant WHERE urgImpId = :urgImpId")
    suspend fun deleteUrgentImportantById(urgImpId: Int)

    @Update
    suspend fun updateUrgentImportant(urgentImportant: UrgentImportantEnt)


    @Query("SELECT * FROM UrgentImportant ORDER BY position DESC")
    fun getAllUrgentImportant(): Flow<List<UrgentImportantEnt>> // Использование Flow для наблюдения за изменениями

    @Query("SELECT * FROM UrgentImportant WHERE urgImpId = :urgentImportantId LIMIT 1")
    suspend fun getUrgentImportantById(urgentImportantId: Int): UrgentImportantEnt?
}
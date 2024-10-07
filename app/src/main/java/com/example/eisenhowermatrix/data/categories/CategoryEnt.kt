package com.example.eisenhowermatrix.data.categories

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEnt (
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Автоматическая генерация ID
    val name: String,
    var position: Int
)
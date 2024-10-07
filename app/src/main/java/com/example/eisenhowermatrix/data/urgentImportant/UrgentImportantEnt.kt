package com.example.eisenhowermatrix.data.urgentImportant

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="UrgentImportant")
data class UrgentImportantEnt (
    @PrimaryKey(autoGenerate = true) val urgImpId: Int = 0,
    val name : String,
    val position: Int
)
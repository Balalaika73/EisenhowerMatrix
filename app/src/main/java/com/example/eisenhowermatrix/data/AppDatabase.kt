package com.example.eisenhowermatrix.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eisenhowermatrix.data.categories.CategoryDAO
import com.example.eisenhowermatrix.data.categories.CategoryEnt

@Database(entities = [CategoryEnt::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun categoryDao(): CategoryDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Имя файла базы данных
                )
                    .fallbackToDestructiveMigration() // Удаляет и пересоздает базу данных при изменении версии
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
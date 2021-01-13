package com.example.marvelworld.favorite.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.marvelworld.favorite.dao.FavoriteDao
import com.example.marvelworld.favorite.models.Favorite
import com.example.marvelworld.util.Converters

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "favorites"
                ).build()
            }

            return INSTANCE!!
        }
    }
}
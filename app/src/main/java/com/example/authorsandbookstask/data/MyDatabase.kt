package com.example.authorsandbookstask.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Author::class, Book::class], version = 3, exportSchema = true)
abstract class MyDatabase: RoomDatabase() {
    abstract fun myDao():MyDao

    companion object {
        @Volatile // writes to this field are immediately visible to other threads
        private var INSTANCE: MyDatabase? = null
        // Singleton prevents multiple instances of database opening at the same time.
        // It is expensive to have more than one instance of the database.
        fun getDatabase(context: Context): MyDatabase {
            val tempInstance = INSTANCE
            tempInstance?.let {
                return tempInstance
            }
            synchronized(this) { // Если INATSANCE!= null, создаем первую INSTANCE (базу данных)
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "user_database"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
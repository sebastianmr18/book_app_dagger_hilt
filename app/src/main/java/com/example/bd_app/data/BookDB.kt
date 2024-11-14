package com.example.bd_app.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bd_app.model.Book
import com.example.bd_app.utils.Constants.NAME_BD

@Database(entities = [Book::class], version = 1)
abstract class BookDB : RoomDatabase() {
    abstract fun bookDao(): BookDao
    companion object{
        fun getDatabase(context: Context): BookDB {
            return Room.databaseBuilder(
                context.applicationContext,
                BookDB::class.java,
                NAME_BD
            ).build()
        }
    }
}
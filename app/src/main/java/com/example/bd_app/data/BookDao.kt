package com.example.bd_app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bd_app.model.Book

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBook(book: Book)

    @Query("SELECT * FROM Book")
    suspend fun getListBook(): MutableList<Book>

    @Delete
    suspend fun deleteBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)
}
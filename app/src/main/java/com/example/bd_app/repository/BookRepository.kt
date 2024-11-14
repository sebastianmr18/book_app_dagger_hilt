package com.example.bd_app.repository

import android.content.Context
import com.example.bd_app.data.BookDB
import com.example.bd_app.data.BookDao
import com.example.bd_app.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookRepository(val context: Context) {
    private var bookDao:BookDao = BookDB.getDatabase(context).bookDao()
    suspend fun saveBook(book: Book){
        withContext(Dispatchers.IO){
            bookDao.saveBook(book)
        }
    }

    suspend fun getListBook():MutableList<Book>{
        return withContext(Dispatchers.IO){
            bookDao.getListBook()
        }
    }

    suspend fun deleteBook(book: Book){
        withContext(Dispatchers.IO){
            bookDao.deleteBook(book)
        }
    }

    suspend fun updateBook(book: Book){
        withContext(Dispatchers.IO){
            bookDao.updateBook(book)
        }
    }
}
package com.example.bd_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bd_app.model.Book
import com.example.bd_app.repository.BookRepository
import kotlinx.coroutines.launch

class BookViewModel(application: Application): AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val bookRepository = BookRepository(context)

    private val _listBook = MutableLiveData<MutableList<Book>>()
    val listBook: LiveData<MutableList<Book>> get() = _listBook

    private val _progressState = MutableLiveData(false)
    val progressState: LiveData<Boolean> = _progressState

    fun saveBook(book: Book) {
        viewModelScope.launch {

            _progressState.value = true
            try {
                bookRepository.saveBook(book)
                _progressState.value = false
            } catch (e: Exception) {
                _progressState.value = false
            }
        }
    }

    fun getListBook() {
        viewModelScope.launch {
            _progressState.value = true
            try {
                _listBook.value = bookRepository.getListBook()
                _progressState.value = false
            } catch (e: Exception) {
                _progressState.value = false
            }
        }
    }

    fun deleteBook(book: Book){
        viewModelScope.launch {
            _progressState.value = true
            try {
                bookRepository.deleteBook(book)
                _progressState.value = false
            } catch (e: Exception) {
                _progressState.value = false
            }
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            _progressState.value = true
            try {
                bookRepository.updateBook(book)
                _progressState.value = false
            } catch (e: Exception) {
                _progressState.value = false
            }
        }
    }
}
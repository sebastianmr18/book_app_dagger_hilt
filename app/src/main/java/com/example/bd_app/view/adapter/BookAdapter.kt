package com.example.bd_app.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.bd_app.databinding.ItemBookBinding
import com.example.bd_app.model.Book
import com.example.bd_app.view.viewholder.BookViewHolder

class BookAdapter(private val listBook:MutableList<Book>,
                  private val navController: NavController): RecyclerView.Adapter<BookViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return BookViewHolder(binding, navController)
    }

    override fun getItemCount(): Int {
        return listBook.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = listBook[position]
        holder.setItemBook(book)
    }
}
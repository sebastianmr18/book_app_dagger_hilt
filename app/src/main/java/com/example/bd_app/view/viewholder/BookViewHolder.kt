package com.example.bd_app.view.viewholder

import android.os.Bundle
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.bd_app.R
import com.example.bd_app.databinding.ItemBookBinding
import com.example.bd_app.model.Book

class BookViewHolder(binding: ItemBookBinding, navController: NavController) :
    RecyclerView.ViewHolder(binding.root){
    val bindingItem = binding
    val navController = navController
    fun setItemBook(book: Book) {
        bindingItem.textViewName.text = "Título: ${book.name}"
        bindingItem.textViewPrice.text = "Precio: $${book.price}"
        bindingItem.textViewGenre.text = "Genero: ${book.genre}"
        bindingItem.textViewAuthor.text = "Autor ${book.author}"

        bindingItem.cardViewBook.setOnClickListener{
            val bundle = Bundle()
            bundle.putSerializable("clave", book)
            navController.navigate(R.id.action_homeBookFragment_to_bookDetailsFragment, bundle)
        }
    }
}
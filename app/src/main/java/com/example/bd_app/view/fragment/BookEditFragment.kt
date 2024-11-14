package com.example.bd_app.view.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bd_app.R
import com.example.bd_app.databinding.FragmentBookDetailsBinding
import com.example.bd_app.databinding.FragmentBookEditBinding
import com.example.bd_app.model.Book
import com.example.bd_app.viewmodel.BookViewModel


// TODO: hacer logica y vista de detalles de libro 

class BookEditFragment : Fragment() {
    private lateinit var binding: FragmentBookEditBinding
    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var receivedBook: Book

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookEditBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBook()
        controlator()
    }

    private fun controlator() {
        binding.buttonEdit.setOnClickListener{
            updateBook()
        }
    }

    private fun dataBook() {
        val receivedBundle = arguments
        receivedBook = receivedBundle?.getSerializable("dataBook") as Book
        binding.editTextBookName.setText(receivedBook.name)
        binding.editTextBookPrice.setText(receivedBook.price.toString())
        binding.editTextBookGenre.setText(receivedBook.genre)
        binding.editTextBookAuthor.setText(receivedBook.author)
    }

    private fun updateBook() {
        val name = binding.editTextBookName.text.toString()
        val price = binding.editTextBookPrice.text.toString().toInt()
        val genre = binding.editTextBookGenre.text.toString()
        val author = binding.editTextBookAuthor.text.toString()
        val book = Book(id = receivedBook.id, name=name, price=price, genre = genre, author = author)
        bookViewModel.updateBook(book)
        findNavController().navigate(R.id.action_bookEditFragment_to_homeBookFragment)
    }
    
}
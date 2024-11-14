package com.example.bd_app.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.core.widget.addTextChangedListener
import com.example.bd_app.R
import com.example.bd_app.databinding.FragmentAddBookBinding
import com.example.bd_app.model.Book
import com.example.bd_app.viewmodel.BookViewModel

class AddBookFragment : Fragment() {
    private lateinit var binding: FragmentAddBookBinding
    private val bookViewModel: BookViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBookBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controlator()
    }

    private fun controlator() {
        validateData()
        binding.buttonSaveBook.setOnClickListener {
            saveBook()
        }
    }

    private fun saveBook(){
        val name = binding.editTextBookName.text.toString()
        val price = binding.editTextBookPrice.text.toString().toInt()
        val genre = binding.editTextBookGenre.text.toString()
        val author = binding.editTextBookAuthor.text.toString()
        val book = Book(name = name, price = price, genre = genre, author = author)
        bookViewModel.saveBook(book)
        Log.d("test", book.toString())
        Toast.makeText(context, "Libro guardado", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    private fun validateData() {
        val listEditText = listOf(binding.editTextBookName, binding.editTextBookPrice,
        binding.editTextBookGenre, binding.editTextBookAuthor)

        for (editText in listEditText) {
            editText.addTextChangedListener {
                val isListFull = listEditText.all{
                    it.text?.isNotEmpty() == true// si toda la lista no está vacía
                }
                binding.buttonSaveBook.isEnabled = isListFull
            }
        }
    }

}
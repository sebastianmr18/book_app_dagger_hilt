package com.example.bd_app.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bd_app.R
import com.example.bd_app.databinding.FragmentBookDetailsBinding
import com.example.bd_app.model.Book
import com.example.bd_app.viewmodel.BookViewModel

class BookDetailsFragment : Fragment() {
    private lateinit var binding: FragmentBookDetailsBinding
    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var receivedBook: Book

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBook()
        controlator()
    }

    private fun controlator() {
        binding.buttonDelete.setOnClickListener{
            deleteBook()
        }

        binding.buttonEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("dataBook", receivedBook)
            findNavController().navigate(R.id.action_bookDetailsFragment_to_bookEditFRagment, bundle)
        }
    }

    private fun dataBook() {
        val receivedBundle = arguments
        receivedBook = receivedBundle?.getSerializable("clave") as Book
        binding.textViewBookNamePH.text = "${receivedBook.name}"
        binding.textViewBookPricePH.text = "$ ${receivedBook.price}"
        binding.textViewBookGenrePH.text = "${receivedBook.genre}"
        binding.textViewBookAuthorPH.text = "${receivedBook.author}"
    }

    private fun deleteBook(){
        bookViewModel.deleteBook(receivedBook)
        bookViewModel.getListBook()
        findNavController().popBackStack()
    }
}
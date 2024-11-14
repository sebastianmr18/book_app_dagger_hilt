package com.example.bd_app.view.fragment

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bd_app.R
import com.example.bd_app.databinding.FragmentHomeBookBinding
import com.example.bd_app.view.adapter.BookAdapter
import com.example.bd_app.viewmodel.BookViewModel

class HomeBookFragment : Fragment() {
    private lateinit var binding: FragmentHomeBookBinding
    private lateinit var mediaPlayer: MediaPlayer
    private val bookViewModel: BookViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBookBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controlator()
        observerViewModel()
        reproducirAudio()
    }

    override fun onResume() {
        super.onResume()
        // Reproducir el audio cuando el fragmento esté visible
        mediaPlayer.start()
    }

    override fun onPause() {
        super.onPause()
        // Pausar el audio cuando el fragmento no esté visible
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Liberar recursos del MediaPlayer al destruir el fragmento
        mediaPlayer.release()
    }

    private fun reproducirAudio() {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.audio)

        mediaPlayer.start()

        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }
    }

    private fun controlator() {
        binding.floatingButtonAddBook.setOnClickListener{
            findNavController().navigate(R.id.action_homeBookFragment_to_addBookFragment)
        }
    }

    private fun observerViewModel() {
        observerListBook()
        observerProgress()
    }

    private fun observerListBook(){
        bookViewModel.getListBook()
        bookViewModel.listBook.observe(viewLifecycleOwner){listBook ->
            val recycler = binding.recyclerView
            val layoutManager = LinearLayoutManager(context)
            recycler.layoutManager = layoutManager
            val adapter = BookAdapter(listBook, findNavController())
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun observerProgress() {
        bookViewModel.progressState.observe(viewLifecycleOwner){status ->
            binding.progress.isVisible = status
        }
    }


}
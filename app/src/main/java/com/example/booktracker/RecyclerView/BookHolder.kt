package com.example.booktracker.RecyclerView

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracker.API.Book
import com.example.booktracker.API.VolumeInfo
import com.example.booktracker.R
import com.example.booktracker.databinding.BookItemBinding

class BookHolder(private val binding: BookItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(volumeInfo: VolumeInfo) {
        binding.titleBook.text = volumeInfo.volumeInfo.title
        binding.authorBook.text = volumeInfo.volumeInfo.authors.first()
        binding.pagesBook.text = "Страниц: ${volumeInfo.volumeInfo.pageCount}"
    }
}

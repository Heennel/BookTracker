package com.example.booktracker.RecyclerView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.booktracker.API.Book
import com.example.booktracker.API.BookItem
import com.example.booktracker.R
import com.example.booktracker.databinding.BookItemBinding

class BookHolder(private val binding: BookItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(book: Book) {
        binding.titleBook.text = book.title
        binding.authorBook.text = book.authors?.first()?:"автор неизвестен"
        binding.pagesBook.text = "Страниц: ${book.pageCount?:"неизвестно"}"

        Glide.with(binding.root)
            .load(book.imageLinks?.maybeNormImage)
            .placeholder(R.drawable.ic_home_black_24dp)
            .into(binding.imageBook)
    }
}

package com.example.booktracker.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booktracker.API.Book
import com.example.booktracker.API.BookItem
import com.example.booktracker.databinding.BookItemBinding

class BookAdapter: RecyclerView.Adapter<BookHolder>() {

    var bookList = mutableListOf<Book>()

    fun refreshList(newBooks: List<Book>) {
        bookList.clear()
        bookList.addAll(newBooks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val binding = BookItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookHolder(binding)
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}
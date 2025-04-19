package com.example.booktracker.RecyclerView

import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.booktracker.API.Book
import com.example.booktracker.API.BookItem
import com.example.booktracker.R
import com.example.booktracker.databinding.BookItemBinding

class BookHolder(private val binding: BookItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(book: Book) {
        binding.titleBook.text = book.title
        binding.authorBook.text = book.authors?.joinToString()?:"Fвтор неизвестен"
        val pagesText = "Страниц: ${book.pageCount?:"неизвестно"}"
        binding.pagesBook.setText(pagesText)

        val radiusInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            4f,
            binding.root.resources.displayMetrics
        ).toInt()

        Glide.with(binding.root)
            .load(book.imageLinks?.maybeNormImage?.replace("http://", "https://"))
            .placeholder(R.drawable.img_placeholder)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusInPx)))
            .into(binding.imageBook)
    }
}

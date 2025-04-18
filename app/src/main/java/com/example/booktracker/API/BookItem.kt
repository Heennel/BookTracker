package com.example.booktracker.API

import com.google.gson.annotations.SerializedName

data class BookItem(
    @SerializedName("volumeInfo") val bookItem: Book
)

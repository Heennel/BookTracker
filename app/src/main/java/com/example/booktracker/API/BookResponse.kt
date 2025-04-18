package com.example.booktracker.APIЫ

import com.example.booktracker.API.BookItem
import com.google.gson.annotations.SerializedName

class BookResponse(
    @SerializedName("items") val items: List<BookItem>?
)
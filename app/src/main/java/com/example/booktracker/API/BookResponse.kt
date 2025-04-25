package com.example.booktracker.APIЫ

import com.example.booktracker.API.BookItem
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class BookResponse(
    @SerialName("items") val items: List<BookItem>? = emptyList()
)
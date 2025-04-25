package com.example.booktracker.API

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookItem(
    @SerialName("volumeInfo") val bookItem: Book
)

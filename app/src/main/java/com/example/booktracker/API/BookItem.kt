package com.example.booktracker.API

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookItem(
    @SerialName("volumeInfo") val bookItem: Book
)

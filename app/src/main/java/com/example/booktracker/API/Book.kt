package com.example.booktracker.API

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    @SerialName("title") val title: String? = "",
    @SerialName("subtitle") val subTitle: String? = "",
    @SerialName("authors") val authors: List<String>? = emptyList(),
    @SerialName("publishedDate") val publishedDate: String? = "",
    @SerialName("description") val description: String? = "",
    @SerialName("imageLinks") val imageLinks: ImageLinks? = ImageLinks(),
    @SerialName("pageCount") val pageCount: Int? = 0
)

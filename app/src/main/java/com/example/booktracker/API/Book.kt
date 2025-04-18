package com.example.booktracker.API

import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subTitle: String,
    @SerializedName("authors") val authors: List<String>,
    @SerializedName("publishedDate") val publishedDate: String,
    @SerializedName("description") val descriptor: String,
    @SerializedName("imageLinks") val imageLinks: ImageLinks,
    @SerializedName("pageCount") val pageCount: String
)

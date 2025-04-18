package com.example.booktracker.API

import com.google.gson.annotations.SerializedName

data class ImageLinks(
    @SerializedName("smallThumbnail") val badImage: String,
    @SerializedName("thumbnail") val maybeNormImage: String
)

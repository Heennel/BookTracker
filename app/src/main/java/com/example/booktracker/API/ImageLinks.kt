package com.example.booktracker.API

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageLinks(
    @SerialName("smallThumbnail") val badImage: String? = "",
    @SerialName("thumbnail") val maybeNormImage: String? = ""
)

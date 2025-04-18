package com.example.booktracker.API

import com.google.gson.annotations.SerializedName

data class VolumeInfo(
    @SerializedName("volumeInfo") val volumeInfo: Book
)

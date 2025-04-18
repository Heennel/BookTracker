package com.example.booktracker.API

import com.example.booktracker.APIЫ.BookResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface GoogleBooksApi {
    @GET("books/v1/volumes")
    fun getBooks(
        @Query("q", encoded = false) bookName: String
    ): Call<BookResponse>
}
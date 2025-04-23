package com.example.booktracker.Modules

import com.example.booktracker.API.GoogleBooksApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchModule {

    @Provides
    @Singleton
    fun provideApiBooks(retrofit: Retrofit): GoogleBooksApi{
        return retrofit.create<GoogleBooksApi>()
    }

    @Provides
    @Singleton
    fun provigeSearchRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
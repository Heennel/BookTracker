package com.example.booktracker.Modules

import com.example.booktracker.API.GoogleBooksApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class SearchModule {

    @Provides
    @ViewModelScoped
    fun provideApiBooks(retrofit: Retrofit): GoogleBooksApi{
        return retrofit.create<GoogleBooksApi>()
    }

    @Provides
    @ViewModelScoped
    fun provigeSearchRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
package com.example.booktracker.Modules

import com.example.booktracker.API.GoogleBooksApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class SearchModule {

    @Provides
    @ViewModelScoped
    fun provideLoggingInspector(): HttpLoggingInterceptor{
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @ViewModelScoped
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @ViewModelScoped
    fun provigeSearchRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit{

        val contentType = "application/json".toMediaType()

        val json = Json{
            explicitNulls = true
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @ViewModelScoped
    fun provideApiBooks(retrofit: Retrofit): GoogleBooksApi{
        return retrofit.create<GoogleBooksApi>()
    }
}
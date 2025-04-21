package com.example.booktracker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

    private val sharedPref: SharedPreferences by lazy { getSharedPreferences(APP_NAME, MODE_PRIVATE) }

    companion object{
        private const val APP_NAME = "Book Tracker"
        private const val DARK_THEME_KEY = "DARK_THEME_KEY"
    }

    override fun onCreate() {
        super.onCreate()
        changeTheme(sharedPref.getBoolean(DARK_THEME_KEY,false))
    }

    fun changeTheme(enable: Boolean){
        val mode = if(enable) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
        sharedPref.edit().putBoolean(DARK_THEME_KEY,enable).apply()
    }
}
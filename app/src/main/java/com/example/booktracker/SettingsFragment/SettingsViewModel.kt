package com.example.booktracker.SettingsFragment

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.booktracker.App

class SettingsViewModel(
    val sharedPref: SharedPreferences,
    val app: App
): ViewModel() {

    private val _themeData = MutableLiveData<Boolean>()
    val themeData: LiveData<Boolean> get() = _themeData

    init {
        _themeData.value = sharedPref.getBoolean(DARK_THEME_KEY,false)
    }

    fun changeTheme(enable: Boolean){
        app.changeTheme(enable)
        _themeData.value = enable
    }

    companion object{
        private const val DARK_THEME_KEY = "DARK_THEME_KEY"
    }
}
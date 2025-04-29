package com.example.booktracker.SettingsFragment

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.booktracker.App
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext val context: Context
): ViewModel() {

    private val app = context.applicationContext as App

    private val sharedPref = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)

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
        private const val APP_NAME = "Book Tracker"
    }
}
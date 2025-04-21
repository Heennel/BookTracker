package com.example.booktracker.SettingsFragment

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.booktracker.App

class SettingsViewModelFactory(
    val sharedPreferences: SharedPreferences,
    val app: App
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SettingsViewModel(sharedPreferences, app) as T
    }
}
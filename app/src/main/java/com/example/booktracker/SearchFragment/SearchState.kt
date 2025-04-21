package com.example.booktracker.SearchFragment

import com.example.booktracker.API.Book

enum class SearchState {
    LOADING,
    SUCCESS,
    NOT_FOUND,
    ERROR,
    NOTHING
}
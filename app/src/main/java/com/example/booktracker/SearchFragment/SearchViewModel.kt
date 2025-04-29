package com.example.booktracker.SearchFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracker.API.Book
import com.example.booktracker.API.GoogleBooksApi
import com.example.booktracker.APIЫ.BookResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.create
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val bookApi: GoogleBooksApi
) : ViewModel() {

    private var searchJob: Job? = null

    private val _listRCView = MutableLiveData<List<Book>>(emptyList())
    val listRCView: MutableLiveData<List<Book>> get() = _listRCView

    private val _searchState = MutableLiveData<SearchState>(SearchState.NOTHING)
    val searchState: MutableLiveData<SearchState> get() = _searchState

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    init {
        searchEngine()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun searchEngine() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchQuery
                .onEach { query ->
                    if (query.isBlank()) {
                        clearList()
                        changeState(SearchState.NOTHING)
                    }
                }
                .debounce(300)
                .distinctUntilChanged()
                .filter { !it.isBlank() }
                .flatMapLatest { query ->
                    doRequest(query)
                        .catch { e ->
                            Log.d("2","ОШИБКА - ${e.message}")
                            changeState(SearchState.ERROR)
                            clearList()
                            emit(BookResponse(emptyList()))
                        }
                }
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    if (_searchState.value != SearchState.ERROR) {
                        updateUI(result)
                    }
                }
        }
    }

    private fun doRequest(query: String): Flow<BookResponse> = flow {
        changeState(SearchState.LOADING)
        try {
            val result = bookApi.getBooks(query, API_KEY)
            emit(result)
        } catch (e: Exception) {
            Log.d("1","АШИПКА - ${e.message}")
            changeState(SearchState.ERROR)
        }
    }

    private fun updateUI(result: BookResponse?) {
        val resultList = result?.items?.map { it.bookItem } ?: emptyList()
        _listRCView.postValue(resultList)

        if (_searchState.value != SearchState.ERROR) {
            val newState = if (resultList.isEmpty()) SearchState.NOT_FOUND else SearchState.SUCCESS
            changeState(newState)
        }
    }

    private fun clearList() {
        _listRCView.postValue(emptyList())
    }

    private fun changeState(state: SearchState) {
        _searchState.postValue(state)
    }

    fun updateSearch(query: String){
        _searchQuery.value = query
    }

    companion object{
        private const val API_KEY = "AIzaSyDLEeZxBnsGuDGjXt7ta8NgnmMc-Nt665I"
    }
}
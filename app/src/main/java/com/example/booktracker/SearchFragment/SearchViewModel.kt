package com.example.booktracker.SearchFragment

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

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun searchEngine(flow: Flow<String>) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            flow
                .onEach { query ->
                    if (query.isBlank()) {
                        clearList()
                        changeState(SearchState.NOTHING)
                    }
                }
                .debounce(300)
                .filter { !it.isBlank() }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    doRequest(query)
                        .catch { e ->
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
            val result = bookApi.getBooks(query)
            emit(result)
        } catch (e: Exception) {
            changeState(SearchState.ERROR)
            throw e
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
}
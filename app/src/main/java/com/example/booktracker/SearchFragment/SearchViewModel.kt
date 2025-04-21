package com.example.booktracker.SearchFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktracker.API.Book
import com.example.booktracker.API.GoogleBooksApi
import com.example.booktracker.API.RetrofitFactory
import com.example.booktracker.APIЫ.BookResponse
import kotlinx.coroutines.CoroutineScope
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

class SearchViewModel(

): ViewModel() {

    private var searchJob: Job? = null

    val bookApi = RetrofitFactory.createRetrofit().create<GoogleBooksApi>()

    private val _listRCView = MutableLiveData<List<Book>>(emptyList())
    val listRCView: MutableLiveData<List<Book>> get() = _listRCView

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun searchEngine(flow: Flow<String>){
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            flow
                .onEach {query ->
                    if (query.isBlank()){
                        clearList()
                    }
                }
                .debounce(300)
                .filter { !it.isBlank() }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    doRequest(query).catch { e ->
                        clearList()
                        emit(BookResponse(emptyList()))
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect{ result ->
                    updateUI(result)
                }
        }
    }

    fun doRequest(query: String): Flow<BookResponse> = flow {
        val result = bookApi.getBooks(query)
        emit(result)
    }

    fun updateUI(result: BookResponse?){
        val resultList = result?.items?.map { it.bookItem } ?: emptyList()
        _listRCView.postValue(resultList)
    }

    private fun clearList(){
        _listRCView.postValue(emptyList())
    }
}
package com.example.booktracker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktracker.API.GoogleBooksApi
import com.example.booktracker.API.RetrofitFactory
import com.example.booktracker.APIЫ.BookResponse
import com.example.booktracker.RecyclerView.BookAdapter
import com.example.booktracker.databinding.FragmentSearchBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.create

class SearchFragment : Fragment() {
    private var searchJob: Job? = null

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val bookApiService = RetrofitFactory.createRetrofit().create<GoogleBooksApi>()

    private val bookAdapter = BookAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearch()
        setupAdapter()
        setupClearButton()
    }

    private fun setupAdapter(){
        with(binding) {
            recyclerBooks.adapter = bookAdapter
            recyclerBooks.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClearButton() {
        with(binding) {
            fragmentSearchClearImg.setOnClickListener {
                if (fragmentSearchEditText.text.isNotEmpty()) {
                    fragmentSearchEditText.text.clear()
                    fragmentSearchEditText.clearFocus()
                    fragmentSearchClearImg.visibility = View.INVISIBLE
                    bookAdapter.refreshList(emptyList())
                    searchJob?.cancel()
                }
            }
        }
    }
    private fun observer(): Flow<String> = callbackFlow {
        val watcher = object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isNotBlank()){
                    trySend(s.toString())
                }
                val isEmpty = s.isNullOrEmpty()
                binding.fragmentSearchClearImg.visibility = if (isEmpty) View.INVISIBLE else View.VISIBLE
            }
        }
        binding.fragmentSearchEditText.addTextChangedListener(watcher)
        awaitClose{ binding.fragmentSearchEditText.removeTextChangedListener(watcher)}
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun setupSearch(){
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            observer()
                .debounce(320)
                .filter { it.length>1 }
                .distinctUntilChanged()
                .flatMapLatest {query ->
                    doRequest(query).catch { e ->
                        bookAdapter.refreshList(emptyList())
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect{ result ->
                    updateList(result)
                }
        }
    }

    private fun doRequest(query: String): Flow<BookResponse> = flow{
        val result = bookApiService.getBooks(query)
        emit(result)
    }

    private fun updateList(result: BookResponse){
        val resultList = result.items?.map { it.bookItem }?.toList()?: emptyList()
        bookAdapter.refreshList(resultList)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        searchJob?.cancel()
        _binding = null
    }
}
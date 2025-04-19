package com.example.booktracker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktracker.API.GoogleBooksApi
import com.example.booktracker.API.RetrofitFactory
import com.example.booktracker.APIЫ.BookResponse
import com.example.booktracker.RecyclerView.BookAdapter
import com.example.booktracker.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class SearchFragment : Fragment() {

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

        with(binding) {
            recyclerBooks.adapter = bookAdapter
            recyclerBooks.layoutManager = LinearLayoutManager(requireContext())
            fragmentSearchClearImg.setOnClickListener {
                if (fragmentSearchEditText.text.isNotEmpty()) {
                    fragmentSearchEditText.text.clear()
                    fragmentSearchEditText.clearFocus()
                    fragmentSearchClearImg.visibility = View.INVISIBLE
                }
            }

            fragmentSearchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val isEmpty = s.isNullOrEmpty()
                    fragmentSearchClearImg.visibility = if (isEmpty) View.INVISIBLE else View.VISIBLE
                    if(isEmpty){
                        searchPlaceholderBase.visibility = View.GONE
                    }
                }
            })

            fragmentSearchEditText.setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(fragmentSearchEditText.text.isNotEmpty()){
                        getBooks()
                    }
                }
                false
            }
        }
    }

    private fun getBooks(){
        val query = binding.fragmentSearchEditText.text
        if(!query.isNullOrEmpty()){
            bookApiService.getBooks(query.toString()).enqueue(object : Callback<BookResponse>{
                override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                    if (response.isSuccessful){
                        val responseList = response.body()?.items
                        val books = responseList?.map { it.bookItem } ?: emptyList()
                        bookAdapter.refreshList(books)
                    }
                }
                override fun onFailure(p0: Call<BookResponse>, p1: Throwable) {
                    binding.fragmentSearchEditText.setText("")
                }
            })
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
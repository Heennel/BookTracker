package com.example.booktracker.SearchFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktracker.RecyclerView.BookAdapter
import com.example.booktracker.databinding.FragmentSearchBinding
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val bookAdapter = BookAdapter()
    private val viewModel: SearchViewModel by viewModels()

    private var textWatcher: TextWatcher? = null
    private var watcherFlow: Flow<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupClearButton()

        watcherFlow = createWatcherFlow()
        watcherFlow?.let { viewModel.searchEngine(it) }

        viewModel.listRCView.observe(viewLifecycleOwner) {
            bookAdapter.refreshList(it)
        }
    }

    private fun setupAdapter() {
        with(binding) {
            recyclerBooks.adapter = bookAdapter
            recyclerBooks.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClearButton() {
        binding.fragmentSearchClearImg.setOnClickListener {
            if (binding.fragmentSearchEditText.text.isNotEmpty()) {
                binding.fragmentSearchEditText.text.clear()
                binding.fragmentSearchEditText.clearFocus()
                binding.fragmentSearchClearImg.visibility = View.INVISIBLE
            }
        }
    }

    private fun createWatcherFlow(): Flow<String> = callbackFlow {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                trySend(s?.toString() ?: "")
                val isEmpty = s.isNullOrEmpty()
                if (_binding != null) {
                    binding.fragmentSearchClearImg.visibility =
                        if (isEmpty) View.INVISIBLE else View.VISIBLE
                }
            }
        }

        textWatcher?.let { watcher ->
            binding.fragmentSearchEditText.addTextChangedListener(watcher)
        }

        awaitClose {
            if (_binding != null) {
                textWatcher?.let { watcher ->
                    binding.fragmentSearchEditText.removeTextChangedListener(watcher)
                }
            }
            textWatcher = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher?.let { watcher ->
            binding.fragmentSearchEditText.removeTextChangedListener(watcher)
        }
        textWatcher = null
        watcherFlow = null
        _binding = null
    }
}
package com.example.booktracker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import com.example.booktracker.databinding.FragmentSearchBinding
import com.example.booktracker.databinding.FragmentSettingsBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            fragmentSearchClearImg.setOnClickListener {
                if (fragmentSearchEditText.text.isNotEmpty()) {
                    fragmentSearchEditText.text.clear()
                    fragmentSearchEditText.clearFocus()
                    fragmentSearchClearImg.visibility = View.INVISIBLE
                }
            }

            fragmentSearchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (fragmentSearchEditText.text.isNullOrEmpty()) {
                        fragmentSearchClearImg.visibility = View.INVISIBLE
                    } else {
                        fragmentSearchClearImg.visibility = View.VISIBLE
                        searchPlaceholderBase.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(s: Editable?){}
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.booktracker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import com.example.booktracker.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsFragmentEmailButton.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            val mail = resources.getString(R.string.email_adress)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
            startActivity(supportIntent)
        }
        binding.settingsFragmentShareButton.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val shareText = resources.getString(R.string.share_text)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.share_subject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(shareIntent)
        }

        binding.switchMaterial.setOnCheckedChangeListener { buttonView, isChecked ->
            val mode = if(isChecked){
                AppCompatDelegate.MODE_NIGHT_YES
            }else{
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
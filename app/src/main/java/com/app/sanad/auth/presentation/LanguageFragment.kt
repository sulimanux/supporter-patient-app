package com.app.sanad.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseBottomSheetDialogFragment
import com.app.sanad.databinding.FragmentLangauageBinding
import com.app.sanad.util.ENGLISH_KEY
import com.app.sanad.util.LANGUAGE
import com.app.sanad.app.presentation.SplashActivity
import com.app.sanad.util.log

@AndroidEntryPoint
class LanguageFragment : BaseBottomSheetDialogFragment() {

    // ViewModel scoped to this fragment
    private val viewModel: AuthViewModel by viewModels()

    // View binding
    private lateinit var binding: FragmentLangauageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate UI
        binding = FragmentLangauageBinding.inflate(inflater, container, false)
        setupClickListener()
        initializeViews()
        return binding.root
    }

    // Close sheet
    fun setupClickListener() {
        binding.close.setOnClickListener { dismiss() }
    }

    // Initialize UI based on saved language
    fun initializeViews() {
        val currentLang = viewModel.sharedPreferences.getString(LANGUAGE)
        log("$currentLang init")

        if (currentLang.isEmpty() || currentLang == ENGLISH_KEY) {
            binding.rbEnglish.isChecked = true
            binding.rbArabic.textDirection = View.TEXT_DIRECTION_LTR
        } else {
            binding.rbArabic.isChecked = true
            binding.close.setBackgroundDrawable(resources.getDrawable(R.drawable.background_back_right))
            binding.root.setBackgroundDrawable(resources.getDrawable(R.drawable.corner_top_lift))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    // Watch language change, save, and restart app
    private fun observeViewModel() {
        var previousLang = viewModel.currentLang.value

        viewModel.currentLang.observe(viewLifecycleOwner) { newLang ->
            log("observeViewModel new=$newLang old=$previousLang")

            if (newLang != previousLang && newLang != null) {
                viewModel.sharedPreferences.storeString(LANGUAGE, newLang)
                startActivity(Intent(activity, SplashActivity::class.java))
                activity?.finish()
            }
            previousLang = newLang
        }
    }
}

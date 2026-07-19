package com.app.sanad.users.patient.profile.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.base.BaseFragment
import com.app.sanad.R
import com.app.sanad.databinding.FragmentSettingsBinding
import com.app.sanad.util.ARABIC_KEY
import com.app.sanad.util.ENGLISH_KEY
import com.app.sanad.util.LANGUAGE
import com.app.sanad.util.SCHEDULING_TIME
import com.app.sanad.app.presentation.SplashActivity

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: ProfileViewModel by viewModels()

    // Stores currently saved language and user-selected language
    private var currentLang = ""
    private var selectedLang = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        initViews()
        setupListeners()

        return binding.root
    }

    /**
     * Sets up UI button listeners and handles primary user actions.
     */
    private fun setupListeners() {
        // Back button returns to previous screen
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Save button: apply language changes only if the user selected a different language
        binding.button.setOnClickListener {
            if (currentLang == selectedLang) {
                findNavController().popBackStack()
            } else {
                applyNewLanguage(selectedLang)
            }
        }
    }

    /**
     * Loads the current saved settings and configures initial UI.
     */
    private fun initViews() {
        // Retrieve stored language
        currentLang = viewModel.sharedPreferences.getString(LANGUAGE)

        // Update back arrow direction for RTL languages
        if (currentLang != ENGLISH_KEY) {
            binding.icBack.background =
                resources.getDrawable(R.drawable.background_back_right, null)
        }

        // Initially, selected language equals stored language
        selectedLang = currentLang

        setupLanguageRadio()
        setupReminderRadio()
    }

    /**
     * Initializes the medication reminder time selector
     * and updates stored value whenever the user changes it.
     */
    private fun setupReminderRadio() {
        val schedulingTime = viewModel.sharedPreferences.getInt(SCHEDULING_TIME, 7)

        // Pre-check correct radio based on saved reminder time
        when (schedulingTime) {
            7 -> binding.rbMorning.isChecked = true
            13 -> binding.rbAfternoon.isChecked = true
            20 -> binding.rbEvening.isChecked = true
        }

        // Store new reminder time when selection changes
        binding.groupRoot2.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbMorning.id -> viewModel.sharedPreferences.storeInt(SCHEDULING_TIME, 7)
                binding.rbAfternoon.id -> viewModel.sharedPreferences.storeInt(SCHEDULING_TIME, 13)
                binding.rbEvening.id -> viewModel.sharedPreferences.storeInt(SCHEDULING_TIME, 20)
            }
        }
    }

    /**
     * Loads the current language setting into the radio buttons
     * and updates selectedLang when the user changes it.
     */
    private fun setupLanguageRadio() {
        // Pre-check current language
        when (currentLang) {
            ENGLISH_KEY -> binding.rbEnglish.isChecked = true
            ARABIC_KEY -> binding.rbArabic.isChecked = true
        }

        // Update the selected language value
        binding.groupRoot.setOnCheckedChangeListener { _, checkedId ->
            selectedLang = when (checkedId) {
                binding.rbEnglish.id -> ENGLISH_KEY
                binding.rbArabic.id -> ARABIC_KEY
                else -> currentLang
            }
        }
    }

    /**
     * Saves the new language, restarts the app through SplashActivity,
     * and applies updated locale settings.
     */
    private fun applyNewLanguage(lang: String) {
        viewModel.sharedPreferences.storeString(LANGUAGE, lang)
        startActivity(Intent(requireActivity(), SplashActivity::class.java))
        requireActivity().finish()
    }
}

package com.app.sanad.auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseBottomSheetDialogFragment
import com.app.sanad.databinding.FragmentGenderBinding
import com.app.sanad.util.ENGLISH_KEY
import com.app.sanad.util.log

@AndroidEntryPoint
class GenderFragment : BaseBottomSheetDialogFragment() {

    // Shared ViewModel with parent Activity
    private val viewModel: AuthViewModel by activityViewModels()

    // View binding
    private lateinit var binding: FragmentGenderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout
        binding = FragmentGenderBinding.inflate(inflater, container, false)
        setupClickListener()
        initializeViews()
        log("GenderFragment() => ${viewModel.name.value}")
        return binding.root
    }

    // Close sheet on click
    fun setupClickListener() {
        binding.close.setOnClickListener { dismiss() }
    }

    // Init UI based on language and selected gender
    fun initializeViews() {
        if (viewModel.currentLang.value != ENGLISH_KEY) {
            binding.close.setBackgroundDrawable(resources.getDrawable(R.drawable.background_back_right))
            binding.root.setBackgroundDrawable(resources.getDrawable(R.drawable.corner_top_lift))
        }
        changeUserUi(viewModel.intGender.value)
    }

    // Observe gender changes
    private fun observeViewModel() {
        viewModel.intGender.observe(viewLifecycleOwner) {
            viewModel.setStrGender(requireContext())
            changeUserUi(it)
        }
    }

    // Update UI based on selected gender
    private fun changeUserUi(type: Int?) {
        when (type) {
            1 -> {
                binding.male.setBackgroundResource(R.drawable.corner_four_dark_blue)
                binding.maleText.setTextColor(resources.getColor(R.color.white))
                binding.female.setBackgroundResource(R.drawable.corner_four_gray)
                binding.femaleText.setTextColor(resources.getColor(R.color.dark_gray))
                binding.maleStatusCircle.visibility = View.GONE
                binding.maleStatusChecked.visibility = View.VISIBLE
                binding.femaleStatusChecked.visibility = View.GONE
                binding.femaleStatusCircle.visibility = View.VISIBLE
            }
            2 -> {
                binding.female.setBackgroundResource(R.drawable.corner_four_dark_blue)
                binding.femaleText.setTextColor(resources.getColor(R.color.white))
                binding.male.setBackgroundResource(R.drawable.corner_four_gray)
                binding.maleText.setTextColor(resources.getColor(R.color.dark_gray))
                binding.femaleStatusCircle.visibility = View.GONE
                binding.femaleStatusChecked.visibility = View.VISIBLE
                binding.maleStatusChecked.visibility = View.GONE
                binding.maleStatusCircle.visibility = View.VISIBLE
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        observeViewModel()
    }
}

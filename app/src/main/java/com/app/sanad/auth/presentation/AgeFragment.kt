package com.app.sanad.auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseBottomSheetDialogFragment
import com.app.sanad.databinding.FragmentAgeBinding
import com.app.sanad.util.AGE_GROUP
import com.app.sanad.util.ENGLISH_KEY

@AndroidEntryPoint
class AgeFragment : BaseBottomSheetDialogFragment() {

    // Shared ViewModel with activity
    private val viewModel: AuthViewModel by activityViewModels()

    // View binding
    private lateinit var binding: FragmentAgeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate view
        binding = FragmentAgeBinding.inflate(inflater,container,false)
        setupClickListener()
        initializeViews()
        return binding.root
    }

    // Close button click
    fun setupClickListener() {
        binding.close.setOnClickListener {
            dismiss()
        }
    }

    // UI init based on language and selected age
    fun initializeViews() {
        if (viewModel.currentLang.value != ENGLISH_KEY) {
            // Change UI direction for RTL languages
            binding.close.setBackgroundDrawable(resources.getDrawable(R.drawable.background_back_right))
            binding.root.setBackgroundDrawable(resources.getDrawable(R.drawable.corner_top_lift))
        }
        setChoosenAge(viewModel.intAge.value)
    }

    // Set checked age radio button
    private fun setChoosenAge(age: Int?) {
        age?.let {
            when (age) {
                1 -> binding.rbYoung.isChecked = true
                2 -> binding.rbMiddleAge.isChecked = true
                3 -> binding.rbOlder.isChecked = true
            }
        }
    }

    // Observe age changes
    private fun observeViewModel() {
        viewModel.intAge.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.setStrAge(requireActivity())
                setChoosenAge(it)
            }
        }
    }

    // Bind UI + start observing ViewModel
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        observeViewModel()
    }
}

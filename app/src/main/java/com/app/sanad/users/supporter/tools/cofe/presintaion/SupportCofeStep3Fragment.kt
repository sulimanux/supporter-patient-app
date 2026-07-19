package com.app.sanad.users.supporter.tools.cofe.presintaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.databinding.FragmentSupportCofeStep3Binding

/**
 * Fragment representing Step 3 in the Support Coffee flow for supporters.
 * Provides navigation options: back, next, or exit.
 */
class SupportCofeStep3Fragment : Fragment() {

    // ViewBinding instance
    private lateinit var binding: FragmentSupportCofeStep3Binding

    /**
     * Called to create and return the fragment view.
     * Sets up click listeners for back, exit, and next actions.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupportCofeStep3Binding.inflate(inflater, container, false)

        // Navigate back to previous fragment
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        // Exit the activity completely
        binding.exit.setOnClickListener {
            activity?.finish()
        }

        // Navigate to the "What Should Do" fragment
        binding.constraintNext.setOnClickListener {
            findNavController().navigate(R.id.action_supportCofeStep3Fragment_to_whatShouldDoFragment)
        }
        return binding.root
    }
}

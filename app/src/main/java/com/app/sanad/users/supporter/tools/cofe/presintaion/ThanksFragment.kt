package com.app.sanad.users.supporter.tools.cofe.presintaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.databinding.FragmentThanksBinding

/**
 * Fragment to thank the user at the end of the Support Coffee flow.
 * Provides options to exit the app or navigate to the next step.
 */
class ThanksFragment : Fragment() {

    // ViewBinding instance
    private lateinit var binding: FragmentThanksBinding

    /**
     * Called to create and return the fragment view.
     * Sets up click listeners for back, next, and exit actions.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThanksBinding.inflate(inflater, container, false)

        // Exit the activity completely
        binding.exit.setOnClickListener {
            activity?.finish()
        }

        // Navigate to the exitCofeFragment (end of flow)
        binding.constraintNext.setOnClickListener {
            findNavController().navigate(R.id.action_thanksFragment_to_exitCofeFragment2)
        }

        // Navigate back to previous fragment
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}

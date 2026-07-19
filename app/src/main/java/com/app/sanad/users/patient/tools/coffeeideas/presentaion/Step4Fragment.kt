package com.app.sanad.users.patient.tools.coffeeideas.presentaion

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentStep4Binding
import com.app.sanad.util.REFRAME_ENGAGEMENT
import com.app.sanad.util.REQUIRED
import com.app.sanad.util.REWRITE_THOUGHT
import com.app.sanad.util.TIME_SPENT
import com.app.sanad.util.Temp
import com.app.sanad.util.USER_SKIP_INPUT
import com.app.sanad.util.log
import kotlin.getValue

/**
 * Step 4 of the Coffee Ideas flow.
 *
 * This step allows the patient to reflect on their thought and rewrite it
 * after reviewing or resetting it in Step3.
 *
 * Handles:
 * - Input validation (cannot proceed if text is empty)
 * - Navigation to Step5
 * - Analytics logging for skipped input, engagement, and rewriting
 */
@AndroidEntryPoint
class Step4Fragment : BaseFragment() {

    // View binding for this fragment
    private lateinit var binding: FragmentStep4Binding

    // Shared ViewModel scoped to the activity
    private val viewModel: CofeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout using ViewBinding
        binding = FragmentStep4Binding.inflate(inflater, container, false)

        // Bind ViewModel to layout for data binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Set up button listeners
        setUPListener()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Track current fragment for analytics
        Temp.stoppedAt = "Step4Fragment"
    }

    /**
     * Sets up click listeners for NEXT, BACK, and EXIT buttons
     */
    private fun setUPListener() {

        // Exit the activity
        binding.exit.setOnClickListener {
            activity?.finish()
        }

        // NEXT button: validate that user entered their rewritten thought
        binding.constraintNext.setOnClickListener {
            if (binding.editText.text.isNullOrEmpty()) {
                log("please write your idea")

                // Log skipped input for analytics
                logEvent(USER_SKIP_INPUT) {
                    param(REQUIRED, REWRITE_THOUGHT)
                }

                // Show toast to prompt user
                showToast(getString(R.string.please_write_your_idea))
            } else {
                log("next")
                log("your idea is  ${binding.editText.text.toString()}")

                // Navigate to Step5 where idea is finalized/shared
                findNavController().navigate(R.id.action_step4Fragment_to_step5Fragment)
            }
        }

        // BACK button: navigate to previous step
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onStop() {
        super.onStop()

        // Log whether user rewrote their thought
        logEvent(REWRITE_THOUGHT) {
            param(REWRITE_THOUGHT, (binding.editText.text.isNotEmpty()).toString())
        }

        // Log engagement time for this reframe step
        logEvent(REFRAME_ENGAGEMENT) {
            param(TIME_SPENT, duration())
        }
    }
}

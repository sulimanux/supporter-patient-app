package com.app.sanad.users.patient.tools.coffeeideas.presentaion

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentStep5Binding
import com.app.sanad.util.REFLECT_ENGAGEMENT
import com.app.sanad.util.TIME_SPENT
import com.app.sanad.util.Temp
import kotlin.getValue

/**
 * Step 5 of the Coffee Ideas flow.
 *
 * This step finalizes the patient's thought and allows proceeding to Step6.
 * Handles navigation, exit, back, and logs engagement time.
 */
class Step5Fragment : BaseFragment() {

    // View binding instance
    private lateinit var binding: FragmentStep5Binding

    // Shared ViewModel scoped to the activity
    private val viewModel: CofeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout
        binding = FragmentStep5Binding.inflate(inflater)

        // Exit activity
        binding.exit.setOnClickListener {
            activity?.finish()
        }

        // NEXT button: proceed to Step6
        binding.constraintNext.setOnClickListener {
            findNavController().navigate(R.id.action_step5Fragment_to_step6Fragment)
        }

        // BACK button
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Track current fragment for engagement analytics
        Temp.stoppedAt = "Step5Fragment"
    }

    override fun onStop() {
        super.onStop()
        // Reset flag to indicate no ongoing supporter connection
        viewModel.sharedPreferences.storeBoolean("isThereConnection", false)

        // Log time spent in this reflection step
        logEvent(REFLECT_ENGAGEMENT) {
            param(TIME_SPENT, duration())
        }
    }
}

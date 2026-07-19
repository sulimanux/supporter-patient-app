/**
 * Fragment that displays a congratulatory screen after completing the mood-tracking task,
 * logs session time, and navigates the user to the next stage of the daily program.
 */
package com.app.sanad.users.patient.moodTracking.presentaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.sanad.base.BaseFragment
import com.app.sanad.R
import com.app.sanad.databinding.FragmentCongratulationsBinding
import com.app.sanad.util.CONGRATULATIONS
import com.app.sanad.util.TIME_SPENT
import com.app.sanad.util.Temp

class CongratulationsFragment : BaseFragment() {

    private lateinit var binding: FragmentCongratulationsBinding

    /**
     * Inflates layout and attaches navigation listeners.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCongratulationsBinding.inflate(inflater, container, false)

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_congratulationsFragment2_to_postDailyProgramFragment)
        }

        binding.icBack.setOnClickListener {
            activity?.finish()
        }

        return binding.root
    }

    /**
     * Tracks screen activation for flow state management.
     */
    override fun onStart() {
        super.onStart()
        Temp.stoppedAt = CONGRATULATIONS
    }

    /**
     * Logs user time spent on screen for analytics.
     */
    override fun onStop() {
        super.onStop()

    }
}

/**
 * Fragment showing comparison between pre-mood and post-mood states.
 * Handles UI rendering, logs session analytics, triggers next-day setup, and finalizes mood tracking flow.
 */
package com.app.sanad.users.patient.moodTracking.presentaion

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.DialogProgressMoodBinding
import com.app.sanad.databinding.FragmentCompareResultsBinding
import com.app.sanad.users.patient.dailyprogram.data.entity.CurrentDay
import com.app.sanad.users.patient.main.presentaion.UserScreensActivity
import com.app.sanad.users.patient.moodTracking.presentaion.viewmodels.MoodTrackingViewModel
import com.app.sanad.util.COMPARE_RESULT
import com.app.sanad.util.MOOD
import com.app.sanad.util.POST_SELECTED_MOOD
import com.app.sanad.util.PRE_SELECTED_MOOD
import com.app.sanad.util.TIME_SPENT
import com.app.sanad.util.Temp

@AndroidEntryPoint
class CompareResultsFragment : BaseFragment() {

    private lateinit var binding: FragmentCompareResultsBinding
    private val viewModel: MoodTrackingViewModel by activityViewModels()
    private var preMoodName = ""
    private var postMoodName = ""

    /**
     * Inflates layout, initializes UI, triggers remote sync, and moves program to next day.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCompareResultsBinding.inflate(inflater, container, false)
        setUpListeners()
        Temp.completion = true
        setUpUi(viewModel.currentDay())
        viewModel.getNextDay(viewModel.currentDay().status?.day!! + 1)
        viewModel.storeDayMoodTrackingRemotely()
        return binding.root
    }

    /**
     * Marks screen as active in analytics state tracker.
     */
    override fun onStart() {
        super.onStart()
        Temp.stoppedAt = COMPARE_RESULT
    }

    /**
     * Logs session metrics and selected moods on exit.
     */
    override fun onStop() {
        super.onStop()
       Temp.pre_mode_selected = preMoodName
        Temp.post_mode_selected = postMoodName

    }

    /**
     * Sets UI click listeners for navigation and finish actions.
     */
    private fun setUpListeners() {
        binding.btnNext.setOnClickListener { showDialog() }
        binding.exit.setOnClickListener { activity?.finish() }
        binding.icBack.setOnClickListener { findNavController().popBackStack() }
    }

    /**
     * Renders mood comparison UI with correct colors, names, and emoji assets.
     */
    private fun setUpUi(currentDay: CurrentDay) {
        val preMood = viewModel.getEmojisStatus(requireContext())[currentDay.status?.preMoodIndex!!]
        val postMood = viewModel.getEmojisStatus(requireContext())[viewModel.getPostMoodIndex()]

        preMoodName = preMood.name
        postMoodName = postMood.name

        binding.moodBefore.text = preMood.name
        binding.imageBefore.setImageResource(preMood.emoji)
        binding.containerBefore.setBackgroundColor(Color.parseColor(preMood.backgroundColor))

        binding.containerAfter.setBackgroundColor(Color.parseColor(postMood.backgroundColor))
        binding.moodAfter.text = postMood.name
        binding.imageAfter.setImageResource(postMood.emoji)
    }

    /**
     * Shows final completion dialog confirming mood submission.
     * Redirects user back to main app screen.
     */
    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogProgressMoodBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(false)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val layoutParams = attributes
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.8).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = layoutParams
        }

        dialogBinding.btnNext.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(requireContext(), UserScreensActivity::class.java))
            activity?.finish()
        }

        dialog.show()
    }
}

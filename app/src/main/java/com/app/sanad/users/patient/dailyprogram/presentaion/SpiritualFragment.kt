/**
 * Handles the Spiritual daily-program task flow:
 * loads spiritual tasks, manages TTS playback, user navigation, and task completion status.
 */
package com.app.sanad.users.patient.dailyprogram.presentaion

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.databinding.LayoutTaskBinding
import com.app.sanad.util.CONTENT_COMPLETED
import com.app.sanad.util.CONTENT_ENGAGEMENT
import com.app.sanad.util.IS_COMPLETED
import com.app.sanad.util.TIME_SPENT
import com.app.sanad.util.SpiritualFragment
import com.app.sanad.util.TEXT_TO_SPEECH
import com.app.sanad.util.Temp
import com.app.sanad.util.TextToSpeechUtil
import com.app.sanad.util.durationAsString

@AndroidEntryPoint
class SpiritualFragment : BaseDailyProgramFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = LayoutTaskBinding.inflate(inflater, container, false)
        checkInternetConnection()
        setupClickListener()
        textToSpeech = TextToSpeechUtil(TextToSpeech(requireActivity(), null))
        textToSpeech.debug()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Temp.stoppedAt = SpiritualFragment
    }




    /**
     * Initializes UI, loads tasks, sets listeners, and prepares TTS.
     */
    override fun init() {
        showDescriptionDialog(
            R.drawable.icon_descriptionw,
            getString(R.string.a_whiff_of_faith),
            getString(R.string.a_whiff_of_faith)
        )

        viewModel.initTasksList("spiritual")
        if (viewModel.listOfTasks.size > 1) binding.btnRecommend.visibility = View.VISIBLE
        getTaskFromList(viewModel.status.currentIndexSpiritual!!)
        changeColorStatus()
    }

    /**
     * Sets up all UI click listeners.
     */
    private fun setupClickListener() {

        binding.play.setOnClickListener {
            if (textToSpeech.textToSpeech.isSpeaking) {
                textToSpeech.textToSpeech.stop()
                binding.play.setImageResource(R.drawable.icon_stop_sound)
            } else {
                textToSpeech.speakText(htmlText)
                binding.play.setImageResource(R.drawable.icon_play_sound)
                logEvent(TEXT_TO_SPEECH) { param(TEXT_TO_SPEECH, "true") }
            }
        }

        binding.icExit.setOnClickListener { activity?.finish() }




        binding.icBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnNext.setOnClickListener { updateStatus() }

        binding.btnPrevious.setOnClickListener { findNavController().popBackStack() }

        binding.btnRecommend.setOnClickListener {
            val currentIndex = getNextTask(viewModel.status.currentIndexSpiritual!!, 3)
            viewModel.status.currentIndexSpiritual = currentIndex
            viewModel.updateCurrentTaskLocally()
        }
    }

    /**
     * Updates task UI state colors based on program progress.
     */
    private fun changeColorStatus() {
        if (viewModel.status.educational == 1) binding.line1.setBackgroundColor(Color.parseColor("#6db7d3"))
        changeColorOfTaskImage(2, binding.constraintTask2, binding.imageTask2)
        changeColorOfTaskImage(viewModel.status.behavioral, binding.constraintTask3, binding.imageTask3)
        changeColorOfTaskImage(viewModel.status.educational, binding.constraintTask1, binding.imageTask1)
    }

    /**
     * Marks task as completed then navigates to next screen.
     */
    private fun updateStatus() {
        if (viewModel.status.spiritual != 1) updateStatusData()
        findNavController().navigate(R.id.action_spiritualFragment_to_activityFragment)
    }

    /**
     * Applies completion logic and updates progress.
     */
    private fun updateStatusData() {
        viewModel.status.spiritual = 1
        viewModel.updateCompletionRate()
        showToast(getString(R.string.the_second_task_was_completed_successfully))
    }

    override fun onStop() {
        super.onStop()
        Temp.contentEngagement +=  durationAsLong() // 1 + 2
        Temp.isContentCompleted = true
    }
}

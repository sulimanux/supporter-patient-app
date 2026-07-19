
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
import com.app.sanad.util.TIME_SPENT
import com.app.sanad.util.EducationalFragment
import com.app.sanad.util.IS_COMPLETED
import com.app.sanad.util.TEXT_TO_SPEECH
import com.app.sanad.util.Temp
import com.app.sanad.util.TextToSpeechUtil
import com.app.sanad.util.durationAsString
import com.app.sanad.util.log

/**
 * Handles the Educational task flow:
 * loads educational content, manages TTS, task completion, and navigation to next task.
 */

/**
 * Fragment that delivers the educational task experience:
 * displays educational content, enables text-to-speech playback, tracks completion,
 * and directs the user to the next task in the daily program flow.
 */
@AndroidEntryPoint
class EducationalFragment : BaseDailyProgramFragment(), TextToSpeech.OnInitListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutTaskBinding.inflate(inflater, container, false)
        hideSpiritualIcon(binding.constraintTask2, binding.line1)
        binding.btnPrevious.visibility = View.GONE
        checkInternetConnection()
        textToSpeech = TextToSpeechUtil(TextToSpeech(requireActivity(), this))
        textToSpeech.debug()
        setupClickListener()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Temp.stoppedAt = EducationalFragment
    }

    override fun init() {
        showDescriptionDialog(
            R.drawable.icon_lamp,
            getString(R.string.a_step_towards_change),
            getString(R.string.a_step_towards_change)
        )
        viewModel.initTasksList("educational")
        initializeViews()
    }

    /**
     * Prepares views, task list, and highlight state.
     */
    fun initializeViews() {
        if (viewModel.listOfTasks.size > 1) binding.btnRecommend.visibility = View.VISIBLE
        getTaskFromList(viewModel.status.currentIndexEducational!!)
        changeColorStatus()
    }

    /**
     * Assigns TTS, navigation, task-switching, and pause actions.
     */
    private fun setupClickListener() {

        binding.icExit.setOnClickListener { activity?.finish() }

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

        binding.icBack.setOnClickListener { activity?.finish() }
        binding.btnNext.setOnClickListener { updateStatus() }
        binding.btnRecommend.setOnClickListener {
            val currentIndex = getNextTask(viewModel.status.currentIndexEducational!!, 1)
            viewModel.status.currentIndexEducational = currentIndex
            viewModel.updateCurrentTaskLocally()
        }
    }

    /**
     * Updates progress UI colors based on task completion.
     */
    private fun changeColorStatus() {
        if (viewModel.status.educational == 1) {
            binding.line1.setBackgroundColor(Color.parseColor("#6db7d3"))
        }
        changeColorOfTaskImage(2, binding.constraintTask1, binding.imageTask1)
        changeColorOfTaskImage(viewModel.status.spiritual, binding.constraintTask2, binding.imageTask2)
        changeColorOfTaskImage(viewModel.status.behavioral, binding.constraintTask3, binding.imageTask3)
    }

    /**
     * Marks educational task complete if not done and navigates forward.
     */
    private fun updateStatus() {
        if (viewModel.status.educational != 1) updateStatusData()
        navigateToNextTask()
    }

    /**
     * Saves educational completion progress.
     */
    private fun updateStatusData() {
        viewModel.status.educational = 1
        viewModel.updateCompletionRate()
        showToast(getString(R.string.the_first_task_was_completed_successfully))
    }

    /**
     * Routes user to spiritual task if applicable, otherwise to behavioral task.
     */
    private fun navigateToNextTask() {
        if (viewModel.userProfile().religion!!) {
            findNavController().navigate(R.id.action_educationalFragment_to_spiritualFragment)
        } else {
            findNavController().navigate(R.id.action_educationFragment_to_behaviorActivationFragment)
        }
    }

    override fun onStop() {
        super.onStop()
        val duration = durationAsLong()
        Temp.contentEngagement = duration
        Temp.isContentCompleted = !viewModel.userProfile().religion!!
    }

    override fun onInit(p0: Int) { }
}

package com.app.sanad.users.patient.dailyprogram.presentaion

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RadioGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButton.OnCheckedChangeListener
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.databinding.DialogDoCompleteTaskBinding
import com.app.sanad.databinding.LayoutTaskBinding
import com.app.sanad.users.patient.moodTracking.presentaion.activties.PostAssessmentActivity
import com.app.sanad.util.LANGUAGE
import com.app.sanad.util.TEXT_TO_SPEECH
import com.app.sanad.util.Temp
import com.app.sanad.util.TextToSpeechUtil
import com.app.sanad.util.durationAsString
import com.app.sanad.util.log

@AndroidEntryPoint
class BehaviouralActivationFragment : BaseDailyProgramFragment(),
    SuggestedChallengesFragment.OnTaskItemClickListener {

    private var currentStatus: Boolean = true // Tracks the user's decision in the completion dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutTaskBinding.inflate(inflater, container, false)
        checkInternetConnection() // Ensures network before loading content
        setupClickListener() // Assign listeners
        textToSpeech = TextToSpeechUtil(TextToSpeech(requireActivity(), null))
        textToSpeech.debug()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Temp.stoppedAt = "BehaviouralActivationFragment" // Track fragment for analytics
    }

    /** Checks network and initializes content if connected, otherwise shows retry dialog */




    /** Initializes tasks, TTS, UI colors, and listeners */
    override fun init() {
        showDescriptionDialog(
            R.drawable.icon_descriptionww,
            getString(R.string.time_of_adventure),
            getString(R.string.time_of_adventure)
        )
        viewModel.initTasksList("behaviorActivation") // Load behavioral tasks
        if (viewModel.listOfTasks.size > 1) binding.btnRecommend.visibility = View.VISIBLE
        getTaskFromList(viewModel.status.currentIndexBehavioral!!) // Load current task
        changeColorStatus() // Update task UI based on completion
        hideSpiritualIcon(binding.constraintTask2, binding.line2)
    }

    /** Sets up TTS, navigation, task recommendation, and next button behavior */
    fun setupClickListener() {
        binding.play.setOnClickListener {
            if (textToSpeech.textToSpeech.isSpeaking) {
                log("stop")
                textToSpeech.textToSpeech.stop()
                binding.play.setImageResource(R.drawable.icon_stop_sound)
            } else {
                log("speak")
                textToSpeech.speakText(htmlText)
                binding.play.setImageResource(R.drawable.icon_play_sound)
                logEvent(TEXT_TO_SPEECH) { param(TEXT_TO_SPEECH, "true") }
            }
        }

        binding.btnNext.setOnClickListener {
            if (viewModel.status.behavioral != 1) {
                showDialogAskingForCompletion() // Ask user to confirm task completion
            } else {
                Temp.challengeCompleted = true

                navigate() // Move to next activity if already completed
            }
        }

        binding.icExit.setOnClickListener { activity?.finish() }

        binding.btnRecommend.setOnClickListener { showSuggestedChallengesFragment() }

        binding.btnPrevious.setOnClickListener { findNavController().popBackStack() }

        binding.icBack.setOnClickListener { findNavController().popBackStack() }
    }

    /** Shows suggested challenges in a bottom sheet fragment */
    private fun showSuggestedChallengesFragment() {
        val suggestedChallengesFragment = SuggestedChallengesFragment.newInstance(
            this,
            viewModel.status.currentIndexBehavioral!!,
            viewModel.listOfTasks,
            viewModel.sharedPreferences.getString(LANGUAGE)
        )
        suggestedChallengesFragment.show(
            childFragmentManager,
            SuggestedChallengesFragment::class.java.name
        )
    }

    /** Updates task progress lines and images based on completion */
    private fun changeColorStatus() {
        if (viewModel.status.educational == 1) binding.line1.setBackgroundColor(Color.parseColor("#6db7d3"))
        if (viewModel.status.behavioral == 1) binding.line2.setBackgroundColor(Color.parseColor("#6db7d3"))

        changeColorOfTaskImage(viewModel.status.educational, binding.constraintTask1, binding.imageTask1)
        changeColorOfTaskImage(viewModel.status.spiritual, binding.constraintTask2, binding.imageTask2)
        changeColorOfTaskImage(2, binding.constraintTask3, binding.imageTask3)

    }

    /** Updates behavioral status and navigates forward */
    private fun updateStatus(boolean: Boolean) {
        Temp.challengeCompleted = true

        if (boolean) updateStatusData()
        navigate()
    }

    override fun onStop() {
        super.onStop()
        Temp.challengeEngagement = durationAsString(durationAsLong())
        Temp.challengeSelected = title
    }

    /** Navigate to post-mood tracking activity and finish this fragment */
    private fun navigate() {
        Temp.completion = true
        startActivity(Intent(requireContext(), PostAssessmentActivity::class.java))
        activity?.finish()
    }

    /** Updates behavioral status flags in ViewModel */
    private fun updateStatusData() {
        viewModel.status.behavioral = 1
        viewModel.status.isDayProgramCompleted = true
        viewModel.updateCompletionRate()
    }

    /** Shows a dialog asking user to confirm task completion before proceeding */
    private fun showDialogAskingForCompletion() {
        currentStatus = true
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogDoCompleteTaskBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(true)
        val window = dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.icClose.setOnClickListener { dialog.dismiss() }
        dialogBinding.btNext.setOnClickListener {
            if (currentStatus) {
                dialog.dismiss()
                updateStatus(currentStatus)
            } else {
                showToast(getString(R.string.you_will_not_go_to_the_next_day))
            }
        }
        dialogBinding.textBackToContinue.setOnClickListener { dialog.dismiss() }

        dialogBinding.radioGroup.setOnCheckedChangeListener(
            object : OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(button: MaterialButton?, isChecked: Boolean) {}
                override fun onCheckedChanged(radioGroup: RadioGroup?, id: Int) {
                    when (id) {
                        R.id.radioButtonYes -> changeStateOfMassage(dialogBinding, true)
                        R.id.radioButtonNo -> changeStateOfMassage(dialogBinding, false)
                    }
                }
            }
        )
        dialog.show()
    }

    /** Updates dialog UI based on user's Yes/No choice */
    private fun changeStateOfMassage(binding: DialogDoCompleteTaskBinding, status: Boolean) {
        currentStatus = status
        if (status) {
            binding.textView.text = getString(R.string.well_done_you_will_get_a_star_when_you_complete_each_task_so_you_will_get_a_full_mark)
            binding.textView.setTextColor(Color.parseColor("#197ea5"))
            binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_clap))
            binding.constraintLayout.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#e7f8ff"))
        } else {
            binding.textView.text = getString(R.string.you_will_not_go_to_the_next_day)
            binding.textView.setTextColor(Color.parseColor("#d77c3f"))
            binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_danger))
            binding.constraintLayout.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#f9f2e1"))
        }
    }

    /** Handles selection from suggested challenges fragment */
    override fun onTaskItemClicked(position: Int) {
        getTaskFromList(position, )
        viewModel.status.currentIndexBehavioral = position
        viewModel.updateCurrentTaskLocally()
    }
}

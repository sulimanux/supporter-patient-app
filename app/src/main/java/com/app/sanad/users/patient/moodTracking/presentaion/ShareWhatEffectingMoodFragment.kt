/**
 * Fragment for collecting factors influencing the user's current mood.
 * Displays selectable reasons, accepts custom text input, updates ViewModel,
 * and launches the daily program when completed.
 */
package com.app.sanad.users.patient.moodTracking.presentaion

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.DialogStartProgramBinding
import com.app.sanad.databinding.FragmentShareWhatEffectingMoodBinding
import com.app.sanad.users.patient.dailyprogram.presentaion.DailyProgramActivity
import com.app.sanad.users.patient.moodTracking.data.entity.EffectingMood
import com.app.sanad.users.patient.moodTracking.data.entity.EmojiMood
import com.app.sanad.users.patient.moodTracking.presentaion.adapters.EffectingMoodAdapter
import com.app.sanad.users.patient.moodTracking.presentaion.viewmodels.MoodTrackingViewModel
import com.app.sanad.util.*

@AndroidEntryPoint
class ShareWhatEffectingMoodFragment : BaseFragment() {

    private lateinit var binding: FragmentShareWhatEffectingMoodBinding
    private val viewModel: MoodTrackingViewModel by activityViewModels()
    private lateinit var effectingAdapter: EffectingMoodAdapter

    /**
     * Inflates the layout, prepares UI, adapters, and listeners.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentShareWhatEffectingMoodBinding.inflate(inflater, container, false)
        updateUiColor(viewModel.getEmoji()!!)
        setUpRecyclerViewEffectingMood(viewModel.getEffectingMood(requireActivity()))
        setUpListener()
        return binding.root
    }

    /**
     * Tracks screen entry for analytics.
     */
    override fun onStart() {
        super.onStart()
        Temp.stoppedAt = ShareWhatEffectingMoodFragment
    }

    /**
     * Configures navigation and submit listeners.
     */
    private fun setUpListener() {
        binding.exit.setOnClickListener { activity?.finish() }
        binding.backBtn.setOnClickListener { findNavController().popBackStack() }
        binding.btnNext.setOnClickListener {
            if(! isValidToProceed()) return@setOnClickListener
            proceedWithSubmission()
        }
    }
    private fun isValidToProceed():Boolean{
        if(!isValidToStoreData()){
            logEvent(USER_SKIP_INPUT) { param(REQUIRED, ShareWhatEffectingMoodFragment) }
            showToast(getString(R.string.please_enter_what_is_affecting_your_current_mood))
            return    false
        }
        if(!isConnected()){
            showToast(getString(R.string.please_check_your_connection_and_try_again))
            return    false

        }

        return  true

    }

    private fun proceedWithSubmission(){
        val chosen = effectingAdapter.getChosenReasons()
        val extraText = binding.editText.text.toString()
        viewModel.updateCurrentTaskPreMood(chosen.toList(), extraText)
        Temp.completion = true
        showStartDailyProgram()
    }


    /**
     * Displays selectable mood-influencing factors.
     */
    private fun setUpRecyclerViewEffectingMood(list: List<EffectingMood>) {
        effectingAdapter = EffectingMoodAdapter(list)
        binding.recyclerViewEffectingMood.layoutManager =
            GridLayoutManager(requireActivity(), 3)
        binding.recyclerViewEffectingMood.adapter = effectingAdapter
    }

    /**
     * Applies mood color theme to UI elements.
     */
    private fun updateUiColor(emoji: EmojiMood) {
        binding.moodText.text = emoji.name
        binding.moodText.alpha = 1f
        binding.icon.setImageResource(emoji.emoji)
        binding.btnNext.setBackgroundResource(R.drawable.gradient_orange)
        binding.root.backgroundTintList = ColorStateList.valueOf(Color.parseColor(emoji.backgroundColor))
        binding.btnNext.backgroundTintList = ColorStateList.valueOf(Color.parseColor(emoji.buttonColor))
    }

    /**
     * Displays dialog prompting the user to start the daily program.
     */
    private fun showStartDailyProgram() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogStartProgramBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val params = attributes
            params.width = (resources.displayMetrics.widthPixels * 0.8).toInt()
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = params
        }

        dialogBinding.button.setOnClickListener {
            startActivity(Intent(requireContext(), DailyProgramActivity::class.java))
            dialog.dismiss()
            activity?.finish()
        }

        dialog.show()
    }

    /**
     * Validates that the user selected or typed a reason.
     */
    private fun isValidToStoreData() =
        effectingAdapter.getChosenReasons().isNotEmpty()
                || binding.editText.text.toString().isNotEmpty()

    /**
     * Logs engagement duration.
     */
    override fun onStop() {
        super.onStop()
    }


}

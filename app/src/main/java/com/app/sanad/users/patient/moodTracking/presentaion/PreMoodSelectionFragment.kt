/**
 * Fragment for capturing the user's pre-program mood.
 * Displays mood emojis, updates UI on selection, validates input,
 * and logs engagement before navigation.
 */
package com.app.sanad.users.patient.moodTracking.presentaion

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentPreMoodSelectionBinding
import com.app.sanad.users.patient.moodTracking.data.entity.EmojiMood
import com.app.sanad.users.patient.moodTracking.presentaion.adapters.EmojisAdapter
import com.app.sanad.users.patient.moodTracking.presentaion.adapters.OnEmojiClickListener
import com.app.sanad.users.patient.moodTracking.presentaion.viewmodels.MoodTrackingViewModel
import com.app.sanad.util.*

@AndroidEntryPoint
class PreMoodSelectionFragment : BaseFragment(), OnEmojiClickListener {

    private lateinit var binding: FragmentPreMoodSelectionBinding
    private var canClickingButton = false
    private lateinit var adapter: EmojisAdapter
    private val viewModel: MoodTrackingViewModel by activityViewModels()

    /**
     * Inflates UI and initializes listeners and emoji list.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPreMoodSelectionBinding.inflate(inflater, container, false)
        setUpListener()
        setUpRecyclerView(viewModel.getEmojisStatus(requireActivity()))
        return binding.root
    }

    /**
     * Marks current step in flow.
     */
    override fun onStart() {
        super.onStart()
        Temp.stoppedAt = PreMoodSelectionFragment
    }

    /**
     * Handles back, exit, and next actions.
     */
    private fun setUpListener() {
        binding.backBtn.setOnClickListener { activity?.finish() }
        binding.exit.setOnClickListener { activity?.finish() }

        binding.btnNext.setOnClickListener {
            if (canClickingButton) {
                findNavController()
                    .navigate(R.id.action_preMoodSelectionFragment2_to_shareWhatEffectingMoodFragment2)
            } else {
                logEvent(USER_SKIP_INPUT) { param(REQUIRED, "pre_mood_selection") }
                showToast(getString(R.string.you_have_not_select_mood_yet))
            }
        }
    }

    /**
     * Sets up grid view for mood emojis.
     */
    private fun setUpRecyclerView(emojisStatus: List<EmojiMood>) {
        adapter = EmojisAdapter(emojisStatus, this)
        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 3)
        binding.recyclerView.adapter = adapter
    }

    /**
     * Handles emoji selection and updates ViewModel.
     */
    override fun onEmojiClicked(emoji: EmojiMood, index: Int) {
        viewModel.setEmoji(emoji)
        viewModel.setPreMoodIndex(index)
        updateUiColor(emoji)
    }

    /**
     * Updates UI colors and content based on selected mood.
     */
    private fun updateUiColor(emoji: EmojiMood) {
        canClickingButton = true
        binding.moodText.text = emoji.name
        binding.moodText.alpha = 1.0f
        binding.icon.setImageResource(emoji.emoji)
        binding.btnNext.setBackgroundResource(R.drawable.gradient_orange)
        binding.root.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(emoji.backgroundColor))
        binding.btnNext.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(emoji.buttonColor))
    }

    /**
     * Logs time spent and resets click state.
     */
    override fun onStop() {
        super.onStop()
        canClickingButton = false
    }
}

/**
 * Fragment that collects the user's post-task mood,
 * updates UI based on selected emoji, logs engagement,
 * and navigates to the suggestions screen after selection.
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
import com.app.sanad.base.BaseFragment
import com.app.sanad.R
import com.app.sanad.databinding.FragmentPostAssessmentBinding
import com.app.sanad.users.patient.moodTracking.data.entity.EmojiMood
import com.app.sanad.users.patient.moodTracking.presentaion.adapters.EmojisAdapter
import com.app.sanad.users.patient.moodTracking.presentaion.adapters.OnEmojiClickListener
import com.app.sanad.users.patient.moodTracking.presentaion.viewmodels.MoodTrackingViewModel
import com.app.sanad.util.*

@AndroidEntryPoint
class PostAssessmentFragment : BaseFragment(), OnEmojiClickListener {

    private val viewModel: MoodTrackingViewModel by activityViewModels()
    private lateinit var adapter: EmojisAdapter
    private var canClickingButton = false
    private lateinit var binding: FragmentPostAssessmentBinding

    /**
     * Inflates UI and initializes listeners and mood list.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPostAssessmentBinding.inflate(inflater, container, false)
        setUpListener()
        setUpMoodsRecyclerView(viewModel.getEmojisStatus(requireActivity()))
        return binding.root
    }

    /**
     * Marks current flow step for state tracking.
     */
    override fun onStart() {
        super.onStart()
        Temp.stoppedAt = POST_MOOD_TRACKING
    }



    /**
     * Sets navigation and exit button listeners.
     */
    private fun setUpListener() {
        binding.backBtn.setOnClickListener { findNavController().popBackStack() }

        binding.exit.setOnClickListener { activity?.finish() }

        binding.btnNext.setOnClickListener {
            if (canClickingButton) {
                Temp.isPostAssessmentCompleted = true
                findNavController()
                    .navigate(R.id.action_postDailyProgramFragment_to_suggestionsFragment)
            } else {
                logEvent(USER_SKIP_INPUT) { param(REQUIRED, "post_mood_selection") }
                showToast(getString(R.string.you_have_not_select_mood_yet))
            }
        }
    }

    /**
     * Initializes emoji grid list.
     */
    private fun setUpMoodsRecyclerView(emojisStatus: List<EmojiMood>) {
        adapter = EmojisAdapter(emojisStatus, this)
        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 3)
        binding.recyclerView.adapter = adapter
    }

    /**
     * Handles emoji click event and updates selected mood.
     */
    override fun onEmojiClicked(emoji: EmojiMood, index: Int) {
        viewModel.setEmoji(emoji)
        viewModel.setPostMoodIndex(index)
        updateUiColor(emoji)
    }

    /**
     * Updates UI theme based on selected mood.
     */
    private fun updateUiColor(emoji: EmojiMood) {
        canClickingButton = true
        binding.icon1.alpha = 0.0f
        binding.moodText.text = emoji.name
        binding.moodText.alpha = 1.0f
        binding.icon.setImageResource(emoji.emoji)
        binding.dynamicContainer.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(emoji.backgroundColor))
        binding.container.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(emoji.backgroundColor))
        binding.btnNext.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(emoji.buttonColor))
    }

    /**
     * Logs post-mood assessment engagement and completion.
     */
    override fun onStop() {
        super.onStop()
        canClickingButton = false
        Temp.postAssessmentEngagement = durationAsString(durationAsLong())
    }
}

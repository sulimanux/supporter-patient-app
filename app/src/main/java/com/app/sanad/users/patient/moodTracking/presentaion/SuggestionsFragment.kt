/**
 * Screen that displays mood-based suggestions to the user after selecting their post-mood.
 * Shows tailored tips, allows user to continue to mood comparison, and updates mood record.
 */
package com.app.sanad.users.patient.moodTracking.presentaion

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentSuggestionsBinding
import com.app.sanad.users.patient.moodTracking.data.entity.EmojiMood
import com.app.sanad.users.patient.moodTracking.presentaion.adapters.SuggestionsAdapter
import com.app.sanad.users.patient.moodTracking.presentaion.viewmodels.MoodTrackingViewModel
import com.app.sanad.util.*

@AndroidEntryPoint
class SuggestionsFragment : BaseFragment() {

    private lateinit var binding: FragmentSuggestionsBinding
    private val viewModel: MoodTrackingViewModel by activityViewModels()

    /**
     * Inflates view and prepares UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSuggestionsBinding.inflate(inflater, container, false)
        setUpUi(viewModel.getEmoji()!!)
        setUpListeners()
        return binding.root
    }

    /**
     * Analytics start marker.
     */
    override fun onStart() {
        super.onStart()
        Temp.stoppedAt = SUGGESTIONS
    }

    /**
     * Logs engagement time.
     */
    override fun onStop() {
        super.onStop()
        Temp.recommendationsEngagement = durationAsString(durationAsLong())
    }

    /**
     * Navigation and action listeners.
     */
    private fun setUpListeners() {
        binding.backBtn.setOnClickListener { findNavController().popBackStack() }
        binding.exit.setOnClickListener { activity?.finish() }

        binding.btnNext.setOnClickListener {
            viewModel.updateCurrentDayPostMood()
            findNavController().navigate(
                R.id.action_suggestionsFragment_to_compareResultsFragment
            )
        }
    }

    /**
     * Populates UI and colors using selected mood data.
     */
    private fun setUpUi(emoji: EmojiMood) {
        setUpSuggestionsRecyclerView(emoji)
        binding.icon1.setImageResource(emoji.emoji)
        binding.title.text = emoji.title
        binding.subTitle.text = emoji.subTitle
        binding.suggestion.text = emoji.titleSuggestion

        binding.title.setTextColor(Color.parseColor(emoji.buttonColor))
        binding.container.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(emoji.buttonColor))
        binding.btnNext.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(emoji.buttonColor))

        binding.root.setBackgroundColor(Color.parseColor(emoji.backgroundColor))
        binding.dynamicContainer.backgroundTintList =
            ColorStateList.valueOf(resources.getColor(R.color.white))
    }

    /**
     * Binds suggestions list to RecyclerView.
     */
    private fun setUpSuggestionsRecyclerView(emoji: EmojiMood) {
        binding.recyclerView.adapter = SuggestionsAdapter(emoji)
    }
}

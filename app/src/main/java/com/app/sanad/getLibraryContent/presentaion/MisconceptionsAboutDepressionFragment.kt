package com.app.sanad.getLibraryContent.presentaion

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentMisconceptionsAboutDepressionBinding
import com.app.sanad.getLibraryContent.data.DepressionMisconception
import com.app.sanad.util.TEXT_TO_SPEECH
import com.app.sanad.util.TextToSpeechUtil
import com.app.sanad.util.log

// Screen that displays depression misconceptions using ViewPager
@AndroidEntryPoint
class MisconceptionsAboutDepressionFragment :
    BaseFragment(),
    MisconceptionAdapter.PageListener {

    // Text-to-speech handler
    lateinit var textToSpeech: TextToSpeechUtil

    // ViewBinding reference
    private lateinit var binding: FragmentMisconceptionsAboutDepressionBinding

    // ViewModel for accessing misconceptions data
    private val viewModel: LibraryViewModel by viewModels()

    // ViewPager adapter
    private lateinit var adapter: MisconceptionAdapter

    // Inflate layout and initialize UI
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentMisconceptionsAboutDepressionBinding.inflate(inflater, container, false)

        // Setup ViewPager with misconceptions data
        setUpViewPager(
            viewModel.depressionRepo.listMisconceptions(requireActivity())
        )

        // Initialize text-to-speech
        textToSpeech =
            TextToSpeechUtil(TextToSpeech(requireActivity(), null))

        setUpListener()
        return binding.root
    }

    // Setup navigation and control buttons
    private fun setUpListener() {

        // Navigate back
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Finish misconceptions flow
        binding.finish.setOnClickListener {
            findNavController().popBackStack()
        }

        // Navigate to next page
        binding.next.setOnClickListener {
            binding.viewPager.currentItem += 1
        }

        // Navigate to previous page
        binding.back.setOnClickListener {
            binding.viewPager.currentItem -= 1
        }
    }

    // Setup ViewPager adapter
    private fun setUpViewPager(data: List<DepressionMisconception>) {
        adapter = MisconceptionAdapter(data, requireActivity(), this)
        binding.viewPager.adapter = adapter
        registerCallback()
    }

    // Handle page changes
    private fun registerCallback() {
        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    // Stop any playing audio on page change
                    onStopClicked()

                    // Update button visibility based on position
                    when (position) {
                        0 ->
                            changeVisibility(
                                View.VISIBLE,
                                View.GONE,
                                View.GONE
                            )

                        in 1..9 ->
                            changeVisibility(
                                View.VISIBLE,
                                View.VISIBLE,
                                View.GONE
                            )

                        10 ->
                            changeVisibility(
                                View.GONE,
                                View.VISIBLE,
                                View.VISIBLE
                            )
                    }
                }
            }
        )
    }

    // Control button visibility
    private fun changeVisibility(
        nextVisibility: Int,
        backVisibility: Int,
        finishVisibility: Int
    ) {
        binding.next.visibility = nextVisibility
        binding.back.visibility = backVisibility
        binding.finish.visibility = finishVisibility
    }

    // Play text-to-speech for selected item
    override fun onPlayIconClicked(position: Int, text: String) {
        textToSpeech.speakText(text)
        logEvent(TEXT_TO_SPEECH) {
            param(TEXT_TO_SPEECH, "true")
        }
    }

    // Stop text-to-speech playback
    override fun onStopClicked() {
        if (textToSpeech.textToSpeech.isSpeaking) {
            textToSpeech.textToSpeech.stop()
        }
        adapter.playingIndex = null
        adapter.notifyDataSetChanged()
    }
}

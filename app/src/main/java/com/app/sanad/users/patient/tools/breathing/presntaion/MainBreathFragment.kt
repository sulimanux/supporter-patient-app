package com.app.sanad.users.patient.tools.breathing.presntaion

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.databinding.FragmentMainBreathBinding
import androidx.fragment.app.activityViewModels
import com.app.sanad.base.BaseFragment
import com.app.sanad.util.log

@AndroidEntryPoint
class MainBreathFragment : BaseFragment() {

    // Shared ViewModel for breathing exercise logic
    private val viewModel: BreathViewModel by activityViewModels()

    // ViewBinding reference
    private lateinit var binding: FragmentMainBreathBinding

    // MediaPlayer for tick sound
    private var mediaPlayer: MediaPlayer? = null

    /**
     * Inflates layout and initializes views, listeners, and observers
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBreathBinding.inflate(inflater, container, false)

        initializeViews()
        setupClickListener()
        observeViewModel()

        return binding.root
    }

    /**
     * Initializes UI components with default values
     */
    fun initializeViews() {


        if (viewModel.soundId == 0) {
            binding.sound.setImageResource(R.drawable.no_musiz)
        }

        binding.remainingTimeFormat.text =  0.toString()
        binding.remainingTime.text = getString(R.string.remaining_time_format)


    }

    /**
     * Sets up click listeners for buttons and interactive UI elements
     */
    fun setupClickListener() {

        // Open duration selection dialog
        binding.chooseDuration.setOnClickListener {
            ChoosingDurationDialog().show(childFragmentManager, "ChoosingDurationDialog")
        }

        // Navigate back
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Open sound selection dialog
        binding.sound.setOnClickListener {
            ChoosingSoundDialog().show(childFragmentManager, "ChoosingSoundDialog")
        }

        // Start exercise
        binding.startingButton.setOnClickListener {
            viewModel.onStartButtonClicked()
        }

        // Finish exercise, clear data, stop sound, navigate back
        binding.finishExercise.setOnClickListener {
            viewModel.clearData()
            stopTickSound()
            findNavController().popBackStack()
        }
    }

    /**
     * Observes all LiveData from ViewModel and updates UI accordingly
     */
    private fun observeViewModel() {

        // Update duration text
        viewModel.textDuration.observe(viewLifecycleOwner) {
            binding.textDuration.text = it
        } //100
 // 59000 / 60000 * 100 = 9.6
        // Update progress bar based on current state
        viewModel.progressState.observe(viewLifecycleOwner) { progress ->
            val selectedDuration = viewModel.getSelectedDurationInMillis().toDouble() // 600000
            progress?.let {
                binding.progressBar.progress =
                    it.toDouble().div(selectedDuration).times(100).toInt()
            }
        }

        // Update sound icon and play tick sound when sound changes
        viewModel.changeSound.observe(viewLifecycleOwner) {
            if (viewModel.soundId == 0) {
                binding.sound.setImageResource(R.drawable.no_musiz)
            } else {
                binding.sound.setImageResource(R.drawable.musiz)
            }
            playTickSound()
        }

        // Show repeat exercise dialog if needed
        viewModel.showDialog.observe(viewLifecycleOwner) {
            it?.let { shouldShow ->
                if (shouldShow) {
                    showRepeatedExerciseDialog()
                    viewModel.restShowDialog()
                }
            }
        }

        // Update remaining time UI and play/stop tick sound
        viewModel.remainingTime.observe(viewLifecycleOwner) { remaining ->
            remaining?.let {
                when (it) {
                    59 -> { log("continue $it"); playTickSound() }
                    15 -> { log("stop $it"); stopTickSound() }
                    0 -> { log("stop $it"); stopTickSound() }
                }

                binding.remainingTimeFormat.text =  it.toString()
                binding.remainingTime.text = getString(R.string.remaining_time_format)

                updateUiBaseCurrentPhase(it)
            }
        }

        // Reset progress bar when instructed
        viewModel.resetProgress.observe(viewLifecycleOwner) { shouldReset ->
            shouldReset?.let {
                if (it) {
                    resetProgress()
                    viewModel.resetRestProgress()
                }
            }
        }

        // Update start button UI based on timer running state
        viewModel.isTimerRunning.observe(viewLifecycleOwner) { isRunning ->
            isRunning?.let { updateStartingButtonUi(it) }
        }
    }

    /**
     * Shows dialog prompting user to repeat exercise
     */
    private fun showRepeatedExerciseDialog() {
        showTemporallyDialog(
            getString(R.string.would_you_like_to_repeat_the_exercise_again),
            getString(R.string.this_procedure_will_delete_the_current_exercise_data_and_replace_it_with_the_new_exercise_data),
            R.drawable.ic_refresh,
            getString(R.string.starting_over)
        ) {
            viewModel.resetIsTimerRunning()
            viewModel.onStartButtonClicked()
        }
    }

    /**
     * Updates start button UI (text and icon) based on timer state
     */
    private fun updateStartingButtonUi(isRunning: Boolean) {
        val text = if (isRunning) getString(R.string.starting_over) else getString(R.string.click_to_start)
        val imageResource = if (isRunning) R.drawable.ic_sync else R.drawable.ic_play
        binding.textView.text = text
        binding.imageView.setImageResource(imageResource)
    }

    /**
     * Resets progress UI to default state
     */
    private fun resetProgress() {
        binding.imageViewFace.setImageResource(R.drawable.image_face0)
        binding.progressBar.progress = 100
        binding.textInstructions.text = getString(R.string.click_on_the_box_below_to_get_started)
        viewModel.resetIsTimerRunning()
    }

    /**
     * Updates UI based on current phase of breathing exercise
     */
    private fun updateUiBaseCurrentPhase(remainingTime: Int) {
        val listPhases =  listOf(59, 44, 29, 14, 0)

        when (remainingTime) {
            listPhases[0] -> {
                binding.imageViewFace.setImageResource(R.drawable.image_face1)
                binding.textInstructions.text = getString(R.string.inhale, 15)
            }
            listPhases[1] -> {
                binding.imageViewFace.setImageResource(R.drawable.image_face2)
                binding.textInstructions.text = getString(R.string.hold_air, 15)
            }
            listPhases[2] -> {
                binding.imageViewFace.setImageResource(R.drawable.image_face3)
                binding.textInstructions.text = getString(R.string.exhale_slowly, 15)
            }
            listPhases[3] -> {
                binding.imageViewFace.setImageResource(R.drawable.image_face0)
                binding.textInstructions.text = getString(R.string.take_rest, 15)
            }
        }
    }

    /**
     * Release MediaPlayer and reset progress when fragment is destroyed
     */
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        binding.progressBar.progress = 100
        viewModel.clearData()
    }

    /**
     * Plays the tick sound if a valid sound is selected
     */
    private fun playTickSound() {
        if (viewModel.soundId == 0) {
            stopTickSound()
            return
        }
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, viewModel.soundId)
        mediaPlayer?.start()
    }

    /**
     * Stops the tick sound and releases MediaPlayer
     */
    private fun stopTickSound() {
        log("stopTickSound")
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

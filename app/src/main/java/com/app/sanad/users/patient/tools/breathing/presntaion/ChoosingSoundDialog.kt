package com.app.sanad.users.patient.tools.breathing.presntaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.app.sanad.databinding.ChoosingSoundDialogBinding
import com.app.sanad.users.patient.tools.breathing.data.entity.Sound
import kotlin.getValue

/**
 * DialogFragment to allow the user to choose a breathing exercise sound
 */
class ChoosingSoundDialog : DialogFragment(), OnItemSoundClicked {

    // Shared ViewModel for breathing exercise
    private val viewModel: BreathViewModel by activityViewModels()

    // ViewBinding reference
    private lateinit var binding: ChoosingSoundDialogBinding

    // Adapter for displaying available sounds
    private lateinit var soundsAdapter: SoundsAdapter

    /**
     * Inflates layout, sets up RecyclerView and click listeners
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ChoosingSoundDialogBinding.inflate(inflater, container, false)

        // Setup RecyclerView with list of sounds
        setSoundsRecycler(viewModel.listOfSounds(requireActivity()))

        // Setup back button and confirm button listeners
        setUpListeners()

        return binding.root
    }

    /**
     * Adjust dialog width and margins when dialog starts
     */
    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val params = dialog?.window?.attributes
        params?.horizontalMargin = 0.05f
        dialog?.window?.attributes = params
    }

    /**
     * Sets click listeners for back and confirm buttons
     */
    private fun setUpListeners() {
        // Dismiss dialog on back icon click
        binding.icBack.setOnClickListener {
            dismiss()
        }

        // Confirm selection and notify ViewModel
        binding.button.setOnClickListener {
            viewModel.setChangeSound(true)
            dismiss()
        }
    }

    /**
     * Initializes RecyclerView with sounds
     */
    private fun setSoundsRecycler(sounds: List<Sound>) {
        soundsAdapter = SoundsAdapter(sounds, requireActivity(), this)
        binding.recycler.adapter = soundsAdapter
        binding.recycler.layoutManager = GridLayoutManager(context, 2) // 2-column grid
    }

    /**
     * Callback from SoundsAdapter when a sound is clicked
     */
    override fun onItemClicked(soundId: Int?) {
        binding.button.isEnabled = true        // Enable confirm button
        viewModel.soundId = soundId!!          // Update selected sound in ViewModel
    }
}

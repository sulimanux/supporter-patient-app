package com.app.sanad.users.patient.tools.breathing.presntaion

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
import com.app.sanad.users.patient.tools.breathing.data.entity.Sound
import com.app.sanad.databinding.FragmentChooseSuondBinding
import kotlin.getValue

@AndroidEntryPoint
class ChoosingSoundFragment : BaseFragment(), OnItemSoundClicked {

    // Shared ViewModel for breathing exercise
    private val viewModel: BreathViewModel by activityViewModels()

    // ViewBinding reference
    private lateinit var binding: FragmentChooseSuondBinding

    // Adapter for displaying available sounds
    private lateinit var soundsAdapter: SoundsAdapter

    /**
     * Inflates layout, initializes RecyclerView and listeners
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChooseSuondBinding.inflate(inflater, container, false)

        // Setup RecyclerView with list of sounds from ViewModel
        setSoundsRecycler(viewModel.listOfSounds(requireActivity()))

        // Setup button and back navigation listeners
        setUpListeners()

        return binding.root
    }

    /**
     * Sets click listeners for back and confirm buttons
     */
    private fun setUpListeners() {
        // Navigate back
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Navigate to main breathing fragment after selecting a sound
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_choosingSoundFragment_to_mainBreathFragment)
        }
    }

    /**
     * Initializes RecyclerView with sounds and adapter
     *
     * @param sounds List of Sound objects
     */
    private fun setSoundsRecycler(sounds: List<Sound>) {
        soundsAdapter = SoundsAdapter(sounds, requireActivity(), this)
        binding.recycler.adapter = soundsAdapter
        binding.recycler.layoutManager = GridLayoutManager(context, 2) // 2-column grid
    }

    /**
     * Callback from SoundsAdapter when a sound is clicked
     *
     * @param soundId ID of the selected sound
     */
    override fun onItemClicked(soundId: Int?) {
        binding.button.isEnabled = true      // Enable confirm button
        viewModel.soundId = soundId!!        // Update selected sound in ViewModel
    }
}

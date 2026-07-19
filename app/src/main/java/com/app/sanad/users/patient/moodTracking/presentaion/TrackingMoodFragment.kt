/**
 * Screen that displays historical mood tracking data for a user.
 * Fetches mood records remotely, handles offline state, and presents tracked moods in a list.
 */
package com.app.sanad.users.patient.moodTracking.presentaion

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentTrackingMoodBinding
import com.app.sanad.users.patient.moodTracking.data.entity.DayMoodTracking
import com.app.sanad.users.patient.moodTracking.presentaion.adapters.TrackingMoodAdapter
import com.app.sanad.users.patient.moodTracking.presentaion.viewmodels.MoodTrackingViewModel

@AndroidEntryPoint
class TrackingMoodFragment : BaseFragment() {

    private val viewModel: MoodTrackingViewModel by activityViewModels()
    private lateinit var binding: FragmentTrackingMoodBinding
    private lateinit var adapter: TrackingMoodAdapter
    private var userId: String? = null

    /**
     * Initializes view, retrieves user ID, and checks internet connectivity.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTrackingMoodBinding.inflate(inflater, container, false)
        val args: TrackingMoodFragmentArgs by navArgs()
        userId = args.userId
        checkInternetConnection()
        setUpListener()
        return binding.root
    }

    /**
     * Validates internet, fetches remote data, or displays retry dialog.
     */
    private fun checkInternetConnection() {
        if (isConnected()) {
            showProgressDialog()
            viewModel.retrieveTracingListRemotely(userId!!)
            observeViewModel()

        } else {
         binding.noInternetLayout.swipeRefresh.visibility = View.VISIBLE
        }
    }



    /**
     * Handles navigation actions.
     */
    private fun setUpListener() {
        binding.back.setOnClickListener { findNavController().popBackStack() }
        binding.noInternetLayout.tryAgainBt.setOnClickListener {
            binding.noInternetLayout.swipeRefresh.visibility = View.GONE
            checkInternetConnection()
        }
    }


    /**
     * Observes mood data and updates UI accordingly.
     */
    private fun observeViewModel() {
        viewModel.trackingList.observe(viewLifecycleOwner) { list ->
            if (list.isNullOrEmpty()) {
                binding.noTracking.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                setUpRecyclerView(list)
                binding.noTracking.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
            dismissProgressDialog()
        }
    }

    /**
     * Configures RecyclerView with mood tracking adapter.
     */
    private fun setUpRecyclerView(data: List<DayMoodTracking>) {
        adapter = TrackingMoodAdapter(
            data,
            viewModel.getEffectingMood(requireActivity()),
            viewModel.getEmojisStatus(requireContext())
        )
        binding.recyclerView.adapter = adapter
    }
}

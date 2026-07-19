package com.app.sanad.users.patient.tools.gratitude.presentaion

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.users.patient.tools.gratitude.presentaion.GratitudeAdapter
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentGratitudeListBinding
import com.app.sanad.util.getGratitudeQuestionsList
import com.app.sanad.util.log
import kotlin.getValue

/**
 * Fragment responsible for displaying a list of gratitude entries.
 * Handles internet state, navigation, and observing gratitude data.
 */
class GratitudeListFragment : BaseFragment() {

    // ViewBinding for accessing layout views safely
    private lateinit var binding: FragmentGratitudeListBinding

    // ViewModel scoped to this Fragment
    private val viewModel: GratitudeViewModel by viewModels()

    /**
     * Inflates the layout and initializes UI logic
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate layout using ViewBinding
        binding = FragmentGratitudeListBinding.inflate(inflater, container, false)

        // Check network status before loading data
        checkInternetConnection()

        // Setup click listeners for UI interactions
        setupClickListener()

        return binding.root
    }

    /**
     * Checks whether the device is connected to the internet
     * and initializes views accordingly
     */
    private fun checkInternetConnection() {
        if (isConnected()) {
            initializeViews()
            observeViewModel()
        } else {
            // Show no-internet UI when offline
            binding.noInternetLayout.swipeRefresh.visibility = View.VISIBLE
        }
    }

    /**
     * Sets up all click listeners in the fragment
     */
    private fun setupClickListener() {

        // Navigate to Add Gratitude screen
        binding.btnAddGratitude.setOnClickListener {
            findNavController().navigate(
                R.id.action_gratitudeListFragment_to_gratitudeFragment
            )
        }

        // Navigate back in the stack
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Retry internet connection
        binding.noInternetLayout.tryAgainBt.setOnClickListener {
            binding.noInternetLayout.swipeRefresh.visibility = View.GONE
            checkInternetConnection()
        }
    }

    /**
     * Initializes data loading and shows progress dialog
     */
    fun initializeViews() {
        showProgressDialog()
        viewModel.retrieveGratitudeListRemotely()
    }

    /**
     * Observes gratitude list data from ViewModel
     * and updates UI accordingly
     */
    private fun observeViewModel() {
        viewModel.gratitudeList.observe(viewLifecycleOwner) {

            // Debug log for received data
            log(it.toString() + "fsadfdsfad")

            if (it.isNotEmpty()) {
                // Setup RecyclerView adapter with gratitude data
                val adapter = GratitudeAdapter(
                    it,
                    getGratitudeQuestionsList(requireActivity())
                )
                binding.recyclerViewGratitude.adapter = adapter
                binding.gratitudeList.visibility = View.VISIBLE
            } else {
                // Show empty state when no data exists
                showNoItemsView()
            }

            // Hide progress dialog after data load
            dismissProgressDialog()
        }
    }

    /**
     * Displays "no items" UI state
     */
    private fun showNoItemsView() {
        binding.gratitudeList.visibility = View.GONE
        binding.noItems.visibility = View.VISIBLE
    }
}

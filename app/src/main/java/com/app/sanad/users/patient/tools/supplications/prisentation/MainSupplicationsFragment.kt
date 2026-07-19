// Package responsible for patient supplications presentation layer
package com.app.sanad.users.patient.tools.supplications.prisentation

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentMainSupplicationsBinding
import com.app.sanad.interfaces.ItemSupplicationClicked
import com.app.sanad.model.Supplication
import com.app.sanad.util.log
import kotlin.getValue

/**
 * Main screen that displays:
 * - Suggested supplications
 * - User supplications
 * Handles navigation and item selection
 */
@AndroidEntryPoint
class MainSupplicationsFragment : BaseFragment(),
    ItemSupplicationClicked {

    // Shared ViewModel with other supplication screens
    private val viewModel: SupplicationViewModel by activityViewModels()

    // ViewBinding for fragment layout
    private lateinit var binding: FragmentMainSupplicationsBinding

    /**
     * Inflates the fragment layout and checks internet connectivity
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainSupplicationsBinding.inflate(inflater, container, false)
        checkInternetConnection()
        setupClickListener()
        return binding.root
    }

    /**
     * Checks network availability before loading data
     */
    private fun checkInternetConnection() {
        if (isConnected()) {
            binding.noInternetLayout.swipeRefresh.visibility = View.GONE

            showProgressDialog()
            observeViewModel()

        } else {
            binding.noInternetLayout.swipeRefresh.visibility = View.VISIBLE
            binding.fab.visibility = View.GONE
            binding.constraintData.visibility = View.GONE
        }
    }



    /**
     * Sets up all UI click listeners
     */
    fun setupClickListener() {
        // Retry on no internet
        binding.noInternetLayout.tryAgainBt.setOnClickListener {
            binding.noInternetLayout.swipeRefresh.visibility = View.GONE
            checkInternetConnection()
        }
        // Floating button opens dialog to add new supplication
        binding.fab.setOnClickListener {
            AddSupplicationDialog().show(parentFragmentManager, "AddSupplicationDialog")
        }

        // Navigate back to previous screen
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        // Navigate to full list of user supplications
        binding.textShowAll.setOnClickListener {
            findNavController()
                .navigate(R.id.action_mainSupplicationsFragment_to_userSupplicationsFragment)
        }
    }

    /**
     * Sets up RecyclerView for suggested supplications
     */
    private fun setupSuggestedSupplicationRecyclerView(
        suggestedSupplication: List<Supplication>
    ) {
        val adapterSuggestedSupplication =
            SuggestedSupplicationAdapter(suggestedSupplication, this)
        binding.suggestedRecyclerView.adapter = adapterSuggestedSupplication
    }

    /**
     * Sets up RecyclerView for user supplications
     * Handles empty state UI
     */
    private fun setupUserSupplicationRecyclerView(
        userSupplication: List<Supplication>
    ) {
        if (userSupplication.isNotEmpty()) {
            val adapterUserSupplication =
                UserSupplicationAdapter(userSupplication, this)
            binding.userSupplicationRecyclerView.adapter = adapterUserSupplication
            binding.textNoItems.visibility = View.GONE
            binding.showUserSupplications.visibility = View.VISIBLE
        } else {
            binding.showUserSupplications.visibility = View.GONE
            binding.textNoItems.visibility = View.VISIBLE
        }
    }

    /**
     * Observes LiveData from ViewModel and updates UI
     */
    private fun observeViewModel() {

        // Observe user's supplications
        viewModel.userSupplications.observe(viewLifecycleOwner) {
            log("userSupplications ${it.size}")
            dismissProgressDialog()

            setupUserSupplicationRecyclerView(it)
            binding.constraintData.visibility = View.VISIBLE
        }

        // Observe suggested supplications
        viewModel.suggestedSupplication.observe(viewLifecycleOwner) {
            log("suggestedSupplication ${it.size}")
            dismissProgressDialog()

            setupSuggestedSupplicationRecyclerView(it)
            binding.constraintData.visibility = View.VISIBLE
            binding.suggestedSupplications.visibility = View.VISIBLE
            binding.fab.visibility = View.VISIBLE
        }
    }

    /**
     * Handles click on any supplication item
     */
    override fun onItemClicked(view: View, supplication: Supplication) {
        viewModel.selectedSupplication = supplication
        findNavController()
            .navigate(R.id.action_mainSupplicationsFragment_to_supplicationsFragment)
    }

    override fun onEditClicked(supplication: Supplication) {
        val dialog = AddSupplicationDialog()
        val bundle = Bundle()
        bundle.putParcelable("supplication", supplication)
        dialog.arguments = bundle
        dialog.show(parentFragmentManager, "AddSupplicationDialog")
    }

    override fun onDeleteClicked(supplication: Supplication) {
        viewModel.deleteUserSupplication(supplication.id!!)
    }
}

// Package for supplications presentation layer
package com.app.sanad.users.patient.tools.supplications.prisentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentUserSupplicationsBinding
import com.app.sanad.interfaces.ItemSupplicationClicked
import com.app.sanad.model.Supplication
import kotlin.getValue

/**
 * Fragment that displays all user-created supplications
 * and provides actions like share, modify, and delete.
 */
@AndroidEntryPoint
class UserSupplicationsFragment : BaseFragment(),
    ItemSupplicationClicked {

    // Shared ViewModel for supplications
    private val viewModel: SupplicationViewModel by activityViewModels()

    // ViewBinding for fragment layout
    private lateinit var binding: FragmentUserSupplicationsBinding

    /**
     * Inflates layout and initializes listeners and observers
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserSupplicationsBinding.inflate(inflater, container, false)
        setupClickListener()
        observeViewModel()
        return binding.root
    }

    /**
     * Sets up click listeners for UI elements
     */
    private fun setupClickListener() {

        // Open dialog to add a new supplication
        binding.fab.setOnClickListener {
            AddSupplicationDialog().show(parentFragmentManager, "AddSupplicationDialog")
        }

        // Navigate back
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * Initializes RecyclerView for user supplications
     */
    private fun setupUserSupplicationRecyclerView(
        userSupplication: List<Supplication>
    ) {
        val adapterUserSupplication =
            UserSupplicationAdapter(userSupplication, this)
        binding.userSupplicationRecyclerView.adapter = adapterUserSupplication
    }

    /**
     * Observes user supplications from ViewModel
     */
    private fun observeViewModel() {
        viewModel.userSupplications.observe(viewLifecycleOwner) {
            setupUserSupplicationRecyclerView(it)
        }
    }

    /**
     * Handles click on supplication item
     */
    override fun onItemClicked(view: View, supplication: Supplication) {
        viewModel.selectedSupplication = supplication
        findNavController()
            .navigate(R.id.action_userSupplicationsFragment_to_supplicationsFragment)
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

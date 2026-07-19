package com.app.sanad.users.patient.points.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentMyPointsBinding
import com.app.sanad.util.log

class MyPointsFragment : BaseFragment() {

    // ViewBinding reference
    private lateinit var binding: FragmentMyPointsBinding

    // Adapter for the points grid
    private lateinit var adapterPoints: AdapterPoints

    /**
     * Inflates layout, retrieves arguments, and initializes RecyclerView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyPointsBinding.inflate(inflater, container, false)

        // Retrieve navigation arguments
        val args: MyPointsFragmentArgs by navArgs()
        log(" MyPointsFragment currentDay => ${args.currentDay}")

        val currentDay = args.currentDay

        // Setup points RecyclerView
        setUpRecycleriew(currentDay)

        // Handle back navigation
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    /**
     * Sets up the RecyclerView with a grid layout and points adapter
     *
     * @param i the number of points / current day value to display
     */
    private fun setUpRecycleriew(i: Int) {
        adapterPoints = AdapterPoints(i)

        // Use a 3-column grid layout
        val gridLayoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.layoutManager = gridLayoutManager

        // Assign adapter to RecyclerView
        binding.recyclerView.adapter = adapterPoints
    }
}

package com.app.sanad.getLibraryContent.presentaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentLibraryContentBinding
import com.app.sanad.getLibraryContent.data.LibraryContent
import com.app.sanad.interfaces.OnItemLibraryContentClicked
import com.app.sanad.util.ARTICLE
import com.app.sanad.util.log

// Main screen displaying library content sections
class LibraryContentFragment : BaseFragment(),
    OnItemLibraryContentClicked {

    // Shared ViewModel for library content
    private val viewModel: LibraryViewModel by activityViewModels()

    // ViewBinding reference
    private lateinit var binding: FragmentLibraryContentBinding

    // Inflate layout and start flow
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryContentBinding.inflate(inflater, container, false)
        checkInternetConnection()
        setupClickListener()
        return binding.root
    }

    // Check network before loading content
    private fun checkInternetConnection() {
        log("checkInternetConnection")
        if (isConnected()) {
            initializeViews()
        } else {
            showNoInternetDialog()
        }
    }

    // Show no-internet UI
    private fun showNoInternetDialog() {
        dismissProgressDialog()
        binding.noInternetLayout.swipeRefresh.visibility = View.VISIBLE
    }

    // Fetch data and observe results
    private fun initializeViews() {
        binding.noInternetLayout.swipeRefresh.visibility = View.GONE
        showProgressDialog()

        viewModel.retrieveLibraryContent()
        observeViewModel()
    }

    // Retry loading data
    private fun refreshPage() {
        binding.noInternetLayout.swipeRefresh.visibility = View.GONE
        showProgressDialog()
        checkInternetConnection()
    }

    // Setup all click listeners
    private fun setupClickListener() {

        // Retry on no internet
        binding.noInternetLayout.tryAgainBt.setOnClickListener {
            refreshPage()
        }

        // Exit app
        binding.icBack.setOnClickListener {
            activity?.finish()
        }

        // Navigate to misconceptions screen
        binding.misconceptionDepression.setOnClickListener {
            findNavController()
                .navigate(R.id.action_libraryContentFragment_to_misconceptionsAboutDepressionFragment)
        }

        // Show all customized content
        binding.showAllCustomized.setOnClickListener {
            navigateToCustomizedContent(
                viewModel.mLibraryContentCustomized.toTypedArray(),
                getString(R.string.customized_for_you)
            )
        }

        // Show all common content in bottom sheet
        binding.showAllCommon.setOnClickListener {
            CommonContentFragment().show(
                childFragmentManager,
                CommonContentFragment::class.java.name
            )
        }
    }

    // Navigate to customized content screen
    private fun navigateToCustomizedContent(
        contents: Array<LibraryContent>,
        textTitle: String
    ) {
        val action =
            LibraryContentFragmentDirections
                .actionLibraryContentFragmentToCustomizedContentFragment(
                    contents,
                    textTitle
                )
        findNavController().navigate(action)
    }

    // Observe content loading state
    private fun observeViewModel() {
        viewModel.isReadyDisplay.observe(viewLifecycleOwner) { isReady ->
            if (isReady) {
                setRecyclerCustomized(viewModel.mLibraryContentCustomized)
                setRecyclerMostCommon(viewModel.mLibraryContentMostCommon)
                dismissProgressDialog()
                viewModel.resetIsReadyDisplay()
                binding.contentLayout.visibility = View.VISIBLE
            }
        }
    }

    // Setup horizontal recycler for common content
    private fun setRecyclerMostCommon(libraryContent: List<LibraryContent>?) {
        binding.recyclerMostCommon.apply {
            layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = CommonContentLibraryAdapter(
                libraryContent,
                requireActivity(),
                viewModel.sharedPreferences,
                this@LibraryContentFragment
            )
        }
    }

    // Setup recycler for customized content
    private fun setRecyclerCustomized(libraryContent: List<LibraryContent>?) {
        binding.recyclerCustomized.adapter =
            CustomizedContentLibraryAdapter(
                libraryContent,
                requireActivity(),
                viewModel.sharedPreferences,
                this@LibraryContentFragment
            )
    }

    // Handle item click from adapters
    override fun onItemClicked(type: String, index: Int, category: String , currentContent: LibraryContent) {

        // Save selected content
        viewModel.setCurrentCategoryContent(category)
        viewModel.currentContent = currentContent
        // Navigate based on type
        when (type) {
            ARTICLE ->
                findNavController()
                    .navigate(R.id.action_libraryContentFragment_to_articleFragment)
        }
    }
}

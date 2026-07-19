package com.app.sanad.getLibraryContent.presentaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentCustomizedContentBinding
import com.app.sanad.getLibraryContent.data.LibraryContent
import com.app.sanad.interfaces.OnItemLibraryContentClicked
import com.app.sanad.util.ARTICLE

// Displays customized library content list
class CustomizedContentFragment : BaseFragment(),
    OnItemLibraryContentClicked {

    // Shared ViewModel for library data
    private val viewModel: LibraryViewModel by activityViewModels()

    // ViewBinding reference
    private lateinit var binding: FragmentCustomizedContentBinding

    // Inflate layout and initialize UI
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomizedContentBinding.inflate(inflater, container, false)
        retrieveDataFromArguments()
        setupClickListener()
        return binding.root
    }

    // Read navigation arguments and populate UI
    private fun retrieveDataFromArguments() {
        val args: CustomizedContentFragmentArgs by navArgs()
        binding.textTitle.text = args.textTitle
        setupUserRecyclerView(args.libraryContentList.toList())
    }

    // Setup recycler view for customized content
    private fun setupUserRecyclerView(libraryContent: List<LibraryContent>) {
        binding.recyclerCustomized.adapter =
            CustomizedContentLibraryAdapter(
                libraryContent,
                requireActivity(),
                viewModel.sharedPreferences,
                this@CustomizedContentFragment
            )
    }

    // Handle back navigation
    private fun setupClickListener() {
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    // Handle content item click
    override fun onItemClicked(type: String, index: Int, content: String, currentContent: LibraryContent) {

        // Save selected content state
        viewModel.setCurrentCategoryContent(content)
        viewModel.currentContent = currentContent

        // Navigate based on content type
        when (type) {
            ARTICLE ->
                findNavController()
                    .navigate(R.id.action_customizedContentFragment_to_articleFragment)
        }
    }
}

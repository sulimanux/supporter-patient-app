package com.app.sanad.getLibraryContent.presentaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.sanad.R
import com.app.sanad.base.BaseBottomSheetDialogFragment
import com.app.sanad.databinding.FragmentCommonContentBinding
import com.app.sanad.interfaces.OnItemLibraryContentClicked
import com.app.sanad.getLibraryContent.data.LibraryContent
import com.app.sanad.util.ARTICLE

// Bottom sheet that displays most common library content
class CommonContentFragment : BaseBottomSheetDialogFragment(),
    OnItemLibraryContentClicked {

    // Shared ViewModel for library data
    private val viewModel: LibraryViewModel by activityViewModels()

    // ViewBinding reference
    private lateinit var binding: FragmentCommonContentBinding

    // Inflate layout and initialize UI
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommonContentBinding.inflate(inflater, container, false)
        setupClickListener()
        initializeViews()
        return binding.root
    }

    // Handle close button click
    private fun setupClickListener() {
        binding.close.setOnClickListener {
            dismiss()
        }
    }

    // Initialize recycler view data
    private fun initializeViews() {
        setRecyclerMostCommon(viewModel.mLibraryContentMostCommon)
    }

    // Setup recycler view for common content
    private fun setRecyclerMostCommon(libraryContent: List<LibraryContent>?) {
        binding.recyclerMostCommon.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = CommonContentLibraryAdapter(
                libraryContent,
                requireActivity(),
                viewModel.sharedPreferences,
                this@CommonContentFragment
            )
        }
    }

    // Handle content item click
    override fun onItemClicked(type: String, index: Int, content: String, currentContent: LibraryContent) {

        // Update selected content in ViewModel
        updateCurrentContent(content)
         viewModel.currentContent = currentContent
        // Navigate based on content type
        when (type) {
            ARTICLE ->
                findNavController()
                    .navigate(R.id.action_libraryContentFragment_to_articleFragment)
        }

        // Close bottom sheet
        dismiss()
    }



    // Save selected content category
    private fun updateCurrentContent(content: String) {
        viewModel.setCurrentCategoryContent(content)
    }
}

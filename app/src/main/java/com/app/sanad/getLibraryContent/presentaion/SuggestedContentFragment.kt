package com.app.sanad.getLibraryContent.presentaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.app.sanad.base.BaseBottomSheetDialogFragment
import com.app.sanad.databinding.FragmentSuggestedContentBinding
import com.app.sanad.interfaces.OnItemLibraryContentClicked
import com.app.sanad.getLibraryContent.data.LibraryContent

// Bottom sheet that displays suggested content based on category and type of content
class SuggestedContentFragment :
    BaseBottomSheetDialogFragment(),
    OnItemLibraryContentClicked {

    // ViewBinding reference
    private lateinit var binding: FragmentSuggestedContentBinding

    // Title displayed in the sheet
    private lateinit var title: String

    // Content type filter
    private lateinit var type: String

    // Shared ViewModel
    private val viewModel: LibraryViewModel by activityViewModels()

    // Callback for item selection
    private lateinit var mOnItemLibraryContentClicked: OnItemLibraryContentClicked

    // Inflate layout and initialize UI
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentSuggestedContentBinding.inflate(inflater, container, false)
        initializeViews()
        setupClickListener()
        return binding.root
    }

    // Setup title and recycler content
    private fun initializeViews() {
        binding.title.text = title
        setRecyclerMostCommon(
            viewModel.getContentsBasedType(type)
        )
    }

    // Handle close action
    private fun setupClickListener() {
        binding.close.setOnClickListener {
            dismiss()
        }
    }

    // Set sheet title (builder-style)
    fun setTitle(title: String): SuggestedContentFragment {
        this.title = title
        return this
    }

    // Set content type filter (builder-style) e.g ARTICLE , AUDIo , VIdEO
    fun setType(type: String): SuggestedContentFragment {
        this.type = type
        return this
    }

    // Setup recycler for suggested content
    private fun setRecyclerMostCommon(
        libraryContent: List<LibraryContent>?
    ) {
        binding.recyclerView.adapter =
            CustomizedContentLibraryAdapter(
                libraryContent,
                requireActivity(),
                viewModel.sharedPreferences,
                this@SuggestedContentFragment
            )
    }

    // Forward item click to parent fragment
    override fun onItemClicked(type: String, index: Int, category: String, currrentContent: LibraryContent) {
        dismiss()
        mOnItemLibraryContentClicked.onItemClicked(type, index, category, currrentContent)
    }

    // Set item click callback
    fun setOnItemLibraryContent(
        onItemLibraryContentClicked: OnItemLibraryContentClicked
    ): SuggestedContentFragment {
        mOnItemLibraryContentClicked = onItemLibraryContentClicked
        return this
    }
}

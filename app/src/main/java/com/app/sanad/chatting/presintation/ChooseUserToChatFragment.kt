package com.app.sanad.chatting.presintation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.base.BaseBottomSheetDialogFragment
import com.app.sanad.databinding.FragmentChooseUserToChatBinding
import com.app.sanad.interfaces.ItemMessagesListClicked

// BottomSheet for choosing a user to start chatting with
class ChooseUserToChatFragment : BaseBottomSheetDialogFragment(),
    ItemMessagesListClicked {

    // Shared ViewModel
    private val viewModel: ChatViewModel by activityViewModels()

    // ViewBinding
    private lateinit var binding: FragmentChooseUserToChatBinding

    // RecyclerView adapter
    private lateinit var adapter: SupportersChattingAdapter

    // Inflate layout and load partners
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooseUserToChatBinding.inflate(inflater, container, false)

        // Load partners list
        viewModel.retrievePartners()

        // Observe data changes
        observeViewModel()

        // Close button handler
        setupClickListener()

        return binding.root
    }

    // Close bottom sheet
    private fun setupClickListener() {
        binding.close.setOnClickListener {
            dismiss()
        }
    }

    // Observe partners list
    private fun observeViewModel() {
        viewModel.partners.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter = SupportersChattingAdapter(it, requireActivity(), this)
                binding.recyclerView.adapter = adapter
            }
        }
    }

    // Item click callback
    override fun onItemClicked(name: String, idPartner: String, urlImage: String, position: Int) {
        navigateToChatFragment(idPartner, urlImage, name)
        dismiss()
    }

    // Navigate to chat screen
    private fun navigateToChatFragment(
        id: String,
        urlImage: String,
        name: String
    ) {
        viewModel.partnerId = id
        val action =
            MessagesListFragmentDirections
                .actionMessagesListFragmentToChatFragment(
                    id,
                    urlImage,
                    name
                )
        findNavController().navigate(action)
    }
}

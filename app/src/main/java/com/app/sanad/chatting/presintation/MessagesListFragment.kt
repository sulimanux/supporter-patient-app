package com.app.sanad.chatting.presintation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentMessagesListBinding
import com.app.sanad.interfaces.ItemMessagesListClicked
import com.app.sanad.chatting.data.entity.Chatting
import kotlin.getValue


// Fragment that displays the list of chat messages
class MessagesListFragment : BaseFragment(), ItemMessagesListClicked {

    // Shared ViewModel with Activity scope
    private val viewModel: ChatViewModel by activityViewModels()

    // ViewBinding for fragment layout
    private lateinit var binding: FragmentMessagesListBinding

    // Inflate layout and check internet connection
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesListBinding.inflate(inflater, container, false)

        checkInternetConnection()
        setupClickListener()
        return binding.root
    }

    // Check internet before loading UI
    private fun checkInternetConnection() {
        if (isConnected()) {
            initializeViews()
        } else {
            binding.noInternetLayout.swipeRefresh.visibility = View.VISIBLE
        }
    }



    // Initialize UI logic
    private fun initializeViews() {
        showProgressDialog()
        observeViewModel()
    }

    private fun setupClickListener() {
        binding.btStartMessaging.setOnClickListener { checkInternet() }
        binding.icAdd.setOnClickListener { checkInternet() }
        binding.noInternetLayout.tryAgainBt.setOnClickListener {
            binding.noInternetLayout.swipeRefresh.visibility = View.GONE
            checkInternetConnection()
        }
    }

    // Re-check internet before navigation
    private fun checkInternet() {
        if (isConnected()) {
            chooseUserToChatWith()
        } else {
            showNoInternetSnackBar(binding.root)
        }
    }



    // Decide which user can be selected for chat
    private fun chooseUserToChatWith() {
        if (viewModel.isUserSupporter()) {
            ChooseUserToChatFragment().show(childFragmentManager, ChooseUserToChatFragment::class.java.name)
        } else {
            if (viewModel.hasPartner()) {
                ChooseUserToChatFragment().show(childFragmentManager, ChooseUserToChatFragment::class.java.name)
            } else {
                showToast(getString(R.string.no_supporters_text))
            }
        }
    }

    // Observe chat list data
    private fun observeViewModel() {
        viewModel.chattingRepo.chattingList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.messages.visibility = View.GONE
                binding.noMessage.visibility = View.VISIBLE
            } else {
                updateUIWithMessagesList(it)
                binding.noMessage.visibility = View.GONE
                binding.messages.visibility = View.VISIBLE
            }
            dismissProgressDialog()
        }
    }

    // Update RecyclerView with messages
    private fun updateUIWithMessagesList(chattingList: List<Chatting>) {
        binding.messagesRecyclerView.adapter =
            MessagesListAdapter(chattingList, requireActivity(), viewModel.sharedPreferences, this)
    }

    // Handle message item click and navigate to chat screen
    override fun onItemClicked(name: String, idPartner: String, urlImage: String, position: Int) {
        viewModel.partnerId = idPartner
        viewModel.updateSeenMessages(idPartner, position)
        val action =
            MessagesListFragmentDirections.actionMessagesListFragmentToChatFragment(
                idPartner,
                urlImage,
                name
            )
        findNavController().navigate(action)
    }
}

package com.app.sanad.chatting.presintation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentChatBinding
import com.app.sanad.chatting.data.entity.Message
import com.app.sanad.chatting.data.entity.MetaDataMessages
import com.app.sanad.util.loadImage

class ChatFragment : BaseFragment() {

    // Shared ViewModel
    private val viewModel: ChatViewModel by activityViewModels()

    // ViewBinding
    private lateinit var binding: FragmentChatBinding

    // Partner data
    private var partnerId = ""
    private var urlImagePartner = ""
    private var namePartner = ""

    // Inflate layout and check internet
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        setupClickListener()
        checkInternetConnection()
        return binding.root
    }

    // Internet check gate
    private fun checkInternetConnection() {
        if (isConnected()) initializeViews() else {
            binding.noInternetLayout.swipeRefresh.visibility = View.VISIBLE
        }
    }



    // Main initialization
    private fun initializeViews() {
        showProgressDialog()
        retrieveDataFromArguments()
        initUserData()
        viewModel.listenToMessages()
        observeViewModel()
        addTextChangedListener()

    }

    // Get navigation arguments
    private fun retrieveDataFromArguments() {
        val args: ChatFragmentArgs by navArgs()
        partnerId = args.partnerId
        urlImagePartner = args.urlImagePartner
        namePartner = args.namePartner
    }

    // Set partner UI data
    private fun initUserData() {
        binding.name.text = namePartner
        loadImage(requireActivity(), urlImagePartner, binding.imageUser)
    }

    // Show send button only when text exists
    private fun addTextChangedListener() {
        binding.messageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.sendButton.visibility =
                    if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Observe messages and UI actions
    private fun observeViewModel() {

        // Messages stream
        viewModel.messages.observe(viewLifecycleOwner) {
            it?.let {
                updateUIWithMessages(it)
                dismissProgressDialog()
            }
        }

        // Clear input after sending
        viewModel.clearEditText.observe(viewLifecycleOwner) {
            if (it) {
                binding.messageEditText.text.clear()
                viewModel.resetClearEditText()
            }
        }
    }

    // Update RecyclerView and scroll to bottom
    private fun updateUIWithMessages(messages: List<Message>) {
        binding.messageRecyclerView.apply {
            adapter = MessagesAdapter(messages, viewModel.user.id!!)
            scrollToPosition(messages.size - 1)
        }
        binding.rootMessages.visibility = View.VISIBLE
    }

    // Build new message object
    private fun getNewMessage() =
        Message(
            seenByPatient = !viewModel.isUserSupporter(),
            seenBySupporter =   viewModel.isUserSupporter(),
            text = binding.messageEditText.text.toString(),
            senderId = viewModel.user.id!!
        )

    // UI click handlers
    private fun setupClickListener() {

        // Retry on no internet
        binding.noInternetLayout.tryAgainBt.setOnClickListener {
            binding.noInternetLayout.swipeRefresh.visibility = View.GONE
            checkInternetConnection()
        }


        // Back navigation
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Send message
        binding.sendButton.setOnClickListener {
            viewModel.sendMessage(getNewMessage(), getMetaDataMessages())
        }
    }

    // Build metadata depending on user role
    private fun getMetaDataMessages(): MetaDataMessages {
        return if (viewModel.isUserSupporter()) {
            MetaDataMessages(
                nameSupporter = viewModel.user.name,
                idSupporter = viewModel.user.id!!,
                imageSupporter = viewModel.user.imageUser!!,
                namePatient = namePartner,
                idPatient = partnerId,
                imagePatient = urlImagePartner
            )
        } else {
            MetaDataMessages(
                nameSupporter = namePartner,
                idSupporter = partnerId,
                imageSupporter = urlImagePartner,
                namePatient = viewModel.user.name,
                idPatient = viewModel.user.id!!,
                imagePatient = viewModel.user.imageUser!!
            )
        }
    }

    // Clear messages when leaving screen
    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearMessages()
    }
}

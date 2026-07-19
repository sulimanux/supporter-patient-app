package com.app.sanad.users.patient.tools.gratitude.presentaion

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.posts.presentation.ChooseSupporterFragment
import com.app.sanad.databinding.DialogShareContentBinding
import com.app.sanad.databinding.FragmentGratitudeBinding
import com.app.sanad.interfaces.OnConfirmButtonClicked
import com.app.sanad.posts.OnSendButtonClicked
import com.app.sanad.posts.presentation.PostsViewModel
import com.app.sanad.users.patient.tools.gratitude.data.entity.Gratitude
import com.app.sanad.posts.data.entity.Post
import com.app.sanad.util.GRATITUDE
import com.app.sanad.util.isValidInput
import com.app.sanad.util.log
import kotlin.getValue

@AndroidEntryPoint
class GratitudeFragment :
    BaseFragment(),
    OnConfirmButtonClicked,
    OnSendButtonClicked {

    // Shared ViewModel for gratitude logic
    private val viewModel: GratitudeViewModel by activityViewModels()

    // ViewModel responsible for post sharing
    private val postsViewModel: PostsViewModel by viewModels()

    // ViewBinding reference
    private lateinit var binding: FragmentGratitudeBinding

    // Stores the last submitted gratitude answer for sharing
    private lateinit var answer: String

    /**
     * Inflates the layout and initializes UI behavior
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGratitudeBinding.inflate(inflater, container, false)

        // Set a random gratitude question
        setText(viewModel.getRandomQuestion(requireActivity()))

        // Setup UI interactions
        setupClickListener()

        // Observe sharing state
        observeViewModel()

        return binding.root
    }

    /**
     * Observes post sharing status
     */
    private fun observeViewModel() {
        postsViewModel.statusSharing.observe(viewLifecycleOwner) {
            if (it) {
                showToast(getString(R.string.shared_successfully))
            }
            dismissProgressDialog()
        }
    }

    /**
     * Updates the displayed gratitude question
     */
    private fun setText(text: String) {
        binding.tvQuestion.text = text
    }

    /**
     * Handles all click events in the screen
     */
    private fun setupClickListener() {

        // Navigate back
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Submit gratitude answer
        binding.btnSend.setOnClickListener {
            val answer = binding.edtAnswer.text.toString()
            if (isValidation(answer)) {
                checkConnection()
            }
        }

        // Open suggested gratitude questions dialog
        binding.btnRecommend.setOnClickListener {
            SuggestedGratitudeQuestionsFragment(this).show(
                childFragmentManager,
                SuggestedGratitudeQuestionsFragment::class.java.name
            )
        }
    }

    /**
     * Checks internet connectivity before submitting gratitude
     */
    private fun checkConnection() {
        if (isConnected()) {
            showProgressDialog()
            addGratitude(
                Gratitude(
                    index = viewModel.getSelectedPosition(),
                    answer = binding.edtAnswer.text.toString()
                )
            )
        } else {
            showNoInternetSnackBar(binding.root)
        }
    }

    /**
     * Saves gratitude remotely and triggers sharing dialog on success
     */
    private fun addGratitude(gratitude: Gratitude) {
        viewModel.saveGratitudeRemotely(gratitude) {
            if (it) {
                showToast(getString(R.string.your_answer_has_been_sent))
                answer = binding.edtAnswer.text.toString()
                clearText()
                showSharingDialog()
            }
            dismissProgressDialog()
        }
    }

    /**
     * Clears the answer input field
     */
    private fun clearText() {
        binding.edtAnswer.text.clear()
    }

    /**
     * Validates user input before submission
     */
    private fun isValidation(text: String): Boolean {
        return if (!isValidInput(text)) {
            showToast(getString(R.string.should_answer_question))
            false
        } else {
            true
        }
    }

    /**
     * Callback from suggested questions dialog
     */
    override fun onConfirmClicked(text: String) {
        setText(text)
    }

    /**
     * Displays dialog asking user whether to share gratitude
     */
    private fun showSharingDialog() {
        sharedDialog = Dialog(requireContext())
        sharedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogBinding = DialogShareContentBinding.inflate(layoutInflater)
        sharedDialog.setContentView(dialogBinding.root)
        sharedDialog.setCanceledOnTouchOutside(true)

        sharedDialog.window!!.apply {
            val layoutParams = attributes
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = layoutParams
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        // Confirm sharing action
        dialogBinding.btOk.setOnClickListener {
            if (viewModel.gratitudeRepo.user().hasPartner == true) {
                navigateToChooseSupporter()
            } else {
                showToast(getString(R.string.no_supporters_text))
            }
            sharedDialog.dismiss()
        }

        // Close dialog
        dialogBinding.icClose.setOnClickListener {
            sharedDialog.dismiss()
        }

        sharedDialog.show()
    }

    /**
     * Opens supporter selection bottom sheet
     */
    private fun navigateToChooseSupporter() {
        val fragment = ChooseSupporterFragment()
        fragment.initOnConfirmButtonClicked(this)
        fragment.show(childFragmentManager, ChooseSupporterFragment::class.java.name)
    }

    /**
     * Callback when supporters are selected and post should be shared
     */
    override fun onSendClicked(supporters: MutableList<String> , listId:MutableList<String>) {
        log("onSendClicked $supporters")
        showProgressDialog()
        postsViewModel.sharePost(post(supporters , listId))
    }

    /**
     * Creates a gratitude post object for sharing
     */
    private fun post(supporters: MutableList<String> , listId:MutableList<String>) =
        Post(
            type = GRATITUDE,
            listSupportersId = listId,
            gratitude = Gratitude(
                index = viewModel.getSelectedPosition(),
                answer = answer
            ),
            supporters = supporters
        )
}

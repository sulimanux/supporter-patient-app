package com.app.sanad.users.patient.tools.coffeeideas.presentaion

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.ChooseSupporterDialogBinding
import com.app.sanad.databinding.FragmentStep3Binding
import com.app.sanad.users.supporter.tools.cofe.data.entity.UserIdea
import com.app.sanad.util.CHOOSE_PATH_ENGAGEMENT
import com.app.sanad.util.PATH_CHOSEN
import com.app.sanad.util.RESET_THOUGHT_MYSELF
import com.app.sanad.util.THOUGHT_SENT
import com.app.sanad.util.TIME_SPENT
import com.app.sanad.util.Temp
import com.app.sanad.util.log

/**
 * Step 3 of the Coffee Ideas flow.
 *
 * Allows the patient to choose between:
 * 1. Resetting the thought and reflecting on their own ("user" path)
 * 2. Sending the thought to a supporter ("friend" path)
 *
 * Handles navigation, showing a supporter selection dialog, and sharing ideas.
 */
@AndroidEntryPoint
class Step3Fragment : BaseFragment(), ItemListener {

    private lateinit var binding: FragmentStep3Binding
    private val viewModel: CofeViewModel by activityViewModels()

    private lateinit var chooseUserDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStep3Binding.inflate(inflater)

        setUpListeners()        // Set up UI click actions
        observeViewModel()      // Observe LiveData from the ViewModel

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Temp.stoppedAt = "Step3Fragment" // Track current fragment for engagement
    }

    /**
     * Handles all click listeners for the fragment buttons.
     */
    private fun setUpListeners() {

        // "User" path: patient decides to reflect on the thought by themselves
        binding.user.enter.setOnClickListener {

            if (viewModel.sharedPreferences.getBoolean("isThereConnection", false)) {
                // If there is a thought already in progress, prompt user
                showToast("You still have a thought needs to clear first")
                val action = Step3FragmentDirections.actionStep3FragmentToFriendIdeaEditingFragment()
                findNavController().navigate(action)
            } else {
                log("user path chosen")
                logEvent(PATH_CHOSEN) {
                    param(PATH_CHOSEN, RESET_THOUGHT_MYSELF)
                }
                // Navigate to Step4 where user reflects on their own thought
                findNavController().navigate(R.id.action_step3Fragment_to_step4Fragment)
            }
        }

        // "Friend" path: send the thought to a supporter
        binding.friend.enter.setOnClickListener {
            if (viewModel.sharedPreferences.getBoolean("isThereConnection", false)) {
                // If previous thought is still in progress
                showToast("You still have a thought needs to clear first")
                val action = Step3FragmentDirections.actionStep3FragmentToFriendIdeaEditingFragment()
                findNavController().navigate(action)
            } else {
                logEvent(PATH_CHOSEN) {
                    param(PATH_CHOSEN, THOUGHT_SENT)
                }
                navigateToChooseSupporter()  // Show dialog to pick supporter
            }
        }

        // Exit the activity
        binding.exit.setOnClickListener {
            activity?.finish()
        }

        // Navigate back
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        log("1 idea is ${viewModel.textIdea.value}")
        log("1f idea is ${viewModel.textQuestion1.value}")
    }

    /**
     * Observes LiveData from the ViewModel for:
     * - supporter profiles
     * - sharing state (when idea has been sent)
     */
    private fun observeViewModel() {

        // Observe supporters profile list
        viewModel.supportersProfile.observe(viewLifecycleOwner) {
            log("supportersProfile observe $it")
            it?.let {
                // Only show dialog if no previous connection exists
                if (!viewModel.sharedPreferences.getBoolean("isThereConnection", false)) {
                    log("supportersProfile inside")
                    showChooseSupporterDialog(it)
                    dismissProgressDialog()
                }
            }
        }

        // Observe sharing state to navigate after idea is sent
        viewModel.sharingState.observe(viewLifecycleOwner) {
            it?.let {
                val action = Step3FragmentDirections.actionStep3FragmentToFriendIdeaEditingFragment()
                viewModel.sharedPreferences.storeBoolean("isThereConnection", true)
                findNavController().navigate(action)
                viewModel.resetSharingState()
            }
        }
    }

    /**
     * Navigate to supporter selection or show message if no partner exists
     */
    private fun navigateToChooseSupporter() {
        if (!viewModel.user().hasPartner!!) {
            showToast(getString(R.string.no_supporters_text))
        } else {
            retrieveSupporters()
        }
    }

    /**
     * Retrieve supporters from backend, show progress dialog, handle no internet
     */
    private fun retrieveSupporters() {
        if (!isConnected()) {
            log("no internet")
            showNoInternetSnackBar(binding.root)
        } else {
            log("internet available")
            showProgressDialog()
            viewModel.retrieveSupporters()
        }
    }

    /**
     * Show a dialog allowing the user to choose a supporter to share their idea
     */
    private fun showChooseSupporterDialog(supporters: List<UserProfile>) {
        log("showChooseSupporterDialog")
        chooseUserDialog = Dialog(requireContext())
        chooseUserDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogBinding = ChooseSupporterDialogBinding.inflate(layoutInflater)
        chooseUserDialog.setContentView(dialogBinding.root)
        chooseUserDialog.setCanceledOnTouchOutside(true)

        val window = chooseUserDialog.window
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val layoutParams = attributes
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = layoutParams
        }

        // Set up adapter with supporter list
        val adapter = ChooseSupporterAdapter(supporters, this)
        dialogBinding.recyclerView.adapter = adapter

        // Close button
        dialogBinding.icClose.setOnClickListener {
            chooseUserDialog.dismiss()
        }

        chooseUserDialog.show()
    }

    /**
     * Called when a supporter is selected from the dialog
     */
    override fun onItemClick(supporter: UserProfile) {
        chooseUserDialog.dismiss()
        viewModel.sharedPreferences.storeString("tempSupporterId", supporter.id!!)

        // Create a UserIdea with current input and cup selection
        val userIdea = UserIdea(
            idea = viewModel.textIdea.value,
            response = "",
            cupIdea = viewModel.cupNumber
        )
        // Share the idea via ViewModel
        viewModel.shareIdeaWithSupporter(userIdea, supporter)
    }

    override fun onStop() {
        super.onStop()
        // Reset supporters list to avoid stale data
        viewModel.resetSupporters()

        // Log engagement duration for this step
        logEvent(CHOOSE_PATH_ENGAGEMENT) {
            param(TIME_SPENT, duration())
        }
    }
}

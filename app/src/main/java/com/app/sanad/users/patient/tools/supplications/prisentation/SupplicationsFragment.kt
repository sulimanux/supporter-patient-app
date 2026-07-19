// Package responsible for supplications presentation layer
package com.app.sanad.users.patient.tools.supplications.prisentation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.posts.OnSendButtonClicked
import com.app.sanad.posts.presentation.ChooseSupporterFragment
import com.app.sanad.posts.presentation.PostsViewModel
import com.app.sanad.databinding.DialogFullTextSupplicationBinding
import com.app.sanad.databinding.FragmentSupplicationsBinding
import com.app.sanad.model.Supplication
import com.app.sanad.util.LANGUAGE
import java.util.Locale
import kotlin.getValue

/**
 * Screen responsible for displaying and interacting with a single supplication.
 * Handles counting, sound effects, sharing, and UI focus changes.
 */
@AndroidEntryPoint
class SupplicationsFragment : BaseFragment(), OnSendButtonClicked {

    // Shared ViewModel holding supplication state
    private val viewModel: SupplicationViewModel by activityViewModels()

    // ViewModel used for sharing supplication as a post
    private val postsViewModel: PostsViewModel by viewModels()

    // ViewBinding for fragment layout
    private lateinit var binding: FragmentSupplicationsBinding

    // Current supplication text
    private lateinit var supplicationText: String

    // Dialog used to display full supplication text
    private lateinit var fullTextSupplicationDialog: Dialog

    // MediaPlayer for tick sound when counting
    private var mediaPlayer: MediaPlayer? = null

    /**
     * Inflates the layout, initializes UI data and listeners
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupplicationsBinding.inflate(inflater, container, false)

        // Initialize UI with selected supplication
        setUiData(viewModel.selectedSupplication!!)
        viewModel.resetCounter()

        observeViewModel()
        setupClickListener()

        return binding.root
    }

    /**
     * Sets supplication data in UI components
     */
    private fun setUiData(supplication: Supplication) {
        binding.textNameSupplication.text = supplication.name
        binding.textBaseNumber.text = getLocalizedNumber(supplication.number!!)
        supplicationText = supplication.name!!
    }

    /**
     * Formats numbers based on selected language
     */
    private fun getLocalizedNumber(number: Int): String {
        var lang = viewModel.supplicationsRepo.sharedPreferences.getString(LANGUAGE)
        lang = if (lang.isEmpty()) "ar" else lang
        return String.format(Locale(lang), "%d", number)
    }

    /**
     * Observes ViewModel updates and updates UI accordingly
     */
    private fun observeViewModel() {

        // Observe sharing status
        postsViewModel.statusSharing.observe(viewLifecycleOwner) {
            if (it) {
                showToast(getString(R.string.shared_successfully))
            }
            dismissProgressDialog()
        }

        // Observe hand image changes
        viewModel.newImageSupplication.observe(viewLifecycleOwner) {
            binding.handImageView.setImageResource(it)
        }

        // Observe remaining counter updates
        viewModel.numberRemaining.observe(viewLifecycleOwner) {
            if (it != 0) {
                playTickSound()
            }
            binding.textRemaining.text = getLocalizedNumber(it)
        }
    }

    /**
     * Sets up click listeners for all UI interactions
     */
    private fun setupClickListener() {

        // Navigate back
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Reset counter when background is clicked
        binding.constraintLayout.setOnClickListener {
            viewModel.resetCounter()
        }

        // Increase counter when hand image is clicked
        binding.handImageView.setOnClickListener {
            viewModel.onHandClick()
        }

        // Switch focus to hand mode
        binding.imageViewHand.setOnClickListener {
            changeFocusing(true)
            viewModel.setListImage(viewModel.supplicationsRepo.handsList())
            viewModel.resetCounter()
        }

        // Show full supplication text dialog
        binding.icShowFullSupplication.setOnClickListener {
            showFullTextSupplicationDialog(supplicationText)
        }

        // Show popup menu (share options)
        binding.icMore.setOnClickListener {
            showPopupMenu(it)
        }

        // Switch focus to sebha mode
        binding.imageViewSebha.setOnClickListener {
            changeFocusing(false)
            viewModel.setListImage(viewModel.supplicationsRepo.sebhaList())
            viewModel.resetCounter()
        }
    }

    /**
     * Updates UI to reflect current focus (hand or sebha)
     */
    private fun changeFocusing(isHand: Boolean) {
        if (isHand) {
            binding.imageViewHand.setBackgroundDrawable(
                resources.getDrawable(R.drawable.circle_blue_border_blue)
            )
            binding.imageViewSebha.setBackgroundDrawable(
                resources.getDrawable(R.drawable.circle_blue2)
            )
        } else {
            binding.imageViewHand.setBackgroundDrawable(
                resources.getDrawable(R.drawable.circle_blue2)
            )
            binding.imageViewSebha.setBackgroundDrawable(
                resources.getDrawable(R.drawable.circle_blue_border_blue)
            )
        }
    }

    /**
     * Displays popup menu for supplication actions
     */
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireActivity(), view)
        popupMenu.inflate(R.menu.settings_supplicaion_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            handleMenuItemClick(menuItem)
        }
        popupMenu.show()
    }

    /**
     * Handles popup menu item clicks
     */
    private fun handleMenuItemClick(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_sharing -> {
                navigateToChooseSupporter()
                true
            }
            else -> false
        }
    }

    /**
     * Navigates to supporter selection before sharing
     */
    private fun navigateToChooseSupporter() {
        if (!viewModel.supplicationsRepo.getUser().hasPartner!!) {
            showToast(getString(R.string.no_supporters_text))
        } else if (!isConnected()) {
            showNoInternetSnackBar(binding.root)
        } else {
            val fragment = ChooseSupporterFragment()
            fragment.initOnConfirmButtonClicked(this)
            fragment.show(childFragmentManager, ChooseSupporterFragment::class.java.name)
        }
    }

    /**
     * Displays dialog with full supplication text
     */
    private fun showFullTextSupplicationDialog(supplicationText: String) {
        fullTextSupplicationDialog = Dialog(requireContext())
        fullTextSupplicationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val binding = DialogFullTextSupplicationBinding.inflate(layoutInflater)
        fullTextSupplicationDialog.setContentView(binding.root)
        fullTextSupplicationDialog.setCanceledOnTouchOutside(true)

        fullTextSupplicationDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        fullTextSupplicationDialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )

        binding.supplicationText.text = supplicationText
        binding.iconClose.setOnClickListener {
            fullTextSupplicationDialog.dismiss()
        }

        fullTextSupplicationDialog.show()
    }

    /**
     * Release MediaPlayer resources
     */
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    /**
     * Plays tick sound when counter changes
     */
    private fun playTickSound() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, R.raw.tick4)
        mediaPlayer?.start()
    }

    /**
     * Callback when sharing is confirmed
     */
    override fun onSendClicked(supporters: MutableList<String> , listId:MutableList<String>) {
        showProgressDialog()
        postsViewModel.sharePost(viewModel.post(supporters , listId))
    }
}

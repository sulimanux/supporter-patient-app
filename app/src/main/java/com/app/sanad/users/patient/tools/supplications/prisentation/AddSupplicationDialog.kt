// Package responsible for patient supplications presentation tools
package com.app.sanad.users.patient.tools.supplications.prisentation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseDialogFragment
import com.app.sanad.databinding.DialogAddSupplicationsBinding
import com.app.sanad.model.Supplication
import com.app.sanad.util.isValidInput

/**
 * Dialog used to add a new supplication with a repetition count.
 * Uses ViewBinding and shares a ViewModel with the hosting Activity.
 */
@AndroidEntryPoint
class AddSupplicationDialog : BaseDialogFragment() {

    // ViewBinding for dialog layout
    private lateinit var binding: DialogAddSupplicationsBinding

    // User input values
    private lateinit var supplicationText: String
    private lateinit var numberOfRepetitionText: String

    // Shared ViewModel between activity and dialog
    private val viewModel: SupplicationViewModel by activityViewModels()

    // Current supplication if in edit mode
    private var editingSupplication: Supplication? = null

    /**
     * Creates and configures the custom dialog UI
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Initialize dialog with custom style
        localDialog = Dialog(requireContext())
        localDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Inflate layout using ViewBinding
        binding = DialogAddSupplicationsBinding.inflate(layoutInflater)
        localDialog.setContentView(binding.root)

        // Check if we are in edit mode
        editingSupplication = arguments?.getParcelable("supplication")
        if (editingSupplication != null) {
            binding.supplicationEditText.setText(editingSupplication?.name)
            binding.numberEditText.setText(editingSupplication?.number.toString())
            binding.addButton.text = getString(R.string.edit)
        }

        // Allow dismiss when user taps outside
        localDialog.setCanceledOnTouchOutside(true)

        // Customize dialog window appearance and size
        val window = localDialog.window
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val layoutParams = attributes
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = layoutParams
        }

        // Setup UI interactions and observers
        setupClickListener()
        observeViewModel()

        return localDialog
    }

    /**
     * Handles button click events inside the dialog
     */
    private fun setupClickListener() {

        // Close icon dismisses the dialog
        binding.icClose.setOnClickListener {
            localDialog.dismiss()
        }

        // Add button validates input and submits supplication
        binding.addButton.setOnClickListener {
            if (checkValidation()) {
                if (isConnected()) {
                    if (editingSupplication != null) {
                        viewModel.updateUserSupplication(supplication().apply { id = editingSupplication?.id })
                    } else {
                        viewModel.storeUserSupplication(supplication())
                    }
                } else {
                    showNoInternetSnackBar(binding.root)
                }
            }
        }
    }

    /**
     * Validates user input fields
     * @return true if inputs are valid, false otherwise
     */
    private fun checkValidation(): Boolean {
        // Read user input from EditTexts
        supplicationText = binding.supplicationEditText.text.toString()
        numberOfRepetitionText = binding.numberEditText.text.toString()

        return if (!isValidInput(supplicationText)) {
            showToast(getString(R.string.please_write_the_supplication_that_you_want))
            false
        } else if (!isValidInput(numberOfRepetitionText)) {
            showToast(getString(R.string.please_write_the_number_of_repetitions_you_want_to_reach))
            false
        } else if (numberOfRepetitionText.startsWith("0")) {
            showToast(getString(R.string.number_not_valid))
            false
        } else {
            true
        }
    }

    /**
     * Creates a Supplication model from validated input
     */
    private fun supplication(): Supplication {
        return Supplication(
            name = supplicationText,
            number = numberOfRepetitionText.toInt()
        )
    }

    /**
     * Observes ViewModel events related to dialog dismissal
     */
    private fun observeViewModel() {
        viewModel.dismissSupplicationDialog.observe(this) { isDismiss ->
            if (isDismiss) {
                localDialog.dismiss()
                viewModel.resetDismissSupplicationDialog()
            }
        }
    }
}

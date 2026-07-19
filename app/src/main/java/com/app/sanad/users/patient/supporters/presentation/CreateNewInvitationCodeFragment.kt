package com.app.sanad.users.patient.supporters.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentCreateNewInvitationCodeBinding

@AndroidEntryPoint

class CreateNewInvitationCodeFragment : BaseFragment() {

    private lateinit var binding: FragmentCreateNewInvitationCodeBinding
    private val viewModel: SupporterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCreateNewInvitationCodeBinding.inflate(inflater, container, false)
        initView()
        setupClickListener()
        observeViewModel()
        return binding.root
    }


    private fun initView() {
        val baseCode = viewModel.userProfile().invitationBase?.take(4)
        binding.edCurrentCode.setText(baseCode)
    }


    private fun setupClickListener() {
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.confirmation.setOnClickListener {
            val code = binding.edNewCode.text.toString().trim()
            if (isValidCode(code)) {
                val baseCode = viewModel.userProfile().invitationBase?.take(4)
                checkConnection(baseCode + code)
            }
        }

    }

    private fun checkConnection(newInvitationCode: String) {
        if (isConnected()) {
            showProgressDialog()
            viewModel.storeNewInvitationCode(newInvitationCode)
        } else {
            showNoInternetSnackBar(binding.root)
        }

    }

    private fun isValidCode(code: String): Boolean {
        return if (code.isNullOrEmpty()) {
            showToast(getString(R.string.enter_new_code))
            false
        } else if (code.length < 4) {
            showToast(getString(R.string.enter_four_digits_or_letters))
            false
        } else if (code.length > 4) {
            showToast(getString(R.string.new_code_length_exceeded))
            false
        } else {
            true
        }
    }

    private fun observeViewModel() {
        viewModel.status.observe(viewLifecycleOwner) {
            it?.let {
                dismissProgressDialog()
                if (it.isEmpty()) {
                    showToast(getString(R.string.new_invitation_code_has_been_created))
                    findNavController().popBackStack()
                } else {
                    showToast(it)
                }
            }
        }
    }





}
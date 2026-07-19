package com.app.sanad.users.patient.supporters.presentation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.DialogAddSupporterBinding
import com.app.sanad.databinding.DialogCannotAddSupporterBinding
import com.app.sanad.databinding.FragmentSupportersBinding
import com.app.sanad.util.log

@AndroidEntryPoint
class SupportersFragment : BaseFragment() {

    private lateinit var binding: FragmentSupportersBinding

    private lateinit var adapter: SupportersAdapter
    private val viewModel: SupporterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupportersBinding.inflate(inflater, container, false)
        checkConnection()
        initAdapter()
        setupClickListener()
        observeViewModel()
        return binding.root
    }
    private fun checkConnection() {
        if(isConnected()){
            retrieveData()
        }else{
            binding.noInternet.swipeRefresh.visibility = View.VISIBLE
        }
    }

    private fun observeViewModel() {
        dismissProgressDialog()
        viewModel.supportersProfile.observe(viewLifecycleOwner) {
            binding.noInternet.swipeRefresh.visibility = View.GONE
            if (it != null) {
                adapter.submitList(it)
                showRecycler(true)
            } else {
                showRecycler(false)
        }
    }
    }

    private fun retry(){
        if(isConnected()){
            retrieveData()
        }else{
            binding.noInternet.swipeRefresh.visibility = View.VISIBLE
        }
    }



    private fun retrieveData() {
        showProgressDialog()
        binding.noInternet.swipeRefresh.visibility = View.GONE
        viewModel.retrieveSupporters()
    }

    private fun initAdapter() {
        adapter = SupportersAdapter(requireActivity(), SupporterListener {
            val action = SupportersFragmentDirections.actionSupportesFragmentToSupporterDetailsFragment(it)
            findNavController().navigate(action)
        })
        binding.recyclerSupporters.adapter = adapter
    }



    private fun showRecycler(boolean: Boolean) {
        dismissProgressDialog()

        if (boolean) {
            binding.noSupporters.visibility = View.GONE
            binding.supporters.visibility = View.VISIBLE
        } else {
            binding.supporters.visibility = View.GONE
            binding.noSupporters.visibility = View.VISIBLE
        }
    }


  private  fun setupClickListener() {

        binding.btAddSupporter.setOnClickListener {
            findNavController().navigate(R.id.action_supportesFragment_to_addSupporterFragment)
        }


        binding.icAdd.setOnClickListener {
            if (viewModel.userProfile().supportersNumber!! >= 3) {
                showDialogCannotAdding()
            }else{
                findNavController().navigate(R.id.action_supportesFragment_to_addSupporterFragment)
            }
        }
        binding.icBack.setOnClickListener {
            activity?.finish()
        }
      binding.noInternet.tryAgainBt.setOnClickListener {
          retry()
      }
    }


    private fun showDialogCannotAdding() {
        sharedDialog = Dialog(requireContext())
        sharedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogCannotAddSupporterBinding.inflate(layoutInflater)
        sharedDialog.setContentView(dialogBinding.root)
        sharedDialog.setCanceledOnTouchOutside(true)
        val window = sharedDialog.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        sharedDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.btOk.setOnClickListener {
            sharedDialog.dismiss()
        }
        dialogBinding.icClose.setOnClickListener {
            sharedDialog.dismiss()
        }
        sharedDialog.show()
    }



}
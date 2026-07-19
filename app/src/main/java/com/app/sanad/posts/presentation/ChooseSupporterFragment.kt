package com.app.sanad.posts.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.posts.presentation.ChooseSupportersAdapter
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.base.BaseBottomSheetDialogFragment
import com.app.sanad.posts.OnSendButtonClicked
import com.app.sanad.databinding.FragmentChooseSupporterBinding
import com.app.sanad.util.log
import kotlin.getValue

@AndroidEntryPoint

class ChooseSupporterFragment : BaseBottomSheetDialogFragment() {

    private val viewModel: PostsViewModel by activityViewModels()
    private lateinit var binding: FragmentChooseSupporterBinding

    private lateinit var onSendButtonClicked: OnSendButtonClicked
    private lateinit var chooseAdapter: ChooseSupportersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseSupporterBinding.inflate(inflater,container,false)
        viewModel.retrieveSupporters()
        setupClickListener()
        observeViewModel()
        return  binding.root
    }


   private  fun setupClickListener() {
       binding.close.setOnClickListener {
           dismiss()
       }

        binding.send.setOnClickListener {
            if (chooseAdapter.getSelectedSupporters().size == 0) {
                showToast(getString(R.string.please_select_supporter))
            } else {
                onSendButtonClicked.onSendClicked(chooseAdapter.getSelectedSupporters() , chooseAdapter.getSelectedSupportersIds())
                dismiss()
            }

        }
    }

    fun initOnConfirmButtonClicked(onSendButtonClicked: OnSendButtonClicked) {
        this.onSendButtonClicked = onSendButtonClicked
    }

    private fun observeViewModel(){
        viewModel.supportersProfile.observe (this){
            it?.let {
                updateUi(it)
            }
        }
    }

    private fun updateUi(it: List<UserProfile>) {
        chooseAdapter = ChooseSupportersAdapter(requireActivity(), it)
        binding.supportersRecyclerView.apply {
            adapter = chooseAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            binding.loaderProgress.visibility = View.GONE
            binding.send.visibility = View.VISIBLE
            alpha = 1.0f
        }

    }


}
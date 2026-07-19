package com.app.sanad.users.supporter.tools.cofe.presintaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.base.BaseBottomSheetDialogFragment
import com.app.sanad.databinding.FragmentSuggestedPhrasesSympathyBinding

@AndroidEntryPoint
class SuggestedPhrasesSympathyFragment : BaseBottomSheetDialogFragment() , OnItemSelectedListener{
     private val viewModel: SupportCafeViewModel by viewModels()
     private lateinit var adapter :SuggestedSympathyAdapter

     private lateinit var binding: FragmentSuggestedPhrasesSympathyBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSuggestedPhrasesSympathyBinding.inflate(inflater, container, false)

        initRecyclerView()
        return binding.root


    }

    private fun initRecyclerView() {
        val list = viewModel.getPhrases(requireContext())
        adapter = SuggestedSympathyAdapter(this, viewModel.selectedItem,list)
        binding.recycler.adapter = adapter

    }

    override fun onItemSelected(position: Int, item: String) {
      viewModel.setSelectedText(item)
      viewModel.selectedItem = position
        dismiss()
    }


}
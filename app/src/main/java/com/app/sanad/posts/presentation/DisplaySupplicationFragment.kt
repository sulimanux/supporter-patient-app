package com.app.sanad.posts.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentDisplaySupplicationBinding


class DisplaySupplicationFragment : BaseFragment() {

    private lateinit var binding: FragmentDisplaySupplicationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDisplaySupplicationBinding.inflate(inflater, container, false)
        val supplication = DisplaySupplicationFragmentArgs.fromBundle(requireArguments()).supplication
        binding.textView.text = supplication.name
 binding.icBack.setOnClickListener {
     findNavController().popBackStack()
 }

        return binding.root


    }

}
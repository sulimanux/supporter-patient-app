package com.app.sanad.numbersHelping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentNumberHelpingBinding


class NumberHelpingFragment : BaseFragment() {


private  lateinit var  binding: FragmentNumberHelpingBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNumberHelpingBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = HelpingAdapterAdapter(numbersList())
        setupClickListener()
        return binding.root
    }

    private fun setupClickListener() {
        binding.iconBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun numbersList() = listOf(
        R.drawable.img_hepling1,
        R.drawable.img_helping2,
        R.drawable.img_helping3,
        R.drawable.img_helping4,
        R.drawable.img_helping5,
        R.drawable.img_helping6,
        R.drawable.img_helping7,
    )
}
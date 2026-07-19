package com.app.sanad.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentContactUsBinding


class ContactUsFragment: BaseFragment() {




    private lateinit var binding: FragmentContactUsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContactUsBinding.inflate(inflater, container, false)
        return binding.root
    }

}
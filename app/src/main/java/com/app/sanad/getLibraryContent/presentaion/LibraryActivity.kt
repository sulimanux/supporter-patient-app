package com.app.sanad.getLibraryContent.presentaion

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.base.BaseActivity
import com.app.sanad.databinding.ActivityLibraryBinding

@AndroidEntryPoint
class LibraryActivity : BaseActivity() {


    private lateinit var  binding: ActivityLibraryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}
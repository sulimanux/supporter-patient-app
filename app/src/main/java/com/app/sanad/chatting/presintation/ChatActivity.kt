package com.app.sanad.chatting.presintation

import android.os.Bundle
import com.app.sanad.base.BaseActivity
import com.app.sanad.databinding.ActivityChatingBinding

class ChatActivity : BaseActivity(){

   private lateinit var binding: ActivityChatingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
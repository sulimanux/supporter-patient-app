package com.app.sanad.users.supporter.main

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.base.BaseActivity
import com.app.sanad.databinding.ActivitySupporterScreenBinding
import com.app.sanad.app.presentation.MyApplication
import com.app.sanad.util.SUPPORTER
import com.app.sanad.util.log


@AndroidEntryPoint
class SupporterScreenActivity : BaseActivity() {

    private lateinit var binding: ActivitySupporterScreenBinding
    private lateinit var user: UserProfile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupporterScreenBinding.inflate(layoutInflater)
        sharedPreferences = (application as MyApplication).sharedPreferences
        user = sharedPreferences.getUserProfile()
        baseViewModel.chattingRepo.retrieveChattingListForSupporter()
        setContentView(binding.root)
        observeViewModel()
        initializeViews()
    }

    private fun observeViewModel() {

         if (sharedPreferences.getUserProfile().typeOfUser == SUPPORTER){
             appViewModel.postsRepo.countNotSeenBySupporter.observe(this) {count->
                 val menuItemId = R.id.posts_navigation
                 val badge = binding.navView.getOrCreateBadge(menuItemId)
                 badge.isVisible = count > 0
                 badge.number = appViewModel.postsRepo.countNotSeenBySupporter.value!!
             }
        }







        baseViewModel.chattingRepo.unseenMessagesCount.observe(this) { count ->
            val menuItemId = R.id.chatting_navigation
            val badge = binding.navView.getOrCreateBadge(menuItemId)
            badge.isVisible = count > 0
            badge.number = count
        }


    }


    fun initializeViews() {
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_supporter_screen)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.supporterHomeFragment -> {
                    log("supporterHomeFragment")
                    navController.popBackStack(R.id.supporterHomeFragment, false)
                    navController.navigate(R.id.supporterHomeFragment)
                    true
                }
                R.id.posts_navigation -> {
                    log("supporterProfileFragment")
                    navController.popBackStack(R.id.posts_navigation, false)
                    navController.navigate(R.id.posts_navigation)
                    true
                }
                R.id.supporterProfileFragment -> {
                    log("supporterProfileFragment")
                    navController.popBackStack(R.id.supporterProfileFragment, false)
                    navController.navigate(R.id.supporterProfileFragment)
                    true
                }
                R.id.chatting_navigation -> {
                    if (user.allowPrivateMessages!!) {
                        navController.popBackStack(R.id.chatting_navigation, false)
                        navController.navigate(R.id.chatting_navigation)
                    } else {
                        showToast(getString(R.string.you_do_not_have_access_to_send_a_private_message))
                    }
                    true
                }
                else -> false
            }
        }
    }



}
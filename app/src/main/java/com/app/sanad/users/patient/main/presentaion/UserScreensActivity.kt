package com.app.sanad.users.patient.main.presentaion

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseActivity
import com.app.sanad.databinding.ActivityUserScreensBinding
import com.app.sanad.util.log
@AndroidEntryPoint
class UserScreensActivity : BaseActivity() {

    private lateinit var binding:ActivityUserScreensBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        log("UserScreensActivity on Create called")
        super.onCreate(savedInstanceState)
        binding = ActivityUserScreensBinding.inflate(layoutInflater)
        setContentView(binding.root)
        baseViewModel.chattingRepo.retrieveChattingListForUser()
        observeViewModel()

        initializeViews()
    }



     fun initializeViews() {

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_user)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener { item ->
            log(item.itemId.toString())

            when (item.itemId) {

                R.id.patientHomeFragment -> {
                    navController.popBackStack(R.id.patientHomeFragment, false)
                    navController.navigate(R.id.patientHomeFragment)
                    true
                }
                R.id.profileFragment -> {
                    navController.popBackStack(R.id.profileFragment, false)
                    navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.chatting_navigation -> {
                    log("chatting_navigation")
                    navController.popBackStack(R.id.chatting_navigation, false)
                    navController.navigate(R.id.chatting_navigation)
                    true
                }
                R.id.calenderActivity -> {
                    log("calenderActivity")
                    navController.popBackStack(R.id.calenderActivity, false)
                    navController.navigate(R.id.calenderActivity)
                    true
                }
                else -> false
            }
        }
    }
    private fun observeViewModel() {
        baseViewModel. chattingRepo.unseenMessagesCount.observe(this) { count ->
            val menuItemId = R.id.chatting_navigation
            val badge = binding.navView.getOrCreateBadge(menuItemId)
            badge.isVisible = count > 0
            badge.number = count
        }
    }
}
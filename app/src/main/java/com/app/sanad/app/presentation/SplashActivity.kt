package com.app.sanad.app.presentation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.WindowManager
import com.app.sanad.R
import androidx.activity.viewModels

import com.app.sanad.app.presentation.MyApplication
import com.app.sanad.auth.presentation.AuthActivity
import com.app.sanad.base.BaseActivity
import com.app.sanad.databinding.ActivitySplashBinding
import com.app.sanad.users.supporter.main.SupporterScreenActivity
import com.app.sanad.users.patient.main.presentaion.UserScreensActivity
import com.app.sanad.util.SUPPORTER
import com.app.sanad.util.IS_USER_LOGGED
import com.app.sanad.util.LANGUAGE
import com.app.sanad.util.USER
import java.util.Locale

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private  lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        sharedPreferences = (application as MyApplication).sharedPreferences

        setContentView(binding.root)
        initializeViews()
        setUpListeners()
    }
    private fun setUpListeners() {
        binding.tryAgainBt.setOnClickListener {
            checkConnection()
        }
    }

    private fun initializeViews() {


        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        val background: Drawable =
            this@SplashActivity.getResources().getDrawable(R.drawable.background53)
        window.setBackgroundDrawable(background)
        setLocale(if (sharedPreferences.getString(LANGUAGE) == "ar") "ar" else "en")
        isLogged()

    }

    private fun isLogged() {
        if (sharedPreferences.getBoolean(IS_USER_LOGGED)) {
            checkConnection()

        } else {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }

    private fun checkConnection() {
     if(isConnected()){
//         Handler().postDelayed({
             appViewModel.setUpListeners()
             getTypeUser()
//         }, 2000)

     }else{
         showToast(getString(R.string.no_internet_connection))
     }
    }


    private fun getTypeUser() {

          when(sharedPreferences.getUserProfile().typeOfUser){
              USER -> {
                  startActivity(Intent(this, UserScreensActivity::class.java))
              }
              SUPPORTER -> {
                  startActivity(Intent(this, SupporterScreenActivity::class.java))
              }
          }
            finish()
    }



    fun setLocale(lang: String) {

        val resources: Resources = resources
        val dm: DisplayMetrics = resources.displayMetrics
        val conf: Configuration = resources.configuration
        conf.setLocale(Locale(lang))
        if (lang == "ar") {
            conf.setLayoutDirection(Locale(lang))
        } else {
            conf.setLayoutDirection(Locale.ENGLISH)
        }
        resources.updateConfiguration(conf, dm)


    }




}
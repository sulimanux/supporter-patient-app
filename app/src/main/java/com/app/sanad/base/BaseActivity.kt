package com.app.sanad.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ParametersBuilder
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.app.presentation.MyApplication
import com.app.sanad.util.SharedPreferencesManager
import com.google.firebase.analytics.logEvent
import com.app.sanad.R
import com.app.sanad.app.presentation.AppViewModel
import com.app.sanad.util.NetworkMonitor
import com.app.sanad.util.log

@AndroidEntryPoint

open class BaseActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferencesManager
     val baseViewModel : BaseViewModel by viewModels()
     val appViewModel : AppViewModel by viewModels()
     lateinit var progressDialog: Dialog

    var startTime:Long = 0


    override fun onStart() {
        super.onStart()
        startTime = System.currentTimeMillis()

        sharedPreferences = (application as MyApplication).sharedPreferences

    }
    fun showProgressDialog() {
        if (progressDialog.ownerActivity == null){
            progressDialog = Dialog(this.baseContext)
        }
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.setCanceledOnTouchOutside(false)
        val window = progressDialog.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.show()
    }


    fun dismissProgressDialog() {
        if (progressDialog.isShowing){
            progressDialog.dismiss()
        }
    }

    fun durationAsLong(): Long  = System.currentTimeMillis() - startTime

    fun isConnected():Boolean{
        val networkMonitor = NetworkMonitor(context = this.baseContext)
        return  networkMonitor.isConnected()
    }


     fun duration(duration: Long): String {
         var duration = duration.toFloat()
         var durationStr = "ms"

         if (duration >= 1000 && duration < 60000) {
             duration = duration / 1000
             durationStr = "s"
         }else if(duration >= 60000 ){
             duration = duration / 60000
             durationStr = "m"
         }
         val formatted = String.format("%.1f", duration)
         durationStr = "$formatted $durationStr"
         log(durationStr)
         return  durationStr
     }

    fun logEvent(eventName: String, block: ParametersBuilder.() -> Unit) {

        FirebaseAnalytics.getInstance(baseContext).logEvent(eventName, block)
    }

    fun showToast(message: String) {
        val layoutInflater = layoutInflater
        val layout = layoutInflater.inflate(R.layout.custom_toast_layout, null)
        val textViewMessage = layout.findViewById<TextView>(R.id.text)
        textViewMessage.text = message
        with(Toast(baseContext)) {
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }
}
package com.app.sanad.base

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ParametersBuilder
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.auth.presentation.AuthActivity
import com.app.sanad.databinding.DialogBinding
import com.app.sanad.databinding.DialogConfirmLogoutBinding
import com.app.sanad.util.NetworkMonitor
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.log
import com.google.firebase.analytics.logEvent

@AndroidEntryPoint
open class BaseFragment: Fragment() {

    private  var startTime:Long = 0
    private lateinit var progressDialog: Dialog
    private lateinit var temporallyDialog: Dialog
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    val baseViewModel: BaseViewModel by viewModels()
    lateinit var sharedDialog: Dialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        progressDialog = Dialog(requireContext())
    }

    override fun onStart() {
        log("onStart BaseFragment")
         firebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity())
         startTime = System.currentTimeMillis()
        super.onStart()
    }

    fun durationAsLong(): Long  = System.currentTimeMillis() - startTime

    fun duration(): String {
        var duration = (System.currentTimeMillis() - startTime).toFloat()
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

    fun logEvent(eventName: String , params : ParametersBuilder.() -> Unit ){
        firebaseAnalytics.logEvent(eventName, params)
    }



    fun showProgressDialog() {
         if (progressDialog.ownerActivity == null){
             progressDialog = Dialog(requireContext())
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

    fun copyTextToClipboard( text: String) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        showToast(getString(R.string.copied))
    }
    fun showNoInternetSnackBar(view: View) {
        Snackbar.make(view, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
            .show()
    }

    fun showToast(message: String) {
        val layoutInflater = layoutInflater
        val layout = layoutInflater.inflate(R.layout.custom_toast_layout, null)
        val textViewMessage = layout.findViewById<TextView>(R.id.text)
        textViewMessage.text = message
        with(Toast(context)) {
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }
    fun showTemporallyDialog(title: String, message: String,imageResource:Int?=null ,textButton: String,callBack:() -> Unit) {
        temporallyDialog = Dialog(requireContext())
        temporallyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogBinding.inflate(layoutInflater)
        dialogBinding.title.text = title
        dialogBinding.title.text = title
        imageResource?.let {dialogBinding.image.setImageResource(it) }
        dialogBinding.message.text = message
        dialogBinding.button.text = textButton
        temporallyDialog.setContentView(dialogBinding.root)
        temporallyDialog.setCanceledOnTouchOutside(true)
        val window = temporallyDialog.window

        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val layoutParams = attributes
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = layoutParams
        }
        temporallyDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.button.setOnClickListener {
            temporallyDialog.dismiss()
//                showProgressDialog()
            callBack()
        }
        dialogBinding.icClose.setOnClickListener {
            temporallyDialog.dismiss()
        }
        temporallyDialog.show()
    }

    fun isConnected():Boolean{
        val networkMonitor = NetworkMonitor(requireActivity())
        return  networkMonitor.isConnected()
    }

    fun showDialogConfirmLogout(sharedPreferences: SharedPreferencesManager) {
        sharedDialog = Dialog(requireContext())
        sharedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogConfirmLogoutBinding.inflate(layoutInflater)
        sharedDialog.setContentView(dialogBinding.root)
        sharedDialog.setCanceledOnTouchOutside(true)

        val window = sharedDialog.window
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val layoutParams = attributes
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = layoutParams
        }
        dialogBinding.icClose.setOnClickListener {
            sharedDialog.dismiss()
        }
        dialogBinding.btnLogout.setOnClickListener {
            logOut()
            sharedDialog.dismiss()
        }
        dialogBinding.btnCancel.setOnClickListener {
            sharedDialog.dismiss()
        }
        sharedDialog.show()
    }



    private fun logOut() {
         baseViewModel.logOut(requireActivity())
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }


}
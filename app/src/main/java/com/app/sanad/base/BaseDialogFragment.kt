package com.app.sanad.base

import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.util.NetworkMonitor

@AndroidEntryPoint
open class BaseDialogFragment : DialogFragment() {

     lateinit var localDialog: Dialog

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

    fun isConnected():Boolean{
        val networkMonitor = NetworkMonitor(requireActivity())
        return  networkMonitor.isConnected()
    }
    fun showNoInternetSnackBar(view: View) {
        Snackbar.make(view, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
            .show()
    }
}
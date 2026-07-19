package com.app.sanad.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, intent: Intent?) {

        if(Intent.ACTION_BOOT_COMPLETED == intent?.action){

        }
    }


}
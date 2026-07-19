package com.app.sanad.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkMonitor (context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isConnected() = connectivityManager.activeNetwork != null

    fun observeNetworkStatus(): Flow<Boolean> = callbackFlow {
        log("observeNetworkStatus ")

        log(connectivityManager.toString())
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                log("                trySend(true)\n ")
                trySend(true)
            }

            override fun onLost(network: Network) {
                log("                trySend(false)\n ")

                trySend(false)
            }
        }
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }

}




package com.app.sanad.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.app.sanad.R
import com.app.sanad.R.string.have_you_had_the_opportunity_to_help_someone_new_how_do_you_feel_about_that
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}


fun errorSnackBar( view: View,text: String) {
    view?.let { Snackbar.make(it, text, Snackbar.LENGTH_LONG).show() }
}

fun localizeNumber(number: Int, context: Context): String {
    val locale = context.resources.configuration.locales[0]
    val numberFormat = NumberFormat.getInstance(locale)
    return numberFormat.format(number)
}


fun log(text:String,tag:String = "TAG" ) {
    Log.e(tag , "=================================================")
    Log.e(tag , text)
}



fun durationAsString(duration: Long): String {
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

fun getDateAsString(currentTimeMillis:Long):String{
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(currentTimeMillis))
}

fun dateTime(currentTimeMillis:Long):String{
    val sdf = SimpleDateFormat("dd/MM/yyyy  hh:mm a", Locale.getDefault())
    return sdf.format(Date(currentTimeMillis))
}



fun isValidInput(input:String) = input.isNotBlank()


fun loadImage(context:Context,imageURL: String?,imageView: ImageView) {

    Glide.with(context).load(imageURL).into(imageView)
}



fun getGratitudeQuestionsList(context: Context): List<String> {
    return listOf(
        context.getString(R.string.q1),
        context.getString(R.string.q2),
        context.getString(R.string.q3),
        context.getString(R.string.q4),
        context.getString(R.string.q5),
        context.getString(R.string.q6),

//        context.getString(have_you_had_the_opportunity_to_help_someone_new_how_do_you_feel_about_that),
//        context.getString(R.string.what_is_the_good_thing_that_happened_to_you_this_week),
//        context.getString(R.string.what_is_the_nice_thing_someone_did_for_you_recently),
//        context.getString(R.string.who_is_the_person_who_is_always_with_you_and_how_do_you_feel_about_them),
    )
}








package com.app.sanad.getLibraryContent.data

import android.content.Context
import com.app.sanad.R
import com.app.sanad.getLibraryContent.data.DepressionMisconception
import javax.inject.Inject

class DepressionMisconceptionRepo @Inject constructor (){

    fun listMisconceptions(context: Context) = listOf(
        DepressionMisconception(
            context.getString(R.string.misconception_1_title),
            context.getString(R.string.misconception_1_false),
            context.getString(R.string.misconception_1_truth)
        ),
        DepressionMisconception(
            context.getString(R.string.misconception_2_title),
            context.getString(R.string.misconception_2_false),
            context.getString(R.string.misconception_2_truth)
        ),
        DepressionMisconception(
            context.getString(R.string.misconception_3_title),
            context.getString(R.string.misconception_3_false),
            context.getString(R.string.misconception_3_truth)
        ),
        DepressionMisconception(
            context.getString(R.string.misconception_4_title),
            context.getString(R.string.misconception_4_false),
            context.getString(R.string.misconception_4_truth)
        ),
        DepressionMisconception(
            context.getString(R.string.misconception_5_title),
            context.getString(R.string.misconception_5_false),
            context.getString(R.string.misconception_5_truth)
        ),
        DepressionMisconception(
            context.getString(R.string.misconception_6_title),
            context.getString(R.string.misconception_6_false),
            context.getString(R.string.misconception_6_truth)
        ),
        DepressionMisconception(
            context.getString(R.string.misconception_7_title),
            context.getString(R.string.misconception_7_false),
            context.getString(R.string.misconception_7_truth)
        ),
        DepressionMisconception(
            context.getString(R.string.misconception_8_title),
            context.getString(R.string.misconception_8_false),
            context.getString(R.string.misconception_8_truth)
        ),
        DepressionMisconception(
            context.getString(R.string.misconception_9_title),
            context.getString(R.string.misconception_9_false),
            context.getString(R.string.misconception_9_truth)
        ),
        DepressionMisconception(
            context.getString(R.string.misconception_10_title),
            context.getString(R.string.misconception_10_false),
            context.getString(R.string.misconception_10_truth)
        ),
        DepressionMisconception(
            context.getString(R.string.misconception_11_title),
            context.getString(R.string.misconception_11_false),
            context.getString(R.string.misconception_11_truth)
        ),
    )

}
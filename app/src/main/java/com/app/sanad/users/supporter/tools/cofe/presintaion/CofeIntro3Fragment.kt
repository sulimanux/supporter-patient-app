package com.app.sanad.users.supporter.tools.cofe.presintaion

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.databinding.FragmentCofeIntro3Binding


class CofeIntro3Fragment : Fragment() {

    lateinit var binding: FragmentCofeIntro3Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCofeIntro3Binding.inflate(inflater, container, false)
        styleText("ar")
        binding.constraintNext.setOnClickListener {
            findNavController().navigate(R.id.action_cofeIntro3Fragment_to_cofePurposeFragment)
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.exit.setOnClickListener {
            activity?.finish()
        }
        return  binding.root
    }
    private fun styleText(lang:String) {
        val text = getString(R.string.through_this_simple_and_gentle)
        val spannable = SpannableString(text)
        if(lang == "ar") text.indexOf("التعرّف") else text.indexOf("identify")
        if(lang == "ar") text.indexOf("هل") else  text.indexOf("Does")
//        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#204167")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannable.setSpan(BackgroundColorSpan(Color.parseColor("#c2e3f8")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannable.setSpan(AbsoluteSizeSpan(16, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        spannable.setSpan(StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.text.text = spannable
    }


}
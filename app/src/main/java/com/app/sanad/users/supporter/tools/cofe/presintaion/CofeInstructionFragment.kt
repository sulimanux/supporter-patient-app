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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.app.sanad.R
import com.app.sanad.databinding.FragmentCofeInstructionBinding


class CofeInstructionFragment : Fragment() {

    private lateinit var binding:FragmentCofeInstructionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCofeInstructionBinding.inflate(layoutInflater,container,false)

      binding.constraintNext.setOnClickListener {
          findNavController().navigate(R.id.action_cofeInstructionFragment_to_friendMessageFragment)
      }
     binding.back.setOnClickListener {
         findNavController().popBackStack()
     }
        binding.exit.setOnClickListener {
            activity?.finish()
        }
        styleText("ar")
        return  binding.root
    }


    private fun styleText(lang:String) {
        val text = getString(R.string.in_the_thought_restructuring)
        val spannable = SpannableString(text)
        val start = if(lang == "ar") text.indexOf("رفيقًا") else text.indexOf(" supportive")
        val end =  if(lang == "ar") text.indexOf("في لحظة") else  text.indexOf(", but")

        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#204167")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(BackgroundColorSpan(Color.parseColor("#c2e3f8")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(AbsoluteSizeSpan(16, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.text2.text = spannable
    }

}
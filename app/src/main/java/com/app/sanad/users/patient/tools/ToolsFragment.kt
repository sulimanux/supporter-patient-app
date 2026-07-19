package com.app.sanad.users.patient.tools

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.DialogBreathIntroBinding
import com.app.sanad.databinding.FragmentSupplicationsIntroBinding
import com.app.sanad.databinding.FragmentToolsBinding
import com.app.sanad.users.patient.tools.coffeeideas.presentaion.CofeActivity
import com.app.sanad.users.patient.tools.coffeeideas.presentaion.CofeViewModel
import com.app.sanad.users.patient.tools.supplications.prisentation.SupplicationViewModel
import com.app.sanad.util.LANGUAGE
import com.app.sanad.util.log
import kotlin.getValue

@AndroidEntryPoint
class ToolsFragment : BaseFragment() {

    private lateinit var binding: FragmentToolsBinding
    private val viewModel: SupplicationViewModel by viewModels()
    private val coffeeViewModel : CofeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToolsBinding.inflate(inflater, container, false)
        changeBadgeGravityBasedLang()
        setupClickListener()
        coffeeViewModel.listenToIdeaChanges()
        observeViewModel()
        return binding.root
    }

    private fun changeBadgeGravityBasedLang(){
        val lang = coffeeViewModel.sharedPreferences.getString(LANGUAGE, "ar")
        if (lang == "ar"){
          val params =   binding.badge.layout.layoutParams  as FrameLayout.LayoutParams
            params.gravity = Gravity.START
            binding.badge.layout.layoutParams = params
        }
    }

    private fun observeViewModel() {
        coffeeViewModel.userIdea.observe(viewLifecycleOwner) {
          log("UserData has Changd to => $it")
           if (it != null && it.response!!.isNotEmpty()){
               if ( it.seenByPatient!! ){
                 binding.badge.layout.visibility = View.GONE
               }else{
                   binding.badge.layout.visibility = View.VISIBLE
               }
           }else{
               binding.badge.layout.visibility = View.GONE
           }
        }

    }


    private fun setupClickListener() {

        binding.icBack.setOnClickListener {
            activity?.finish()

        }

        binding.imageCofe.setOnClickListener {
            startActivity(Intent(requireContext(), CofeActivity::class.java))
        }
        binding.imageSupplications.setOnClickListener {
            showDialog()

        }
        binding.imageBreath.setOnClickListener {
            showBreathIntroDialog(R.drawable.breath_intro){
                findNavController().navigate(R.id.action_toolsFragment_to_choosingBreathDurationFragment)
            }

        }

        binding.imageGratitude.setOnClickListener {
            showBreathIntroDialog(R.drawable.gratitude_intro){
                findNavController().navigate(R.id.action_toolsFragment_to_gratitudeFragment2)
            }

        }

    }


    fun showBreathIntroDialog(image: Int, function: () -> Unit) {
        val sharedDialog = Dialog(requireContext())
        sharedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogBreathIntroBinding.inflate(layoutInflater)
        dialogBinding.image.setImageResource(image)
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
        dialogBinding.start.setOnClickListener {
            function()
            sharedDialog.dismiss()
        }

        sharedDialog.show()
    }


    fun showDialog() {
        val  sharedDialog = Dialog(requireContext())
        sharedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = FragmentSupplicationsIntroBinding.inflate(layoutInflater)
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
        dialogBinding.start.setOnClickListener {
            findNavController().navigate(R.id.action_toolsFragment_to_mainSupplicationsFragment3)
            sharedDialog.dismiss()
        }

        sharedDialog.show()
    }



}
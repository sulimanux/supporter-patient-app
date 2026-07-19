package com.app.sanad.users.patient.main.presentaion

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.getLibraryContent.presentaion.LibraryActivity
import com.app.sanad.databinding.DialogPreMoodSelectionBinding
import com.app.sanad.databinding.FragmentUserHomeBinding
import com.app.sanad.users.patient.calender.presentaion.CalenderActivity
import com.app.sanad.users.patient.dailyprogram.data.entity.CurrentDay
import com.app.sanad.users.patient.dailyprogram.presentaion.DailyProgramActivity
import com.app.sanad.users.patient.moodTracking.presentaion.activties.PostAssessmentActivity
import com.app.sanad.users.patient.moodTracking.presentaion.activties.PreAssessmentActivity
import com.app.sanad.users.patient.tools.UserToolsActivity
import com.app.sanad.users.patient.tools.coffeeideas.presentaion.CofeViewModel
import com.app.sanad.util.Temp
import com.app.sanad.util.UserDataListener
import com.app.sanad.util.loadImage
import com.app.sanad.util.log
import kotlin.getValue


@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentUserHomeBinding
    private val viewModel: UserViewModel by viewModels()
    private val coffeeViewModel : CofeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserHomeBinding.inflate(inflater,container, false)
        log("on create in home fragment ")
        onBoarding()
        setupClickListener()
        initializeViews()
        isPermissionGranted()
        UserDataListener.addListener(viewModel.sharedPreferencesManager, viewModel.firestore)
        baseViewModel.updateUserPropertyAnalytics()
        coffeeViewModel.listenToIdeaChanges()
        log("HomeFragment =>   ${viewModel.userProfile}" )
        observeViewModel()
        return binding.root
    }

    private fun onBoarding() {

        if (viewModel.isFirstTime()) {
            OnBoardingFragment().show(childFragmentManager,null)
            viewModel.updateFirstTimeState()
        }

    }

    override fun onStart() {
        log("on Start in home fragment ")
        setStatusOfCurrentTask(viewModel.currentTask())
        super.onStart()
    }

    fun initializeViews() {
        loadImage(requireActivity(),viewModel.userProfile.imageUser,binding.imageUser)
        binding.nameUser.text = viewModel.userProfile.name
    }

     fun setupClickListener() {
// binding.imageNotif.setOnClickListener {
//     findNavController().navigate(R.id.action_patientHomeFragment_to_notificationsFragment)
// }
        binding.dailyProgram.setOnClickListener {
            Temp.dailyProgramEngagement = 0L
            val task = viewModel.currentTask()
            if (task.status?.day == 3) {
                showToast(getString(R.string.there_are_no_tasks_for_today))
                return@setOnClickListener
              }
                if (task.status?.preChecked!! ){

                if (task.status?.isDayProgramCompleted!!){
                    showToast(getString(R.string.you_haven_t_completed_the_post_mood_tracking_yet))
                    startActivity(Intent(requireActivity(), PostAssessmentActivity::class.java))
                 }
                else{
                     startActivity(Intent(requireActivity(), DailyProgramActivity::class.java))
                 }
            }else{

                showMoodTrackingDialog()
            }
        }
         binding.statistics.setOnClickListener() {
             val userId = viewModel.userProfile.id
             val action = HomeFragmentDirections.actionPatientHomeFragmentToTrackingMoodFragment(userId!!)
             findNavController().navigate(action)

        }

        binding.rootTools.setOnClickListener{
            startActivity(Intent(requireActivity(), UserToolsActivity::class.java))
        }

        binding.rootEducationalContent.setOnClickListener{
            startActivity(Intent(requireActivity(), LibraryActivity::class.java))
        }
        binding.helpNumbers.setOnClickListener {
            findNavController().navigate(R.id.action_patientHomeFragment_to_numberHelpingFragment2)
        }
        binding.containerDailyPlanner.setOnClickListener {
            startActivity(Intent(requireActivity(), CalenderActivity::class.java))
        }
    }


    private fun setStatusOfCurrentTask(currentTask: CurrentDay?){
        currentTask?.let {
            val status= it.status
            binding.currentDayLevel.text= buildString {
                append(getString(R.string.day, status?.day))
            }

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
    private fun showMoodTrackingDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogPreMoodSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        val window = dialog.window
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val layoutParams = attributes
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.8).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = layoutParams
        }

        dialogBinding.icClose.setOnClickListener {


            dialog.dismiss()
        }
        dialogBinding.button.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(requireContext(), PreAssessmentActivity::class.java))
        }
        dialog.show()
    }

    private fun isPermissionGranted() {
        when {

            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED -> {
                log("the Permission is granted")
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.POST_NOTIFICATIONS) -> {
                log("shouldShowRequestPermissionRationale")

            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.POST_NOTIFICATIONS)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
            } else {

            }
        }








}
package com.app.sanad.users.patient.supporters.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.databinding.FragmentSupporterDetailsBinding
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.base.BaseFragment
import com.app.sanad.util.loadImage

@AndroidEntryPoint
class SupporterDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentSupporterDetailsBinding
    private val viewModel: SupporterViewModel by viewModels()

    private lateinit var supporter: UserProfile
    private  var currentPermissions = mutableListOf(false,false,false)
    private  val modifiedPermissions = mutableListOf(false,false,false)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupporterDetailsBinding.inflate(inflater, container, false)
        initializeViews()
        setupClickListener()
        observeViewModel()
        return binding.root
    }
     fun initializeViews() {
        val args: SupporterDetailsFragmentArgs by navArgs()
        supporter = args.supporter
        setUpUiSupporter(args.supporter)
        setUpSwitchesListener()

    }

    private fun setUpSwitchesListener() {
     binding.permissions.viewDailyProgramDetails.setOnCheckedChangeListener { _, isChecked ->updateSwitching(0,isChecked)}
     binding.permissions.viewMoodTrackingDetails.setOnCheckedChangeListener { _, isChecked ->updateSwitching(1,isChecked)}
     binding.permissions.allowPrivateMessages.setOnCheckedChangeListener { _, isChecked ->updateSwitching(2,isChecked)}
    }
    private fun updateSwitching(index:Int, isChecked:Boolean){
        modifiedPermissions[index] = isChecked
        if (currentPermissions == modifiedPermissions){
            binding.save.visibility = View.GONE
        }else{
            binding.save.visibility = View.VISIBLE
        }
    }

   private  fun setupClickListener() {

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.icMore.setOnClickListener {
            showPopupMenu(it)
        }
        binding.save.setOnClickListener {
            if(isConnected()){
                showProgressDialog()
                viewModel.updateSupporterPermissionsRemotely(supporter.id!!,modifiedPermissions, requireActivity())
            }else{
                showNoInternetSnackBar(binding.root)
            }
        }
    }


    private fun setUpUiSupporter(supporter: UserProfile) {

        binding.nameSupporter.text = supporter.name
        loadImage(requireActivity(), supporter.imageUser, binding.imageUser)
        checkStatus()

            if (supporter.allowDailyProgramDetails!!){
                binding.permissions.viewDailyProgramDetails.isChecked =true
                currentPermissions[0] = true
                modifiedPermissions[0] = true
            }
            if (supporter.allowMoodTrackingDetails!!) {
                binding.permissions.viewMoodTrackingDetails.isChecked =true
                currentPermissions[1] = true
                modifiedPermissions[1] = true
            }
            if (supporter.allowPrivateMessages!!) {
                binding.permissions.allowPrivateMessages.isChecked =true
                currentPermissions[2] = true
                modifiedPermissions[2] = true
            }
    }

    private fun checkStatus() {
        if (supporter.status == 1) {
            binding.status.background = getDrawable(requireActivity(), R.drawable.corner_light_blue)

            binding.status.text = requireActivity().getString(R.string.active)
        } else {
            binding.status.background = getDrawable(requireActivity(), R.drawable.corner_red)
            binding.status.text = requireActivity().getString(R.string.not_active)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireActivity(), view)
        popupMenu.inflate(R.menu.settings_support_menu)
        if (supporter.status == 0) {
            popupMenu.menu.findItem(R.id.menu_suspend_temporarily).title =
                getString(R.string.remove_temporary_suspend)
        }

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_suspend_temporarily -> {
                    isSupporterSuspend()
                }
                else ->
                    showTemporallyDialog(
                    getString(R.string.remove_supporter),
                    getString(R.string.confirm_remove_supporter),
                        R.drawable.ic_user_profile,
                    getString(R.string.remove_supporter_confirmation),
                ){}
            }
            true
        }
        popupMenu.show()
    }

    private fun isSupporterSuspend() {
        if (supporter.status == 0) {
            showTemporallyDialog(
                getString(R.string.remove_temporary_suspend),
                getString(R.string.confirm_remove_temporary_suspend),
                R.drawable.ic_user_profile,
                getString(R.string.remove_temporary_suspend),
            ){
                updateSupporterStatus(1)
            }
        } else {
            showTemporallyDialog(
                getString(R.string.suspend_temporarily),
                getString(R.string.confirm_temporarily_suspend_supporter),
                R.drawable.ic_user_profile,
                getString(R.string.suspend_temporarily)

            ){
                updateSupporterStatus(0)
            }
        }

    }

    private fun updateSupporterStatus(status: Int) {
   if(isConnected()){
       showProgressDialog()
       viewModel.changeStatusOfSupporter(supporter.id!!,status)
   }else{
       showNoInternetSnackBar(binding.root)
   }
    }


    private fun observeViewModel() {

        viewModel.updateStatus.observe(viewLifecycleOwner) {
            dismissProgressDialog()
            it?.let {
                if (it.isEmpty()) {
                    showToast(getString(R.string.supporter_status_changed))
                    updateUi()
                }else{
                    showToast(getString(R.string.failed_try_again_later))
                }
            }
        }

        viewModel.status.observe(viewLifecycleOwner) {
            dismissProgressDialog()
            it?.let {
                showToast(it)
                binding.save.visibility = View.GONE
            }
        }
    }

    private fun updateUi() {
        supporter.status = if (supporter.status == 0) 1 else 0
        checkStatus()
    }


}
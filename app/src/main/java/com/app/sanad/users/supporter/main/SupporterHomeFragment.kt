package com.app.sanad.users.supporter.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentSupporterHomeBinding
import com.app.sanad.getLibraryContent.presentaion.LibraryActivity
import com.app.sanad.posts.presentation.PostsActivity
import com.app.sanad.users.patient.tools.coffeeideas.presentaion.CofeViewModel
import com.app.sanad.util.UserDataListener
import com.app.sanad.util.loadImage
import com.app.sanad.util.localizeNumber
import com.app.sanad.util.log
import kotlin.getValue

class SupporterHomeFragment : BaseFragment() {

    private  val viewModel : CareMainViewModel by viewModels()
    private val coffeeViewModel : CofeViewModel by activityViewModels()

    private lateinit var  binding: FragmentSupporterHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupporterHomeBinding.inflate(inflater, container, false)
        initializeViews()
        UserDataListener.addListener(viewModel.sharedPreferencesManager, viewModel.firestore)
        UserDataListener.addListenerForPartner(viewModel.sharedPreferencesManager, viewModel.firestore)
        setupClickListener()
        observeViewModel()
        return binding.root
    }


    private fun observeViewModel() {
        viewModel.unReadCountNotification.observe (viewLifecycleOwner){
            if (it > 0){
                binding.badgePosts.layout.visibility = View.VISIBLE
                binding.badgePosts.count.text = localizeNumber(it, requireContext())
            }else{
                binding.badgePosts.layout.visibility = View.GONE
            }
        }






    }

    private fun initializeViews() {
        binding.nameUser.text = viewModel.user().name
        log(viewModel.user().email!!)
        loadImage(requireActivity(),viewModel.user().imageUser,binding.imageUser)
    }


    private fun setupClickListener() {

        binding.enter.setOnClickListener {
            findNavController().navigate(R.id.action_supporterHomeFragment_to_usersFragment)
        }

        binding.posts.setOnClickListener {
            findNavController().navigate(R.id.action_supporterHomeFragment_to_notificationsFragment2)

//            startActivity(Intent(requireActivity(), PostsActivity::class.java))
        }
        binding.rootTools.setOnClickListener {
         findNavController().navigate(R.id.action_supporterHomeFragment_to_supporterToolsFragment)
        }
        binding.helpNumbers.setOnClickListener {
            findNavController().navigate(R.id.action_supporterHomeFragment_to_numberHelpingFragment)
        }

        binding.rootEducationalContent.setOnClickListener {
            startActivity(Intent(requireActivity(), LibraryActivity::class.java))
        }

    }

}
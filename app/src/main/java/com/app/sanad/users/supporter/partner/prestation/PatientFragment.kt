package com.app.sanad.users.supporter.partner.prestation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentPatientBinding
import com.app.sanad.posts.presentation.PostsActivity
import com.app.sanad.util.LANGUAGE
import com.app.sanad.util.MOOD
import com.app.sanad.util.PROGRAM
import com.app.sanad.util.SHARING
import com.app.sanad.util.data.itemList
import com.app.sanad.util.loadImage
import com.app.sanad.util.log

class PatientFragment : BaseFragment(), ItemClickListener {


    private lateinit var binding: FragmentPatientBinding
    private val viewModel: PartnerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientBinding.inflate(inflater, container, false)
        retrievePartner()
        setUpRecyclerData()
        setupClickListener()

        return binding.root
    }



    private fun setupClickListener() {

        binding.iconBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.messaging.setOnClickListener {

            if (viewModel.user.allowPrivateMessages!! ) {
                navigateToChatWithPatient(viewModel.partner().name!!,viewModel.partner().id!!,viewModel.partner().imageUser!!)
            }else{
                showToast(getString(R.string.you_do_not_have_access_to_send_a_private_message))
            }
        }

    }

    private fun navigateToChatWithPatient(name: String, idPartner: String, urlImage: String) {
        val action = PatientFragmentDirections.actionUsersFragmentToChatFragment(idPartner,urlImage,name)
        findNavController().navigate(action)
    }



    private fun retrievePartner() {
        val partner = viewModel.partner()
        binding.name.text = partner.name
        loadImage(requireActivity(), partner.imageUser, binding.imageView)
    }

    private fun setUpRecyclerData() {
        val adapter =
            RecyclerAdapter(itemList(), viewModel.sharedPreferences.getString(LANGUAGE),this)
        val gridLayoutManager = GridLayoutManager(context, 2)
        binding.recycler.layoutManager =
            gridLayoutManager
        binding.recycler.adapter = adapter

    }

    override fun onItemClick(flag: String) {

        when(flag){
            PROGRAM -> {
                if (viewModel.user.allowDailyProgramDetails!! ) {
                   val  currentDay =   viewModel.partner().currentDay
                    log(" onItemClick , currentDay => $currentDay")
                   val action = PatientFragmentDirections.actionUsersFragmentToMyPointsFragment2(currentDay!! )
                   findNavController().navigate(action)
                }else{
                    showToast(getString(R.string.you_do_not_have_permission_to_view_the_daily_program))
                }
            }
            SHARING -> {
                startActivity(Intent(requireActivity(), PostsActivity::class.java))
            }
            MOOD -> {
                if (viewModel.user.allowMoodTrackingDetails!!) {
                    val userId = viewModel.partner().id
                    val action = PatientFragmentDirections.actionUsersFragmentToTrackingMoodFragment2(userId!!)
                    findNavController().navigate(action)
                }else{
                    showToast(getString(R.string.you_do_not_have_access_to_the_mood))
                }
            }
        }


        }
    }
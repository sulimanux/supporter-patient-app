package com.app.sanad.notifications.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.chatting.presintation.ChatActivity
import com.app.sanad.databinding.FragmentNotificationsBinding
import com.app.sanad.databinding.FragmentSuggestionsBinding
import com.app.sanad.notifications.data.entities.Notification
import com.app.sanad.notifications.data.entities.NotificationsEnum
import com.app.sanad.posts.presentation.PostsActivity
import com.app.sanad.users.supporter.tools.cofe.presintaion.SupportCofeActivity
import com.app.sanad.users.patient.moodTracking.presentaion.viewmodels.MoodTrackingViewModel
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.log
import kotlin.getValue

@AndroidEntryPoint

class NotificationsFragment : BaseFragment(), NotificationsAdapter.OnItemClicked {

    private lateinit var binding: FragmentNotificationsBinding

    @javax.inject.Inject
    lateinit var sharedPreferences: SharedPreferencesManager

    private val viewModel: NotificationsViewModel by viewModels()
    private lateinit var adapter: NotificationsAdapter
    /**
     * Inflates view and prepares UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        // check internet connection
        checkInternetConnection()
        setupClickListener()
        return binding.root
    }

    private fun checkInternetConnection() {
        if (isConnected()) {
            showProgressDialog()
            setUpListener()
    }else{
            binding.noInternetLayout.swipeRefresh.visibility = View.VISIBLE
            binding.contentList.visibility = View.GONE
            binding.noItems.visibility = View.GONE
        }
    }

    private  fun setupClickListener(){

        binding.noInternetLayout.tryAgainBt.setOnClickListener {
            binding.noInternetLayout.swipeRefresh.visibility = View.GONE
            checkInternetConnection()
        }

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.icMarkAllRead.setOnClickListener {
            viewModel.markAllAsRead()
        }

    }

    private fun setUpListener() {
        adapter = NotificationsAdapter(sharedPreferences, {
            viewModel.delete(baseViewModel.currentUserProfile().id!!, it.id)

        }, this)

        binding.recyclerView.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.notifications.observe (viewLifecycleOwner) {
                dismissProgressDialog()
               if (it.isEmpty()){
                   binding.contentList.visibility = View.GONE
                   binding.noItems.visibility = View.VISIBLE
               }else{
                   binding.contentList.visibility = View.VISIBLE
                   binding.noItems.visibility = View.GONE
                   log("notifications => $it")
                   adapter.submitList(it)
               }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stop()
    }

    override fun onItemClicked(notification: Notification) {
        if (notification.read == false){
            viewModel.markAsRead(notification.id)
        }
        when (notification.type) {
            NotificationsEnum.Sharing.toString() ->{
                startActivity(Intent(requireActivity(), PostsActivity::class.java))
            }
            NotificationsEnum.Coffee.toString() ->{
                startActivity(Intent(requireActivity(), SupportCofeActivity::class.java))

            }
            NotificationsEnum.Chat.toString() ->{
                startActivity(Intent(requireActivity(), ChatActivity::class.java))

            }
            NotificationsEnum.System.toString() ->{

            }
        }
    }
}
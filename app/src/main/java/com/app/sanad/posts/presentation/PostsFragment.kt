package com.app.sanad.posts.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentPostsBinding
import com.app.sanad.interfaces.ItemPostsClicked
import com.app.sanad.users.patient.tools.gratitude.data.entity.Gratitude
import com.app.sanad.getLibraryContent.data.LibraryContent
import com.app.sanad.posts.data.entity.Post
import com.app.sanad.model.Supplication
import com.app.sanad.util.GRATITUDE
import com.app.sanad.util.LIBRARY
import com.app.sanad.util.SUPPLICATIONS

@AndroidEntryPoint
class PostsFragment : BaseFragment(), ItemPostsClicked {

  private val viewModel: PostsViewModel by viewModels()
  private lateinit var binding: FragmentPostsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPostsBinding.inflate(inflater, container, false)
        checkInternetConnection()
        setupClickListener()
        return binding.root


    }

    private fun checkInternetConnection() {
        if (isConnected()) {
            showProgressDialog()
            binding.noItems.visibility = View.GONE
            viewModel.retrievePostsRemotely()
            observeViewModel()

        } else {
           binding.noInternetLayout.swipeRefresh.visibility = View.VISIBLE
        }
    }




    private fun setupClickListener() {
        binding.icBack.setOnClickListener {
          activity?.finish()
        }

        binding.noInternetLayout.tryAgainBt.setOnClickListener {
            binding.noInternetLayout.swipeRefresh.visibility = View.GONE
            checkInternetConnection()
        }
    }


    private fun observeViewModel() {

        viewModel.posts.observe(viewLifecycleOwner) { list ->
          if (list !=null && list.isNotEmpty()){
              updateUi(list.reversed())
         }else{
             binding.noItems.visibility = View.VISIBLE
             binding.contentList.visibility = View.GONE
         }
            dismissProgressDialog()
        }

    }

    private fun updateUi(data: List<Post>) {
            binding.recyclerView.adapter =
            PostsAdapter(data, requireActivity(), viewModel.sharedPreferences, this)
            binding.noItems.visibility = View.GONE
            binding.contentList.visibility = View.VISIBLE

    }




    override fun onItemClicked(post: Post) {
        when(post.type){
            GRATITUDE ->  navigateToGratitude(post.gratitude!!)
            SUPPLICATIONS -> navigateToSupplication(post.supplication)
            LIBRARY -> navigateToLibrary(post.libraryContent)
        }
    }

    override fun updateSeenBySupporter(post: Post) {
        viewModel.updateSupporterSeen(post)

    }

    private fun navigateToSupplication(supplication: Supplication?) {
        val action = PostsFragmentDirections.actionPostsFragmentToDisplaySupplicationFragment2(supplication!!)
        findNavController().navigate(action)
    }

    private fun navigateToLibrary(libraryContent: LibraryContent?) {

        findNavController().navigate(PostsFragmentDirections.actionPostsFragmentToDisplayArticleFragment2(libraryContent!!))
    }

    private fun navigateToGratitude(gratitude: Gratitude) {
     val action = PostsFragmentDirections.actionPostsFragmentToDisplayGratitudeFragment2(gratitude)
    findNavController().navigate(action)
    }

}
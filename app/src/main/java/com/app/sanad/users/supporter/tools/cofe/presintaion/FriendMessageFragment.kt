package com.app.sanad.users.supporter.tools.cofe.presintaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentFriendMessageBinding
import com.app.sanad.users.patient.tools.coffeeideas.presentaion.CofeViewModel
import com.app.sanad.util.loadImage
import androidx.core.graphics.toColorInt
import androidx.fragment.app.activityViewModels

@AndroidEntryPoint
class FriendMessageFragment : BaseFragment() {

    private lateinit var binding: FragmentFriendMessageBinding
   private val viewModel: CofeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFriendMessageBinding.inflate(inflater, container, false)
        showProgressDialog()
        viewModel.retrievePartner()
        viewModel.listenToIdeaChanges()
        observeViewModel()
        setUpListeners()

        return binding.root
    }

    private fun setUpListeners() {
        binding.constraintNext.setOnClickListener {
            findNavController().navigate(R.id.action_friendMessageFragment_to_cupsMeaningFragment)
        }
        binding.exit.setOnClickListener {
            activity?.finish()
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }




    private fun setUpCupInfo(cupIdea: Int?) {
        when (cupIdea) {
           2-> {
                updateUiData(getString(R.string.over_sugary_coffee),getString(R.string.unrealistic_thinking),getString(R.string.ignoring_challenges),"#f4aa34",R.drawable.image_cup2)
            }
          3 -> {
               updateUiData(getString(R.string.balanced_coffee),getString(R.string.realistic_thinking),getString(R.string.acknowledge_the_difficulties),"#5cb348",R.drawable.image_cup3)
            }


    }


}

    private fun updateUiData(text1: String, text2: String, text3: String, color:String, image: Int) {
        binding.typeCofe.text = text1
        binding.typeCofe.setTextColor(color.toColorInt())
        binding.feeling.text = text2
        binding.example.text = text3
        binding.coffe.setImageResource(image)
    }

    private  fun observeViewModel(){

        viewModel.userIdea.observe(viewLifecycleOwner) {userIdea->
            if(userIdea != null && userIdea.idea?.isNotEmpty() == true ){
                binding.message.text = userIdea.idea
                binding.constraintNext.visibility = View.VISIBLE
                setUpCupInfo(userIdea.cupIdea)
                binding.cupInfo.visibility = View.VISIBLE
                viewModel.updateSeenByUser("seenBySupporter")
            }else{
                binding.message.text =
                    getString(R.string.no_ideas_received_yet_nyou_can_only_continue_after_receiving_a_message_from_your_friend)
            }
            dismissProgressDialog()
        }





        viewModel.partnerProfile.observe(viewLifecycleOwner){
            it?.let {
                loadImage(requireActivity(),it.imageUser,binding.imageView)
                binding.nameUser.text = it.name
            }
        }

    }
}
package com.app.sanad.users.patient.main.presentaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.app.sanad.R
import com.app.sanad.base.BaseBottomSheetDialogFragment
import com.app.sanad.databinding.FragmentOnBoardingBinding
import com.app.sanad.util.log


class OnBoardingFragment : BaseBottomSheetDialogFragment() {

    private  lateinit var binding:FragmentOnBoardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOnBoardingBinding.inflate(inflater,container,false)
        initViewPager()

        binding.button.setOnClickListener {
            dismiss()
        }
        binding.skip.setOnClickListener {
            dismiss()
        }

        binding.next.setOnClickListener {
            binding.viewPager.currentItem += 1
        }
        binding.previose.setOnClickListener {
            binding.viewPager.currentItem -= 1
        }

        return  binding.root

    }

    private fun initViewPager() {

//        val dotsIndicator = findViewById<WormDotsIndicator>(R.id.dots_indicator)

        val images = listOf(R.drawable.image_on_bording1, R.drawable.image_on_bording2,R.drawable.image_on_bording3, R.drawable.image_on_bording4)
        val adapter = OnboardingAdapter(images)
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    binding.button.visibility = View.GONE
                    binding.previose.visibility = View.GONE
                    binding.next.alpha = 1.0f
                } else if (position == 3) {
                    binding.button.visibility = View.VISIBLE
                    binding.previose.visibility = View.GONE
                    binding.next.alpha = 0.0f
                } else {
                    binding.next.alpha = 1.0f
                    binding.previose.visibility = View.VISIBLE
                    binding.button.visibility = View.GONE
                }
                log("$position")
                super.onPageSelected(position)
            }

        }
        )
//        dotsIndicator.attachTo(viewPager)
    }

}

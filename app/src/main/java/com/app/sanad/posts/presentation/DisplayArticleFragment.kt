package com.app.sanad.posts.presentation

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentArticleBinding
import com.app.sanad.databinding.FragmentDisplayArticleBinding
import com.app.sanad.getLibraryContent.data.LibraryContent
import com.app.sanad.getLibraryContent.presentaion.LibraryViewModel
import com.app.sanad.util.LANGUAGE
import com.app.sanad.util.TextToSpeechUtil
import com.app.sanad.util.loadImage
import com.app.sanad.util.log

class DisplayArticleFragment : BaseFragment(), TextToSpeech.OnInitListener {

    private lateinit var binding: FragmentDisplayArticleBinding
    private val viewModel: LibraryViewModel by activityViewModels()
    private lateinit var htmlText: String
    private lateinit var textToSpeech: TextToSpeechUtil

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentDisplayArticleBinding.inflate(inflater, container, false)
        initializeView()
        setupClickListener()
        return binding.root


    }


    private fun initializeView() {
        textToSpeech =  TextToSpeechUtil(TextToSpeech(requireActivity(), this))

        val libraryContent =
            DisplayArticleFragmentArgs.fromBundle(requireArguments()).libraryContent as LibraryContent
        setArticle(libraryContent)
        setTitles(libraryContent)
        loadImage(requireActivity(), libraryContent.imageURL, binding.imageView)
        loadImage(requireActivity(), libraryContent.imageURL, binding.imageView2)

    }

    private fun setTitles(content: LibraryContent) {
        if (viewModel.sharedPreferences.getString(LANGUAGE) == "en") {
            binding.title.text = content.enTitle
        } else {
            binding.title.text = content.arTitle
        }
    }

    private fun setArticle(content: LibraryContent) {
        val headerColor = "#204167"
        htmlText = if (viewModel.sharedPreferences.getString(LANGUAGE) == "en") {
            content.enDescription.toString()
        } else {
            log("ar")
            content.arDescription.toString()
        }
        val formattedHtml = htmlText.replace("\$headerColor", headerColor)
        val spannedText = HtmlCompat.fromHtml(formattedHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.article.text = spannedText
    }

    private fun setupClickListener() {

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.play.setOnClickListener {
            if(textToSpeech.textToSpeech.isSpeaking){
                textToSpeech.textToSpeech.stop()
                binding.play.setImageResource(R.drawable.icon_stop_sound)
            }else{
                textToSpeech.speakText(htmlText)
                binding.play.setImageResource(R.drawable.icon_play_sound)

            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.release()
    }
    override fun onInit(p0: Int) {

    }

}
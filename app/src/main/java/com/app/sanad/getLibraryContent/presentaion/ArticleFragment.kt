package com.app.sanad.getLibraryContent.presentaion

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentArticleBinding
import com.app.sanad.getLibraryContent.data.LibraryContent
import com.app.sanad.interfaces.OnItemLibraryContentClicked
import com.app.sanad.posts.OnSendButtonClicked
import com.app.sanad.posts.data.entity.Post
import com.app.sanad.posts.presentation.ChooseSupporterFragment
import com.app.sanad.posts.presentation.PostsViewModel
import com.app.sanad.util.ARTICLE
import com.app.sanad.util.SUPPORTER
import com.app.sanad.util.LANGUAGE
import com.app.sanad.util.LIBRARY
import com.app.sanad.util.Status
import com.app.sanad.util.TextToSpeechUtil
import com.app.sanad.util.loadImage
import com.app.sanad.util.log

// Fragment responsible for displaying library article details
class ArticleFragment : BaseFragment(),
    OnSendButtonClicked,
    TextToSpeech.OnInitListener,
    OnItemLibraryContentClicked {

    // Shared ViewModel for library content
    private val viewModel: LibraryViewModel by activityViewModels()

    // ViewModel responsible for sharing posts
    private val postsViewModel: PostsViewModel by viewModels()

    // ViewBinding reference
    private lateinit var binding: FragmentArticleBinding

    // Holds raw HTML text of article
    private lateinit var htmlText: String

    // Text-to-speech helper
    private lateinit var textToSpeech: TextToSpeechUtil

    // Inflate layout and initialize UI
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        setupClickListener()
        initializeView()
        observeLiveData()
        return binding.root
    }


    // Initialize UI data and content
    private fun initializeView() {
        textToSpeech = TextToSpeechUtil(TextToSpeech(requireActivity(), this))
        textToSpeech.debug()
        if (viewModel.sharedPreferences.getUserProfile().typeOfUser == SUPPORTER){
            binding.share.visibility = View.GONE
        }



        val content = viewModel.currentContent

        // Apply background color from content
        binding.container.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(content.backgroundColor))

        setArticle(content)
        setTitles(content)

        // Load article images
        loadImage(requireActivity(), content.imageURL, binding.imageView)
        loadImage(requireActivity(), content.imageURL, binding.imageView2)
    }

    // Observe sharing status
    private fun observeLiveData() {

        textToSpeech.status.observe(viewLifecycleOwner) { status ->
            log("status $status")
            when (status) {
                Status.Done , Status.Init, Status.Error  -> {
                    binding.play.setImageResource(R.drawable.icon_stop_sound)
                }
                Status.Start  ->
                binding.play.setImageResource(R.drawable.icon_play_sound)
            }
            textToSpeech.resetStatus()


        }

        postsViewModel.statusSharing.observe(viewLifecycleOwner) {
            if (it) showToast(getString(R.string.shared_successfully))
            dismissProgressDialog()
        }
    }

    // Set article title based on language
    private fun setTitles(content: LibraryContent) {
        binding.title.text =
            if (viewModel.sharedPreferences.getString(LANGUAGE) == "en")
                content.enTitle
            else
                content.arTitle
    }

    // Parse and display article HTML
    private fun setArticle(content: LibraryContent) {
        val headerColor = "#204167"

        htmlText =
            if (viewModel.sharedPreferences.getString(LANGUAGE) == "en")
                content.enDescription.toString()
            else
                content.arDescription.toString()

        val formattedHtml = htmlText.replace("\$headerColor", headerColor)
        binding.article.text =
            HtmlCompat.fromHtml(formattedHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    // Handle UI click actions
    private fun setupClickListener() {

        // Navigate back
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Play / stop text-to-speech
        binding.play.setOnClickListener {
            if (textToSpeech.textToSpeech.isSpeaking) {
                log("stop speaking")
                textToSpeech.textToSpeech.stop()
                binding.play.setImageResource(R.drawable.icon_stop_sound)
            } else {
                log("start speaking ")
                textToSpeech.speakText(htmlText)
                binding.play.setImageResource(R.drawable.icon_play_sound)
            }
        }

        // Share article
        binding.share.setOnClickListener {
            navigateToChooseSupporter()
        }

        // Suggest other articles
        binding.suggest.setOnClickListener {
            displaySuggestedContent(this, getString(R.string.suggest_other_articles), ARTICLE)
        }
    }

    // Open supporter selection dialog
    private fun navigateToChooseSupporter() {
        when {
            !postsViewModel.user.hasPartner!! ->
                showToast(getString(R.string.no_supporters_text))
            !isConnected() ->
                showNoInternetSnackBar(binding.root)
            else -> {
                ChooseSupporterFragment().apply {
                    initOnConfirmButtonClicked(this@ArticleFragment)
                }.show(childFragmentManager, ChooseSupporterFragment::class.java.name)
            }
        }
    }

    // Show suggested content bottom sheet
    fun displaySuggestedContent(
        onItemLibraryContentClicked: OnItemLibraryContentClicked,
        title: String,
        type: String
    ) {
        SuggestedContentFragment()
            .setType(type)
            .setOnItemLibraryContent(onItemLibraryContentClicked)
            .setTitle(title)
            .show(childFragmentManager, SuggestedContentFragment::class.java.name)
    }

    // Called when user confirms sharing
    override fun onSendClicked(supporters: MutableList<String> , listId:MutableList<String>) {
        showProgressDialog()
        postsViewModel.sharePost(createPost(supporters , listId))
    }

    // Build Post object for sharing
    private fun createPost(supporters: MutableList<String> , listId:MutableList<String>) =
        Post(
            type = LIBRARY,
            libraryContent = viewModel.currentContent,
            supporters = supporters
            ,listSupportersId = listId
        )

    // TextToSpeech init callback
    override fun onInit(status: Int) {
        binding.play.visibility = View.VISIBLE
    }

    // Release TTS resources
    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.release()
    }

    // Handle suggested content click
    override fun onItemClicked(type: String, index: Int, content: String, currentContent: LibraryContent) {
        viewModel.currentContent = currentContent
        initializeView()
    }
}

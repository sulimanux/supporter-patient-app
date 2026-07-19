package com.app.sanad.users.patient.dailyprogram.presentaion

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.base.BaseFragment
import com.app.sanad.R
import com.app.sanad.databinding.LayoutTaskBinding
import com.app.sanad.databinding.StaionDescriptionDialogBinding
import com.app.sanad.users.patient.dailyprogram.data.entity.Task
import com.app.sanad.util.ENGLISH_KEY
import com.app.sanad.util.LANGUAGE
import com.app.sanad.util.TextToSpeechUtil

@AndroidEntryPoint
open class BaseDailyProgramFragment : BaseFragment() {

    val viewModel: DailyProgramViewModel by viewModels() // Access to daily program tasks and status
    lateinit var textToSpeech: TextToSpeechUtil // Handles TTS for task descriptions
    lateinit var htmlText: String // Stores the HTML content of current task description

    var title = "" // Stores current task title
    lateinit var binding: LayoutTaskBinding
    lateinit var task: Task // Current task object
    var player: ExoPlayer? = null // Media player for audio/video tasks

    /**
     * Loads a task from the ViewModel list and updates the UI based on language and type.
     * @param index index of the task to display
     * @param numberTask position in UI (for highlighting or numbering)
     */
    

    fun getTaskFromList(index: Int) {
        showProgressDialog()
        viewModel.listOfTasks.let { listOfTasks ->
            task = listOfTasks[index]
            val currentLang = viewModel.sharedPreferences.getString(LANGUAGE)
            if (currentLang != ENGLISH_KEY) {
                setText( task.arTitle, task.arDescription)
            } else {
                setText( task.enTitle, task.enDescription)
            }
            checkType(task.type)
        }
    }
     fun checkInternetConnection() {
        if (isConnected()) {
            init()
            binding.noInternetLayout.swipeRefresh.visibility = View.GONE
            binding.uiComponent.visibility = View.VISIBLE
        }
        else {
            binding.noInternetLayout.swipeRefresh.visibility = View.VISIBLE
            binding.uiComponent.visibility = View.GONE
        }

    }
    open fun init(){

    }
    /** Sets title and description of the task on the UI */
    private fun setText( title: String?, description: String?) {
        this.title = title ?: ""
        binding.textTitle.visibility = if (title.isNullOrEmpty()) View.GONE else View.VISIBLE
        binding.textTitle.text = title
        setDescriptionText(description)
    }

    /** Formats and displays HTML task description */
    private fun setDescriptionText(description: String?) {
        val headerColor = "#204167"
        htmlText = description ?: ""
        val formattedHtml = htmlText.replace("\$headerColor", headerColor)
        binding.textDescription.text = HtmlCompat.fromHtml(formattedHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    /** Updates task icon and background based on status */
    fun changeColorOfTaskImage(status: Int?, root: ConstraintLayout, image: ImageView) {
        when (status) {
            1 -> {
                image.setImageResource(R.drawable.ic_check_blue)
                root.setBackgroundResource(R.drawable.circle_blue_dark)
            }
            2 -> {
                val params = root.layoutParams
                params.width = 115
                params.height = 115
                root.layoutParams = params
                root.setBackgroundResource(R.drawable.circle_orange_with_border)
            }
        }
    }

    /** Hides spiritual task if user has no religion */
    fun hideSpiritualIcon(constraintTask: ConstraintLayout, line: View) {
        if (viewModel.userProfile().religion == false) {
            constraintTask.visibility = View.GONE
            line.visibility = View.GONE
        }
    }



    /** Retrieves the next task cyclically and updates the UI */
    fun getNextTask(index: Int, numberTask: Int): Int {
        player?.pause()
        return if (viewModel.listOfTasks.isNotEmpty()) {
            val currentTaskIndex = (index + 1) % viewModel.listOfTasks.size
            getTaskFromList(currentTaskIndex)
            currentTaskIndex
        } else 0
    }

    /** Determines how to display the task based on its type */
    private fun checkType(type: Int?) {
        when (type) {
            1 -> displayImage(type) // Image task
            2, 3 -> { // Video or audio
                hideViews(type)
                playVideoAudio(Uri.parse(task.link))
            }
            4 -> showText(type) // Text task
        }
    }

    /** Plays audio or video tasks using ExoPlayer */
    private fun playVideoAudio(uri: Uri) {
        player = ExoPlayer.Builder(requireContext()).build().also { exoPlayer ->
            binding.exoPlayer.player = exoPlayer
            val mediaItem = MediaItem.fromUri(uri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            dismissProgressDialog()
        }
    }

    /** Hides irrelevant views based on task type */
    private fun showText(int: Int) = hideViews(int)
    private fun displayImage(int: Int) {
        Glide.with(this).load(task.image).into(binding.imageView)
        hideViews(int)
        dismissProgressDialog()
    }

    /** Hides/shows views dynamically for image/video/audio tasks */
    private fun hideViews(int: Int) {
        when (int) {
            1 -> { binding.exoPlayer.visibility = View.GONE; binding.imageView.visibility = View.VISIBLE } // Image
            2 -> { binding.imageView.visibility = View.GONE; binding.exoPlayer.visibility = View.VISIBLE } // Video
            3 -> { binding.imageView.visibility = View.GONE; binding.exoPlayer.visibility = View.VISIBLE } // Audio
        }
    }

    /** Displays a modal dialog with task description and image */
    fun showDescriptionDialog(image: Int, title: String, text: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = StaionDescriptionDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val layoutParams = attributes
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.8).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = layoutParams
        }

        dialogBinding.image.setImageResource(image)
        dialogBinding.title.text = title
        dialogBinding.text.text = text
        dialogBinding.icClose.setOnClickListener { dialog.dismiss() }
        dialogBinding.button.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    /** Lifecycle handling: pause player and release TTS, sync data if needed */
    override fun onStop() {
        super.onStop()
        player?.pause()
        if (::textToSpeech.isInitialized) textToSpeech.release()
        if (viewModel.isSyncNeeded) viewModel.updateCurrentTaskRemotely()
    }

    /** Release ExoPlayer resources to prevent memory leaks */
    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}

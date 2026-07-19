package com.app.sanad.getLibraryContent.presentaion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.getLibraryContent.data.LibraryContent
import com.app.sanad.interfaces.OnItemLibraryContentClicked
import com.app.sanad.util.*
import com.app.sanad.util.loadImage

// Adapter for displaying customized library content list
class CustomizedContentLibraryAdapter(
    private val libraryContents: List<LibraryContent>?,
    private val context: Context,
    private val sharedPreferences: SharedPreferencesManager,
    private val onItemLibraryContentClicked: OnItemLibraryContentClicked
) : RecyclerView.Adapter<CustomizedContentLibraryAdapter.ViewHolder>() {

    // Inflate item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view_customized_content, parent, false)
        return ViewHolder(view)
    }

    // Bind content data to item views
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val libraryContent = libraryContents?.get(position)

        // Load image and basic info
        loadImage(context, libraryContent?.imageURL, holder.imageView)
        holder.date.text = libraryContent?.date
        holder.type.text = getTextType(libraryContent?.type)
        holder.title.text = getTextTitle(libraryContent)

        // Show / hide duration based on content type
        setTextDuration(libraryContent, holder.duration)

        // Handle item click
        holder.itemView.setOnClickListener {
            onItemLibraryContentClicked.onItemClicked(
                libraryContent?.type!!,
                position,
                Customized_CONTENT,
                libraryContent
            )
        }
    }

    // Hide duration for articles, show for audio/video
    private fun setTextDuration(libraryContent: LibraryContent?, duration: TextView) {
        if (libraryContent?.type == ARTICLE) {
            duration.visibility = View.GONE
        } else {
            duration.text = libraryContent?.duration
        }
    }

    // Return localized content type text
    private fun getTextType(contentType: String?): String =
        when (contentType) {
            ARTICLE -> context.getString(R.string.article)
            VIDEO -> context.getString(R.string.video)
            else -> context.getString(R.string.audio)
        }

    // Return title based on selected language
    private fun getTextTitle(libraryContent: LibraryContent?): String =
        if (sharedPreferences.getString(LANGUAGE) == "en")
            libraryContent?.enTitle!!
        else
            libraryContent?.arTitle!!

    // Number of items
    override fun getItemCount(): Int {
        return libraryContents?.size!!
    }

    // ViewHolder for customized content item
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val date: TextView = itemView.findViewById(R.id.date)
        val type: TextView = itemView.findViewById(R.id.type)
        val duration: TextView = itemView.findViewById(R.id.duration)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}

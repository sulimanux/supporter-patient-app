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
import com.app.sanad.util.COMMON_CONTENT
import com.app.sanad.util.LANGUAGE
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.loadImage

// Adapter for displaying common library content in a grid
class CommonContentLibraryAdapter(
    private val libraryContents: List<LibraryContent>?,
    private val context: Context,
    private val sharedPreferences: SharedPreferencesManager,
    private val onItemLibraryContentClicked: OnItemLibraryContentClicked
) : RecyclerView.Adapter<CommonContentLibraryAdapter.ViewHolder>() {

    // Inflate item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view_most_common_content, parent, false)
        return ViewHolder(view)
    }

    // Bind data to item view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get current content item
        val libraryContent = libraryContents?.get(position)

        // Load image and set title
        loadImage(context, libraryContent?.imageURL, holder.imageView)
        setText(libraryContent, holder.title)

        // Handle item click
        holder.itemView.setOnClickListener {
            onItemLibraryContentClicked.onItemClicked(
                libraryContent?.type!!,
                position,
                COMMON_CONTENT,
                libraryContent
            )
        }
    }

    // Set title based on selected language
    private fun setText(libraryContent: LibraryContent?, title: TextView) {
        title.text =
            if (sharedPreferences.getString(LANGUAGE) == "en")
                libraryContent?.enTitle
            else
                libraryContent?.arTitle
    }

    // Return list size
    override fun getItemCount(): Int {
        return libraryContents?.size!!
    }

    // ViewHolder for content item
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}

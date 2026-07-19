package com.app.sanad.posts.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.interfaces.ItemPostsClicked
import com.app.sanad.posts.data.entity.Post
import com.app.sanad.util.SUPPORTER
import com.app.sanad.util.GRATITUDE
import com.app.sanad.util.LANGUAGE
import com.app.sanad.util.LIBRARY
import com.app.sanad.util.SUPPLICATIONS
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.getGratitudeQuestionsList

class PostsAdapter(
    private val posts: List<Post>,
    private val context: Context,
    private val sharedPreferences: SharedPreferencesManager,
    private val itemPostsClicked: ItemPostsClicked
) :
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view_post, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     val user = sharedPreferences.getUserProfile()
       var shouldUpdateSeenStatus = false

     val post = posts[position]

     if (user.typeOfUser == SUPPORTER && post.seenList?.contains(user.email) == false){
//         holder.badge.visibility = View.VISIBLE
         holder.itemView.setBackgroundColor("#eff5f9".toColorInt())
         shouldUpdateSeenStatus = true
     }else{
         holder.itemView.setBackgroundColor("#ffffff".toColorInt())
     }
        when(post.type){
            GRATITUDE ->  setUpGratitude(holder,post)
            SUPPLICATIONS -> setUpSupplications(holder,post)
            LIBRARY -> setUpLibrary(holder,post)
        }
        holder.display.setOnClickListener {
            itemPostsClicked.onItemClicked(post)
            if (shouldUpdateSeenStatus){
                itemPostsClicked.updateSeenBySupporter(post)
            }
        }
    }

    private fun setUpGratitude(holder: ViewHolder, content: Post) {
        val list = getGratitudeQuestionsList(context)
        holder.text .text = list[content.gratitude!!.index]
        holder.type.text = context.getString(R.string.message_of_gratitude)
        holder.display.text =
            "${context.getString(R.string.display)} ${context.getString(R.string.message_of_gratitude)}"

        holder.imageView.setImageResource(R.drawable.message_gratitude)



    }

    private fun setUpSupplications(holder: ViewHolder, content: Post) {
        holder.text .text = content.supplication?.name
        holder.type.text = context.getString(R.string.supplication_card)
        holder.display.text =
            "${context.getString(R.string.display)} ${context.getString(R.string.supplication_card)}"

        holder.imageView.setImageResource(R.drawable.img_tasbih)

    }

    private fun setUpLibrary(holder: ViewHolder, content: Post) {
        holder.text .text =  if (sharedPreferences.getString(LANGUAGE) == "en") {
            content.libraryContent?.enTitle
        } else {
            content.libraryContent?.arTitle
        }


    }




    override fun getItemCount(): Int {
        return posts.size
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var type: TextView = itemView.findViewById<TextView>(R.id.type)
        var text: TextView = itemView.findViewById<TextView>(R.id.text)
        var display: TextView = itemView.findViewById<TextView>(R.id.display)
        var imageView: ImageView = itemView.findViewById<ImageView>(R.id.imageView)
        val badge = itemView.findViewById<ImageView>(R.id.badge_posts)

    }
}
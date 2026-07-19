package com.app.sanad.users.supporter.tools.cofe.presintaion

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R

class SuggestedSympathyAdapter (
    private val onItemSelectedListener: OnItemSelectedListener,
    private val selectedPosition: Int,
    private val phrases: List<String>): RecyclerView.Adapter<SuggestedSympathyAdapter.ViewHolder> (){


    inner  class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val root: ConstraintLayout = itemView.findViewById(R.id.root)
        val text: TextView = itemView.findViewById(R.id.text)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_suggested_phrases_sympathy, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount () = phrases.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val icon = if(selectedPosition == position) R.drawable.image_heart01  else R.drawable.icon_heart97
        holder.root.backgroundTintList =
            if(selectedPosition == position) ColorStateList.valueOf(Color.parseColor("#8cc9ed"))
            else ColorStateList.valueOf(Color.parseColor("#f2f2f2"))
        val text = phrases[position]
        holder.text.text = text
        holder.imageView.setImageResource(icon)
       holder.itemView.setOnClickListener {
        onItemSelectedListener.onItemSelected(position, text)
      }

    }


}

interface  OnItemSelectedListener{
    fun onItemSelected(position: Int, item: String)
}
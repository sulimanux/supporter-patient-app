package com.app.sanad.users.supporter.partner.prestation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.model.DataPatient


class RecyclerAdapter(
private val itemList: List<DataPatient>,
private val language:String, private val itemClickListener: ItemClickListener
) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.imageView.setImageResource(item.image)
        holder.title.text =  if (language == "en") item.enTitle else item.arTitle


        holder.description.text = if (language == "en") item.enDescription else item.arDescription


        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(item.flag)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<TextView>(R.id.title)
        var description: TextView = itemView.findViewById<TextView>(R.id.description)
        var imageView: ImageView = itemView.findViewById<ImageView>(R.id.imageView)
    }
}

interface ItemClickListener {
    fun onItemClick(flag: String)
}


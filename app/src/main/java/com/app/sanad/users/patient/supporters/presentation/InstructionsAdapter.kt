package com.app.sanad.users.patient.supporters.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.model.Step
import com.app.sanad.users.patient.supporters.data.entity.Instructions
import com.app.sanad.util.log


class InstructionsAdapter(
    private val steps: List<Instructions>,
    private val lang: String
) : RecyclerView.Adapter<InstructionsAdapter.StepViewHolder>() {

    inner class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescEn: TextView = itemView.findViewById(R.id.tvDesc)
        val img : ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.instructio_item, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        log(lang)
        val step = steps[position]
        holder.img.setImageResource(step.image)
        if (lang == "ar") {
            holder.tvTitle.text = step.titleAr
            holder.tvDescEn.text = step.descAr
        }else{
            holder.tvTitle.text = step.titleEn
            holder.tvDescEn.text = step.descEn
        }
    }

    override fun getItemCount(): Int = steps.size
}
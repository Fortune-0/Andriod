package com.example.efficilog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.efficilog.model.ProductionActivity


class ProductionActivityAdapter(private val activityList: List<ProductionActivity>) :
    RecyclerView.Adapter<ProductionActivityAdapter.ProductionActivityViewHolder>() {

    inner class ProductionActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val activityName: TextView = itemView.findViewById(R.id.user_name)
        val activityTask: TextView = itemView.findViewById(R.id.activity_description)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductionActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.production_activity_card, parent, false)
        return ProductionActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductionActivityViewHolder, position: Int) {
        val activity = activityList[position]
        holder.activityName.text = activity.name
        holder.activityTask.text = activity.task
        holder.timestampTextView.text = activity.timestamp
    }

    override fun getItemCount(): Int {
        return activityList.size
    }
}
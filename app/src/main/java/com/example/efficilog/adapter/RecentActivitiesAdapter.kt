package com.example.efficilog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.efficilog.R
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.efficilog.model.RecentActivity
class RecentActivitiesAdapter(
    private val items: List<RecentActivity>
) : RecyclerView.Adapter<RecentActivitiesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.item_username)
        val activityType: TextView = view.findViewById(R.id.item_task)
        val timestamp: TextView = view.findViewById(R.id.item_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_activity, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.username.text = item.username
        holder.activityType.text = item.task
        holder.timestamp.text = item.timestamp
    }
}

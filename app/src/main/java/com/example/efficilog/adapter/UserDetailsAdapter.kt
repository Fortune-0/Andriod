package com.example.efficilog.adapter

import com.example.efficilog.R
import com.example.efficilog.model.UserDetail
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class UserDetailsAdapter(
    private val users: List<UserDetail>
) : RecyclerView.Adapter<UserDetailsAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.expanded_username)
        val lastActivity: TextView = view.findViewById(R.id.expanded_last_activity)
        val totalUnits: TextView = view.findViewById(R.id.item_total_units)
        val breakdown: TextView = view.findViewById(R.id.activities_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_details, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        holder.username.text = user.username
        holder.lastActivity.text = "Last: ${user.lastActivity}"
        holder.totalUnits.text = "Units: ${user.totalUnits}"

        // Convert activities map to readable string
        holder.breakdown.text = user.activities.entries.joinToString { "${it.key}: ${it.value}" }
    }
}

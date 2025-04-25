package com.example.efficilog

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import android.view.ViewGroup
import com.example.efficilog.model.Staff
import androidx.recyclerview.widget.RecyclerView
class StaffAdapter(
    private val staffList: List<Staff>,
    private val onStaffClicked: (Staff) -> Unit
) : RecyclerView.Adapter<StaffAdapter.StaffViewHolder>() {

    // ViewHolder  Represents a single item in the RecyclerView
    inner class StaffViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val staffName: TextView = itemView.findViewById(R.id.staff_name)
        val staffRole: TextView = itemView.findViewById(R.id.staff_role)
        val staffStatus: TextView = itemView.findViewById(R.id.staff_status)
        val viewMoreButton: Button = itemView.findViewById(R.id.view_more_button)
    }
    // Inflates the layout for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.staff_item, parent, false)
        return StaffViewHolder(view)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        val staff = staffList[position]

        // Bind the Staff data
        holder.staffName.text = staff.name
        holder.staffRole.text = "Role: ${staff.role}"
        holder.staffStatus.text = "Status: ${staff.status}"
        holder.viewMoreButton.setOnClickListener {
            onStaffClicked(staff)
        }
    }
    override fun getItemCount(): Int {
        return staffList.size
    }
}
package com.example.efficilog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.efficilog.R
import com.example.efficilog.model.Job

class JobAdapter (private val jobList: List<Job>) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>()  {
        // Define ViewHolder for job item
        inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleTextView: TextView = itemView.findViewById(R.id.titleText)
            val timestamp: TextView = itemView.findViewById(R.id.timestampTextView)
            val configTextView: TextView = itemView.findViewById(R.id.typeText)
            val threadTextView: TextView = itemView.findViewById(R.id.threadTypeText)
            val sizeTextView: TextView = itemView.findViewById(R.id.sizeText)
            val numberTextView: TextView = itemView.findViewById(R.id.numberText)
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
            val view = LayoutInflater.from(parent.context)
               .inflate(R.layout.history_card, parent, false)
            return JobViewHolder(view)
        }
    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val currentItem = jobList[position]
        holder.titleTextView.text = "Job: ${currentItem.featureName}"
        holder.timestamp.text = "Date: ${currentItem.timestamp}"

        if (currentItem.entries.isNotEmpty()) {
            val entry = currentItem.entries.first()  // Assume only one entry per job

            holder.configTextView.text = "Config: ${entry["type"] as? String ?: "unknown"}"
            holder.threadTextView.text = "Thread: ${entry["threadType"] as? String ?: "unknown"}"
            holder.sizeTextView.text = "Size: ${entry["size"] as? String ?: "unknown"}"
            holder.numberTextView.text = "Number: ${(entry["number"] as? Long) ?: 0L}"
        } else {
            // Handle empty entries
            holder.configTextView.text = "Config: -"
            holder.threadTextView.text = "Thread: -"
            holder.sizeTextView.text = "Size: -"
            holder.numberTextView.text = "Number: -"
        }
    }
    override fun getItemCount(): Int = jobList.size
}
//    }
//    override fun getItemCount(): Int = jobList.size
//}
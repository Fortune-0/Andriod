package com.example.efficilog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.efficilog.R
import com.example.efficilog.model.Job
import com.example.efficilog.model.EntryItem

class JobAdapter(private var jobList: MutableList<Job>) :
    RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    private var entryList: List<EntryItem> = emptyList()

    init {
        updateEntries(jobList)
    }

    fun updateJobs(newJobs: List<Job>) {
        jobList.clear()
        jobList.addAll(newJobs)
        updateEntries(jobList)
        notifyDataSetChanged()  // Notify RecyclerView to refresh
    }

    private fun updateEntries(jobList: List<Job>) {
        entryList = jobList.flatMap { job ->
            job.entries.map { entry ->
                EntryItem(
                    featureName = job.featureName,
                    timestamp = job.timestamp,
                    type = entry["type"] as? String ?: "unknown",
                    threadType = entry["threadType"] as? String ?: "unknown",
                    size = entry["size"] as? String ?: "unknown",
                    number = entry["number"] as? Long ?: 0L
                )
            }
        }
    }

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
        val entry = entryList[position]

        holder.titleTextView.text = "Job: ${entry.featureName}"
        holder.timestamp.text = "Date: ${entry.timestamp}"
        holder.configTextView.text = "Config: ${entry.type}"
        holder.threadTextView.text = "Thread: ${entry.threadType}"
        holder.sizeTextView.text = "Size: ${entry.size}"
        holder.numberTextView.text = "Number: ${entry.number}"
    }
    override fun getItemCount(): Int = entryList.size
}
//    }
//    override fun getItemCount(): Int = jobList.size
//}
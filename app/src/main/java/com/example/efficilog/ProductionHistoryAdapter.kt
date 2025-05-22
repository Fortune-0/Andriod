package com.example.efficilog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil

class ProductionHistoryAdapter(
    private val historyList: List<ProductionHistoryCard>,
) : RecyclerView.Adapter<ProductionHistoryAdapter.ProductionHistoryViewHolder>() {

    inner class ProductionHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskType: TextView = itemView.findViewById(R.id.task_type)
        val threadType: TextView = itemView.findViewById(R.id.thread_type)
        val taskSize: TextView = itemView.findViewById(R.id.task_size)
        val taskNumber: TextView = itemView.findViewById(R.id.task_number)
        val taskTimestamp: TextView = itemView.findViewById(R.id.task_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductionHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.production_history_card, parent, false)
        return ProductionHistoryViewHolder(view)
    }
    override fun onBindViewHolder(holder: ProductionHistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.taskType.text = "Task: ${item.type}"
        holder.threadType.text = "Thread Type: ${item.threadType}"
        holder.taskSize.text = "Size: ${item.size}"
        holder.taskNumber.text = "Number: ${item.number}"
        holder.taskTimestamp.text = "Timestamp: ${item.timestamp}"
    }

    override fun getItemCount(): Int = historyList.size
}
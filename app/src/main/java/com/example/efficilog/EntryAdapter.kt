package com.example.efficilog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EntryAdapter(private val entries: List<Entry>) :
    RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

    // Define ViewHolder for entry item
    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
        val threadTypeTextView: TextView = itemView.findViewById(R.id.threadTypeTextView)
        val sizeTextView: TextView = itemView.findViewById(R.id.sizeTextView)
        val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entry, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entries[position]
        holder.typeTextView.text = "Type: ${entry.type}"
        holder.threadTypeTextView.text = "Thread Type: ${entry.threadType}"
        holder.sizeTextView.text = "Size: ${entry.size}"
        holder.numberTextView.text = "Number: ${entry.number}"
    }

    override fun getItemCount(): Int {
        return entries.size
    }
}

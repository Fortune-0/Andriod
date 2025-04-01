package com.example.efficilog.model

data class EntryItem (
    val featureName: String,
    val timestamp: String,
    val type: String,
    val threadType: String,
    val size: String,
    val number: Long
)
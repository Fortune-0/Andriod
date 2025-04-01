package com.example.efficilog.model

data class Job(
    val featureName: String,
    val timestamp: String,
    val entries: List<Map<String, Any>>
//    val type: String,
//    val threadType: String,
//    val size: String,
//    val number: Long
)
package com.example.efficilog.model

data class Job(
    val featureName: String,
    val timestamp: String,
    val entries: List<Map<String, Any>>
)
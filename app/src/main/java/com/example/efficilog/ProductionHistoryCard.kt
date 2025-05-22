package com.example.efficilog

data class ProductionHistoryCard (
    val timestamp: String,
    val type: String,
    val threadType: String,
    val size: String,
    val number: Int
)
package com.example.efficilog.model

data class UserDetail(
    val username: String,
//    val lastActivity: RecentActivity,
    val lastActivity: String,
    val totalUnits: Int,
    val activities: Map<String, Int>
)
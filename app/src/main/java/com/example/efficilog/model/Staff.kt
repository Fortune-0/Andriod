package com.example.efficilog.model

class Staff (
    val id: String,
    val name: String,
    val role: String,
    val status: String,
    val lastActive: String = "N/A",
    val productionRate: String,
    var isExpanded: Boolean = false,
    var contactInfo: String = "view details"

)
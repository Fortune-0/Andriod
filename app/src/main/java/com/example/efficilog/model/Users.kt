package com.example.efficilog.model

data class Users (
        val name: String = "",
        val email: String = "",
        val passcode: String = "",
        val position: String = "Operator",
        val role: String = "Staff", // or Admin
        val phone: String? = "",
        val address: String? = "",
        val profileImageUrl: String? = null
)
//        val phone: String? = null,
//        val id: String = ""

package com.example.efficilog.model

data class Users (
        val name: String = "",
        val email: String = "",
        val passcode: String = "",
        val position: String = "Operator",
        val phone: String? = "",
        val address: String? = ""
)
//        val phone: String? = null,
//        val id: String = ""

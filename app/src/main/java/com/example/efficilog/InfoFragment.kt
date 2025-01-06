//package com.example.efficilog
//
//import android.os.Bundle
//import android.widget.TextView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//
//class InfoFragment : Fragment() {
//
//    private lateinit var profileEmail: TextView
//    private lateinit var profilePhone: TextView
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.fragment_info, container, false)
//        // Set up the info section layout here
//
//        profileEmail = view.findViewById(R.id.profileEmail)
//        profilePhone = view.findViewById(R.id.profilePhone)
//
//        profilePhone.text = "0800 123 456"
//        profileEmail.text = "Test@test.com"
//
//        return view
//    }
//}
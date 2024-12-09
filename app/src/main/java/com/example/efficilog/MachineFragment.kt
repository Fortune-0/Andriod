package com.example.efficilog
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView

class MachineFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_machine, container, false)

//        val machineTxt = view.findViewById<TextView>(R.id.tvMachineType)
//        val machineNumTxt = view.findViewById<TextView>(R.id.tvMachineNumber)

        return view
    }
}
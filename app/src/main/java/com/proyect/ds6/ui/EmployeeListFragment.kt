package com.proyect.ds6.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyect.ds6.model.staticEmployees
import com.proyect.ds6.R

class EmployeeListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_employee_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerEmployees)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = EmployeeAdapter(staticEmployees)
        return view
    }
}

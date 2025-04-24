package com.proyect.ds6.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyect.ds6.R
import com.proyect.ds6.model.Employee

class EmployeeAdapter(
    private val employees: List<Employee>
) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    inner class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvApellido: TextView = itemView.findViewById(R.id.tvApellido)
        val tvNacionalidad: TextView = itemView.findViewById(R.id.tvNacionalidad)
        val tvDepartamento: TextView = itemView.findViewById(R.id.tvDepartamento)
        val tvCargo: TextView = itemView.findViewById(R.id.tvCargo)
        val btnVerMas: Button = itemView.findViewById(R.id.btnVerMas)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_employee, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = employees[position]
        holder.tvNombre.text = employee.nombre1
        holder.tvApellido.text = employee.apellido1
        holder.tvNacionalidad.text = employee.nacionalidad
        holder.tvDepartamento.text = employee.departamento
        holder.tvCargo.text = employee.cargo
        // Acciones de los botones (por ahora solo placeholders)
        holder.btnVerMas.setOnClickListener { /* TODO: Acción ver más */ }
        holder.btnEditar.setOnClickListener { /* TODO: Acción editar */ }
        holder.btnEliminar.setOnClickListener { /* TODO: Acción eliminar */ }
    }

    override fun getItemCount(): Int = employees.size
}

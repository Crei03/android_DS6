package com.proyect.ds6.ui.employee.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*

/**
 * Componente que muestra los campos para la información de trabajo del empleado
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkInfoComponent(
    departamento: String,
    onDepartamentoChange: (String) -> Unit,
    cargo: String,
    onCargoChange: (String) -> Unit,
    fechaContratacion: Long,
    onFechaContratacionChange: (Long) -> Unit,
    estado: Int,
    onEstadoChange: (Int) -> Unit
) {
    // Lista estática de departamentos
    val departamentos = listOf(
        "Recursos Humanos", "Contabilidad", "Ventas", "Marketing", 
        "Tecnología de la Información", "Operaciones", "Servicio al Cliente",
        "Producción", "Logística", "Investigación y Desarrollo"
    )

    // Mapeo estático de cargos por departamento
    val cargosPorDepartamento = mapOf(
        "Recursos Humanos" to listOf("Gerente de RRHH", "Especialista en Reclutamiento", "Analista de Compensaciones", "Asistente de RRHH"),
        "Contabilidad" to listOf("Contador General", "Analista Contable", "Auditor Interno", "Asistente Contable"),
        "Ventas" to listOf("Gerente de Ventas", "Ejecutivo de Ventas", "Representante de Ventas", "Asistente de Ventas"),
        "Tecnología de la Información" to listOf("Director de TI", "Desarrollador", "Administrador de Sistemas", "Soporte Técnico", "Analista de Datos")
    )

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fechaContratacion
    )

    var expandedDepartamento by remember { mutableStateOf(false) }
    var expandedCargo by remember { mutableStateOf(false) }
    var expandedEstado by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Información de Trabajo",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Departamento
            ExposedDropdownMenuBox(
                expanded = expandedDepartamento,
                onExpandedChange = { expandedDepartamento = it },
            ) {
                OutlinedTextField(
                    value = departamento,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Departamento") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDepartamento) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedDepartamento,
                    onDismissRequest = { expandedDepartamento = false },
                ) {
                    departamentos.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { 
                                onDepartamentoChange(option)
                                expandedDepartamento = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cargo
            ExposedDropdownMenuBox(
                expanded = expandedCargo,
                onExpandedChange = { expandedCargo = it },
            ) {
                OutlinedTextField(
                    value = cargo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Cargo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCargo) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedCargo,
                    onDismissRequest = { expandedCargo = false },
                ) {
                    // Mostrar cargos del departamento seleccionado o una lista vacía si no hay
                    val cargos = cargosPorDepartamento[departamento] ?: emptyList()
                    cargos.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { 
                                onCargoChange(option)
                                expandedCargo = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha de Contratación
            OutlinedTextField(
                value = if (fechaContratacion > 0) {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = fechaContratacion
                    "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
                } else "",
                onValueChange = {},
                label = { Text("Fecha de Contratación") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Text("📅")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Estado
            ExposedDropdownMenuBox(
                expanded = expandedEstado,
                onExpandedChange = { expandedEstado = it },
            ) {
                OutlinedTextField(
                    value = when(estado) {
                        1 -> "Activo"
                        0 -> "Inactivo"
                        else -> ""
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedEstado,
                    onDismissRequest = { expandedEstado = false },
                ) {
                    mapOf(1 to "Activo", 0 to "Inactivo").forEach { (value, text) ->
                        DropdownMenuItem(
                            text = { Text(text) },
                            onClick = { 
                                onEstadoChange(value)
                                expandedEstado = false
                            }
                        )
                    }
                }
            }
        }
    }
    
    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { 
                            onFechaContratacionChange(it)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
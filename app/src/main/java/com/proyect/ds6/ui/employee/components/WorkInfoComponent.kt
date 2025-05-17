package com.proyect.ds6.ui.employee.components // Adjust package as needed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable // Import clickable modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.* // Import necessary Compose state APIs
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.*
// Import necessary Material 3 DatePicker components
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.TextButton

import androidx.compose.ui.Alignment // Import Alignment for the DatePicker dialog buttons

// Import the data classes for Cargo and Departamento
import com.proyect.ds6.model.Cargo
import com.proyect.ds6.model.Departamento
// Ya no necesitamos importar el DropdownSelector
// import com.proyect.ds6.ui.components.DropdownSelector


/**
 * Componente que muestra los campos para la información de trabajo del empleado,
 * incluyendo la selección de Departamento y Cargo desde listas proporcionadas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkInfoComponent(
    // --- Parámetros actualizados para las listas y selecciones ---
    departamentos: List<Departamento>, // Lista de departamentos obtenida del ViewModel
    selectedDepartamento: Departamento?, // Departamento actualmente seleccionado (objeto)
    onDepartamentoSelected: (Departamento?) -> Unit, // Callback cuando se selecciona un departamento

    cargos: List<Cargo>, // Lista de cargos (obtenida del ViewModel)
    selectedCargo: Cargo?, // Cargo actualmente seleccionado (objeto)
    onCargoSelected: (Cargo?) -> Unit, // Callback cuando se selecciona un cargo
    // --- Fin de parámetros actualizados ---

    fechaContratacion: Long,
    onFechaContratacionChange: (Long) -> Unit,
    estado: Int, // Still using Int for now (consider dropdown for status if needed)
    onEstadoChange: (Int) -> Unit
) {
    // Remove static lists as they will be passed as parameters
    // val departamentos = listOf(...)
    // val cargosPorDepartamento = mapOf(...)

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fechaContratacion
    )    // Estados para controlar los dropdowns
    var expandedDepartamento by remember { mutableStateOf(false) }
    var expandedCargo by remember { mutableStateOf(false) }
    var expandedEstado by remember { mutableStateOf(false) } // Estado para el dropdown Estado


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
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Use spacedBy for consistent spacing
        ) {            // Departamento Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedDepartamento,
                onExpandedChange = { expandedDepartamento = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedDepartamento?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Departamento") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDepartamento) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedDepartamento,
                    onDismissRequest = { expandedDepartamento = false }
                ) {
                    departamentos.forEach { depto ->
                        DropdownMenuItem(
                            text = { Text(depto.nombre ?: "Sin Nombre") },
                            onClick = {
                                onDepartamentoSelected(depto)
                                expandedDepartamento = false
                            }
                        )
                    }
                    // Opción para limpiar la selección
                    if (selectedDepartamento != null) {
                        DropdownMenuItem(
                            text = { Text("Limpiar selección") },
                            onClick = {
                                onDepartamentoSelected(null)
                                expandedDepartamento = false
                            }
                        )
                    }
                }
            }

            // Cargo Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedCargo,
                onExpandedChange = { expandedCargo = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCargo?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Cargo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCargo) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedCargo,
                    onDismissRequest = { expandedCargo = false }
                ) {
                    cargos.forEach { cargo ->
                        DropdownMenuItem(
                            text = { Text(cargo.nombre ?: "Sin Nombre") },
                            onClick = {
                                onCargoSelected(cargo)
                                expandedCargo = false
                            }
                        )
                    }
                    // Opción para limpiar la selección
                    if (selectedCargo != null) {
                        DropdownMenuItem(
                            text = { Text("Limpiar selección") },
                            onClick = {
                                onCargoSelected(null)
                                expandedCargo = false
                            }
                        )
                    }
                }
            }

            // Estado Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedEstado,
                onExpandedChange = { expandedEstado = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = when (estado) {
                        1 -> "Activo"
                        0 -> "Inactivo"
                        else -> ""
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado) },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedEstado,
                    onDismissRequest = { expandedEstado = false }
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

// Fecha de Contratación
            OutlinedTextField(
                value = if (fechaContratacion > 0) {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = fechaContratacion
                    "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
                } else "",
                onValueChange = {},
                label = { Text("Fecha de Contratación") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Filled.Info, contentDescription = "Seleccionar fecha")
                    }
                }
            )

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
    }
}
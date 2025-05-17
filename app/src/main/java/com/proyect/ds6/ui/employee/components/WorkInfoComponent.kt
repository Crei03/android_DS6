package com.proyect.ds6.ui.employee.components // Adjust package as needed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable // Import clickable modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
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
// Import the helper dropdown composable
import com.proyect.ds6.ui.components.DropdownSelector


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
    )

    // Remove local expanded states for dropdowns handled by DropdownSelector
    // var expandedDepartamento by remember { mutableStateOf(false) }
    // var expandedCargo by remember { mutableStateOf(false) }
    var expandedEstado by remember { mutableStateOf(false) } // Keep local state for static Estado dropdown


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
        ) {
            Text(
                text = "Información de Trabajo",
                style = MaterialTheme.typography.headlineSmall,
                // modifier = Modifier.padding(bottom = 16.dp) // Spacing handled by Column arrangement
            )

            // Departamento Dropdown, using DropdownSelector helper
            DropdownSelector(
                label = "Departamento",
                options = departamentos, // Use the list passed from ViewModel
                selectedOption = selectedDepartamento, // Use the selected object state
                onOptionSelected = onDepartamentoSelected, // Use the callback to update selection
                optionToString = { it.nombre ?: "Sin Nombre" } // Define how to display a Departamento
            )

            // Cargo Dropdown, using DropdownSelector helper
            DropdownSelector(
                label = "Cargo",
                options = cargos, // Use the list passed from ViewModel
                selectedOption = selectedCargo, // Use the selected object state
                onOptionSelected = onCargoSelected, // Use the callback to update selection
                optionToString = { it.nombre ?: "Sin Nombre" } // Define how to display a Cargo
                // Note: If Cargo filtering by Departamento is needed in the UI,
                // the 'cargos' list passed here would be the already filtered list from the ViewModel.
            )

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
                        Icon(Icons.Filled.CalendarToday, contentDescription = "Seleccionar fecha")
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
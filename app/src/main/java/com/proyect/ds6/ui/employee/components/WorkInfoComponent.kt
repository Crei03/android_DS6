package com.proyect.ds6.ui.employee.components // Adjust package as needed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable // Import clickable modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday // Changed from Info to CalendarToday for date icon
import androidx.compose.material3.*
import androidx.compose.runtime.* // Import necessary Compose state APIs
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat // Import SimpleDateFormat
import java.util.*
// Import necessary Material 3 DatePicker components
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.proyect.ds6.R



// Import the data classes for Cargo and Departamento
import com.proyect.ds6.model.Cargo
import com.proyect.ds6.model.Departamento


/**
 * Componente que muestra los campos para la información de trabajo del empleado,
 * incluyendo la selección de Departamento y Cargo desde listas proporcionadas.
 * Ahora incluye parámetros para mostrar mensajes de error de validación para Departamento y Cargo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkInfoComponent(
    // --- Parámetros actualizados para las listas y selecciones ---
    departamentos: List<Departamento>, // Lista de departamentos obtenida del ViewModel
    selectedDepartamento: Departamento?, // Departamento actualmente seleccionado (objeto)
    onDepartamentoSelected: (Departamento?) -> Unit, // Callback cuando se selecciona un departamento
    departamentoError: String? = null, // Added error parameter for departamento

    cargos: List<Cargo>, // Lista de cargos (obtenida del ViewModel)
    selectedCargo: Cargo?, // Cargo actualmente seleccionado (objeto)
    onCargoSelected: (Cargo?) -> Unit, // Callback cuando se selecciona un cargo
    cargoError: String? = null, // Added error parameter for cargo
    // --- Fin de parámetros actualizados ---

    fechaContratacion: Long,
    onFechaContratacionChange: (Long) -> Unit,
    estado: Int, // Still using Int for now (consider dropdown for status if needed)
    onEstadoChange: (Int) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fechaContratacion
    )

    // Estados para controlar los dropdowns
    var expandedDepartamento by remember { mutableStateOf(false) }
    var expandedCargo by remember { mutableStateOf(false) } // Corrected state type
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
        ) {
            // Departamento Dropdown
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
                        .menuAnchor(),
                    isError = departamentoError != null // Indicate error visually
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
            // Display error message for Departamento if not null
            if (departamentoError != null) {
                Text(
                    text = departamentoError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
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
                        .menuAnchor(),
                    isError = cargoError != null // Indicate error visually
                )

                ExposedDropdownMenu(
                    expanded = expandedCargo,
                    onDismissRequest = { expandedCargo = false }
                ) {
                    // It's assumed that the 'cargos' list passed to this component
                    // is already filtered by the selected department in the parent Composable.
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
            // Display error message for Cargo if not null
            if (cargoError != null) {
                Text(
                    text = cargoError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }


            // Estado Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedEstado,
                onExpandedChange = { expandedEstado = it },
                modifier = Modifier.fillMaxWidth() // <-- Asegura ancho completo
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
                    modifier = Modifier
                        .fillMaxWidth() // <-- Asegura ancho completo
                        .menuAnchor()
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
                    // Format date as dd/MM/yyyy for display
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
                } else "",
                onValueChange = {}, // Read-only field
                label = { Text("Fecha de Contratación") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }, // Open DatePicker on click
                readOnly = true, // Prevent direct typing
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.calendar_month_24px),
                            contentDescription = "Seleccionar fecha"
                        )
                    }
                }
                // Note: DatePicker itself doesn't typically have a direct error state in the UI field
                // unless you add manual validation for the selected date value.
            )
        }
    }

    // Date Picker Dialog
    // This dialog is rendered outside the Card so it floats correctly
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Get the selected date from the state
                        datePickerState.selectedDateMillis?.let {
                            onFechaContratacionChange(it) // Pass the selected timestamp back
                        }
                        showDatePicker = false // Hide the dialog
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
            // The DatePicker content itself
            DatePicker(state = datePickerState)
        }
    }
}

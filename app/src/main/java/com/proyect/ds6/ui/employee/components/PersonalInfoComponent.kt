package com.proyect.ds6.ui.employee.components // Adjust package as needed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable // Import clickable modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.* // Import necessary Compose state APIs
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.*
// Import necessary Material 3 DatePicker components
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.ui.Alignment // Import Alignment for the DatePicker dialog buttons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.proyect.ds6.R

// Import the data class for Nacionalidad
import com.proyect.ds6.model.Nacionalidad
// Ya no necesitamos importar el DropdownSelector
// import com.proyect.ds6.ui.components.DropdownSelector


/**
 * Componente que muestra los campos para la información personal del empleado,
 * incluyendo la selección de Nacionalidad desde una lista proporcionada y un DatePicker para la fecha.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoComponent(
    cedula: String,
    onCedulaChange: (String) -> Unit,
    primerNombre: String,
    onPrimerNombreChange: (String) -> Unit,
    segundoNombre: String,
    onSegundoNombreChange: (String) -> Unit,
    primerApellido: String,
    onPrimerApellidoChange: (String) -> Unit,    segundoApellido: String,
    onSegundoApellidoChange: (String) -> Unit,
    fechaNacimiento: Long,
    onFechaNacimientoChange: (Long) -> Unit,
    genero: String, // Still using String for now
    onGeneroChange: (String) -> Unit, // Still using String for now
    estadoCivil: String, // Still using String for now
    onEstadoCivilChange: (String) -> Unit, // Still using String for now
    tipoSangre: String, // Still using String for now
    onTipoSangreChange: (String) -> Unit, // Still using String for now
    usaAc: Int = 0, // Parámetro para "Usa apellido de casa" (0=No, 1=Sí)
    onUsaAcChange: (Int) -> Unit = {}, // Callback para cambios en "Usa apellido de casa"
    // --- Parámetros para la Nacionalidad desde el ViewModel ---
    nationalities: List<Nacionalidad>, // Lista de nacionalidades obtenida del ViewModel
    selectedNacionalidad: Nacionalidad?, // Nacionalidad actualmente seleccionada (objeto)
    onNacionalidadSelected: (Nacionalidad?) -> Unit // Callback cuando se selecciona una nacionalidad
    // --- Fin de parámetros ---
) {
    // State to control the visibility of the DatePicker dialog
    var showDatePicker by remember { mutableStateOf(false) }

    // State for the DatePicker itself
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fechaNacimiento // Initialize with the current date value
    )

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

            // Campo de Cédula
            OutlinedTextField(
                value = cedula,
                onValueChange = onCedulaChange,
                label = { Text("Cédula") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Fila para nombres
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = primerNombre,
                    onValueChange = onPrimerNombreChange,
                    label = { Text("Primer Nombre") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = segundoNombre,
                    onValueChange = onSegundoNombreChange,
                    label = { Text("Segundo Nombre") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Fila para apellidos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = primerApellido,
                    onValueChange = onPrimerApellidoChange,
                    label = { Text("Primer Apellido") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = segundoApellido,
                    onValueChange = onSegundoApellidoChange,
                    label = { Text("Segundo Apellido") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Campo de Fecha de Nacimiento
            OutlinedTextField(
                value = if (fechaNacimiento > 0) {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = fechaNacimiento
                    "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
                } else "",
                onValueChange = {}, // Campo de solo lectura
                label = { Text("Fecha de Nacimiento") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }, // Abre el DatePicker al hacer clic
                readOnly = true, // Evita que se escriba directamente
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.calendar_month_24px),
                            contentDescription = "Seleccionar fecha"
                        )
                    }
                }
            )

// Dialog para seleccionar la fecha
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    onFechaNacimientoChange(it) // Actualiza la fecha seleccionada
                                }
                                showDatePicker = false // Cierra el diálogo
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

            // Género
            var expandedGenero by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedGenero,
                onExpandedChange = { expandedGenero = it },
                modifier = Modifier.fillMaxWidth()
            ) {                OutlinedTextField(
                    value = genero,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Género") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGenero) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor() // Ensure menuAnchor is applied
                )

                ExposedDropdownMenu(
                    expanded = expandedGenero,
                    onDismissRequest = { expandedGenero = false }
                ) {
                    listOf("Masculino", "Femenino").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onGeneroChange(option) // Update the selected value
                                expandedGenero = false // Close the dropdown
                            }
                        )
                    }
                }
            }

// Estado Civil
            var expandedEstadoCivil by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedEstadoCivil,
                onExpandedChange = { expandedEstadoCivil = it },
                modifier = Modifier.fillMaxWidth()
            ) {                OutlinedTextField(
                    value = estadoCivil,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado Civil") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstadoCivil) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedEstadoCivil,
                    onDismissRequest = { expandedEstadoCivil = false }
                ) {
                    listOf("Soltero/a", "Casado/a", "Divorciado/a", "Viudo/a").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onEstadoCivilChange(option)
                                expandedEstadoCivil = false
                            }
                        )
                    }
                }
            }
                        // Campo "Usa apellido de casa" - Solo visible para mujeres casadas o viudas
            if (genero == "Femenino" && (estadoCivil == "Casado/a" || estadoCivil == "Viudo/a")) {
                var expandedUsaAc by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedUsaAc,
                    onExpandedChange = { expandedUsaAc = it },
                    modifier = Modifier.fillMaxWidth()                ) {                    
                    OutlinedTextField(
                        value = if (usaAc == 1) "Sí" else "No",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Usa apellido de casa") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUsaAc) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedUsaAc,
                        onDismissRequest = { expandedUsaAc = false }
                    ) {
                        listOf(1 to "Sí", 0 to "No").forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    onUsaAcChange(value)
                                    expandedUsaAc = false
                                }
                            )
                        }
                    }
                }
            }

// Tipo de Sangre
            var expandedTipoSangre by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedTipoSangre,
                onExpandedChange = { expandedTipoSangre = it },
                modifier = Modifier.fillMaxWidth()            ) {                
                OutlinedTextField(
                    value = tipoSangre,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Sangre") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipoSangre) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedTipoSangre,
                    onDismissRequest = { expandedTipoSangre = false }
                ) {
                    listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onTipoSangreChange(option)
                                expandedTipoSangre = false
                            }
                        )
                    }
                }
            }            // Nacionalidad
            var expandedNacionalidad by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedNacionalidad,
                onExpandedChange = { expandedNacionalidad = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedNacionalidad?.pais ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nacionalidad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedNacionalidad) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedNacionalidad,
                    onDismissRequest = { expandedNacionalidad = false }
                ) {
                    nationalities.forEach { nacionalidad ->
                        DropdownMenuItem(
                            text = { Text(nacionalidad.pais ?: "Sin País") },
                            onClick = {
                                onNacionalidadSelected(nacionalidad)
                                expandedNacionalidad = false
                            }
                        )
                    }
                    // Opción para limpiar la selección
                    if (selectedNacionalidad != null) {
                        DropdownMenuItem(
                            text = { Text("Limpiar selección") },
                            onClick = {
                                onNacionalidadSelected(null)
                                expandedNacionalidad = false
                            }
                        )
                    }
                }
            }
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
                            onFechaNacimientoChange(it) // Pass the selected timestamp back
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
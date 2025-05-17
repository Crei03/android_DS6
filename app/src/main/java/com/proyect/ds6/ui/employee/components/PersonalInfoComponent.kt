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

// Import the data class for Nacionalidad
import com.proyect.ds6.model.Nacionalidad
import java.text.SimpleDateFormat // Import SimpleDateFormat for date formatting

/**
 * Componente que muestra los campos para la información personal del empleado,
 * incluyendo la selección de Nacionalidad desde una lista proporcionada y un DatePicker para la fecha.
 * Ahora incluye parámetros para mostrar mensajes de error de validación y control de editabilidad de cédula,
 * además de validación de entrada en tiempo real para ciertos campos, incluyendo límite de longitud.
 * El campo Tipo de Sangre es un dropdown con valores predefinidos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoComponent(
    cedula: String,
    onCedulaChange: (String) -> Unit,
    readOnlyCedula: Boolean = false, // New parameter to control cedula editability
    cedulaError: String? = null, // Added error parameter for cedula

    primerNombre: String,
    onPrimerNombreChange: (String) -> Unit,
    primerNombreError: String? = null, // Added error parameter

    segundoNombre: String,
    onSegundoNombreChange: (String) -> Unit,
    segundoNombreError: String? = null, // Added error parameter

    primerApellido: String,
    onPrimerApellidoChange: (String) -> Unit,
    primerApellidoError: String? = null, // Added error parameter

    segundoApellido: String,
    onSegundoApellidoChange: (String) -> Unit,
    segundoApellidoError: String? = null, // Added error parameter

    apellidoCasado: String, // Added apellidoCasado state
    onApellidoCasadoChange: (String) -> Unit, // Added callback for apellidoCasado
    apellidoCasadoError: String? = null, // Added error parameter for apellidoCasado

    fechaNacimiento: Long,
    fechaNacimientoError: String? = null,
    onFechaNacimientoChange: (Long) -> Unit,

    genero: String, // Still using String for now
    generoError: String? = null,
    onGeneroChange: (String) -> Unit, // Still using String for now

    estadoCivil: String, // Still using String for now
    estadoCivilError: String? = null,
    onEstadoCivilChange: (String) -> Unit, // Still using String for now

    tipoSangre: String, // Still using String for now
    onTipoSangreChange: (String) -> Unit, // Still using String for now
    tipoSangreError: String? = null, // Added error parameter

    usaAc: Int = 0, // Parámetro para "Usa apellido de casa" (0=No, 1=Sí)
    onUsaAcChange: (Int) -> Unit = {}, // Callback para cambios en "Usa apellido de casa"
    nacionalidadError: String? = null,
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

    // List of predefined blood types
    val bloodTypes = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")


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
                onValueChange = { newValue ->
                    // Allow only digits and hyphens and limit length to 13
                    val filteredValue = newValue.filter { it.isDigit() || it == '-' }.take(13)
                    onCedulaChange(filteredValue)
                },
                label = { Text("Cédula") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Suggest numeric keyboard
                readOnly = readOnlyCedula, // Use the new parameter to control editability
                isError = cedulaError != null, // Set isError based on the error state
                supportingText = { // Display error message if not null
                    if (cedulaError != null) {
                        Text(cedulaError)
                    }
                }
            )

            // Fila para nombres
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = primerNombre,
                    onValueChange = { newValue ->
                        // Allow only letters and spaces and limit length to 25
                        val filteredValue = newValue.filter { it.isLetter() || it.isWhitespace() }.take(25)
                        onPrimerNombreChange(filteredValue)
                    },
                    label = { Text("Primer Nombre") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), // Suggest text keyboard
                    isError = primerNombreError != null, // Set isError based on the error state
                    supportingText = { // Display error message if not null
                        if (primerNombreError != null) {
                            Text(primerNombreError)
                        }
                    }
                )
                OutlinedTextField(
                    value = segundoNombre,
                    onValueChange = { newValue ->
                        // Allow only letters and spaces and limit length to 25
                        val filteredValue = newValue.filter { it.isLetter() || it.isWhitespace() }.take(25)
                        onSegundoNombreChange(filteredValue)
                    },
                    label = { Text("Segundo Nombre") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), // Suggest text keyboard
                    isError = segundoNombreError != null, // Set isError based on the error state
                    supportingText = { // Display error message if not null
                        if (segundoNombreError != null) {
                            Text(segundoNombreError)
                        }
                    }
                )
            }

            // Fila para apellidos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = primerApellido,
                    onValueChange = { newValue ->
                        // Allow only letters and spaces and limit length to 25
                        val filteredValue = newValue.filter { it.isLetter() || it.isWhitespace() }.take(25)
                        onPrimerApellidoChange(filteredValue)
                    },
                    label = { Text("Primer Apellido") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), // Suggest text keyboard
                    isError = primerApellidoError != null, // Set isError based on the error state
                    supportingText = { // Display error message if not null
                        if (primerApellidoError != null) {
                            Text(primerApellidoError)
                        }
                    }
                )
                OutlinedTextField(
                    value = segundoApellido,
                    onValueChange = { newValue ->
                        // Allow only letters and spaces and limit length to 25
                        val filteredValue = newValue.filter { it.isLetter() || it.isWhitespace() }.take(25)
                        onSegundoApellidoChange(filteredValue)
                    },
                    label = { Text("Segundo Apellido") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), // Suggest text keyboard
                    isError = segundoApellidoError != null, // Set isError based on the error state
                    supportingText = { // Display error message if not null
                        if (segundoApellidoError != null) {
                            Text(segundoApellidoError)
                        }
                    }
                )
            }

            // Campo de Apellido de Casado - Visible solo si es Femenino y Casado/a o Viudo/a
            if (genero == "Femenino" && (estadoCivil == "Casado/a" || estadoCivil == "Viudo/a")) {
                OutlinedTextField(
                    value = apellidoCasado,
                    onValueChange = { newValue ->
                        // Allow only letters and spaces and limit length to 25
                        val filteredValue = newValue.filter { it.isLetter() || it.isWhitespace() }.take(25)
                        onApellidoCasadoChange(filteredValue)
                    },
                    label = { Text("Apellido de Casado") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), // Suggest text keyboard
                    isError = apellidoCasadoError != null, // Set isError based on the error state
                    supportingText = { // Display error message if not null
                        if (apellidoCasadoError != null) {
                            Text(apellidoCasadoError)
                        }
                    }
                )
            }


            // Campo de Fecha de Nacimiento (Read-only, opens DatePicker)
            OutlinedTextField(
                value = if (fechaNacimiento > 0) {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = fechaNacimiento
                    // Format date as dd/MM/yyyy for display
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
                } else "",
                onValueChange = {}, // Campo de solo lectura
                label = { Text("Fecha de Nacimiento") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }, // Abre el DatePicker al hacer clic
                readOnly = true, // Evita que se escriba directamente
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar fecha")
                    }
                }
                // Note: DatePicker itself doesn't typically have a direct error state in the UI field
                // unless you add manual validation for the selected date value.
            )

            // Género Dropdown
            var expandedGenero by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedGenero,
                onExpandedChange = { expandedGenero = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
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
                    listOf("Masculino", "Femenino", "Otro").forEach { option -> // Added "Otro" option
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

            // Estado Civil Dropdown
            var expandedEstadoCivil by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedEstadoCivil,
                onExpandedChange = { expandedEstadoCivil = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
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
                    // Added "Unión libre" option
                    listOf("Soltero/a", "Casado/a", "Divorciado/a", "Viudo/a", "Unión libre").forEach { option ->
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

            // Tipo de Sangre Dropdown
            var expandedTipoSangre by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedTipoSangre,
                onExpandedChange = { expandedTipoSangre = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = tipoSangre, // Use the state value
                    onValueChange = {}, // Read-only as it's a dropdown
                    readOnly = true,
                    label = { Text("Tipo de Sangre") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipoSangre) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    isError = tipoSangreError != null // Set isError based on the error state
                )

                ExposedDropdownMenu(
                    expanded = expandedTipoSangre,
                    onDismissRequest = { expandedTipoSangre = false }
                ) {
                    bloodTypes.forEach { option -> // Use the predefined list
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onTipoSangreChange(option) // Update the selected value
                                expandedTipoSangre = false // Close the dropdown
                            }
                        )
                    }
                    // Option to clear the selection if needed (optional)
                    if (tipoSangre.isNotEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Limpiar selección") },
                            onClick = {
                                onTipoSangreChange("") // Clear the selected value
                                expandedTipoSangre = false
                            }
                        )
                    }
                }
            }
            // Display error message for TipoSangre if not null
            if (tipoSangreError != null) {
                Text(
                    text = tipoSangreError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }


            // Nacionalidad Dropdown
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
                                // TODO: Add error handling for dropdowns if they are required fields
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

            // Campo "Usa apellido de casa" - Solo visible para mujeres casadas o viudas
            if (genero == "Femenino" && (estadoCivil == "Casado/a" || estadoCivil == "Viudo/a")) {
                var expandedUsaAc by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedUsaAc,
                    onExpandedChange = { expandedUsaAc = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
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

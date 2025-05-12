package com.proyect.ds6.ui.employee.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.*

/**
 * Componente que muestra los campos para la información personal del empleado
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
    onPrimerApellidoChange: (String) -> Unit,
    segundoApellido: String,
    onSegundoApellidoChange: (String) -> Unit,
    fechaNacimiento: Long,
    onFechaNacimientoChange: (Long) -> Unit,
    genero: String,
    onGeneroChange: (String) -> Unit,
    estadoCivil: String,
    onEstadoCivilChange: (String) -> Unit,
    tipoSangre: String,
    onTipoSangreChange: (String) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = fechaNacimiento
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
                .fillMaxWidth()
        ) {
            Text(
                text = "Información Personal",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo de Cédula
            OutlinedTextField(
                value = cedula,
                onValueChange = onCedulaChange,
                label = { Text("Cédula") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha de Nacimiento
            OutlinedTextField(
                value = if (fechaNacimiento > 0) {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = fechaNacimiento
                    "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
                } else "",
                onValueChange = {},
                label = { Text("Fecha de Nacimiento") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Text("📅")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Género
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = {},
            ) {
                OutlinedTextField(
                    value = genero,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Género") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = false,
                    onDismissRequest = {},
                ) {
                    listOf("Masculino", "Femenino", "Otro").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { onGeneroChange(option) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Estado Civil
            var expandedEstadoCivil by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedEstadoCivil,
                onExpandedChange = { expandedEstadoCivil = it },
            ) {
                OutlinedTextField(
                    value = estadoCivil,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado Civil") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstadoCivil) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedEstadoCivil,
                    onDismissRequest = { expandedEstadoCivil = false },
                ) {
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

            Spacer(modifier = Modifier.height(16.dp))

            // Tipo de Sangre
            var expandedTipoSangre by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedTipoSangre,
                onExpandedChange = { expandedTipoSangre = it },
            ) {
                OutlinedTextField(
                    value = tipoSangre,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Sangre") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipoSangre) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedTipoSangre,
                    onDismissRequest = { expandedTipoSangre = false },
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
                            onFechaNacimientoChange(it)
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
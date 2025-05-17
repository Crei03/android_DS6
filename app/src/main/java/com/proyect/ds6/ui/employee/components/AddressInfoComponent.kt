package com.proyect.ds6.ui.employee.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.proyect.ds6.model.Corregimiento
import com.proyect.ds6.model.Distrito
import com.proyect.ds6.model.Provincia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressInfoComponent(
    // Listas y selecciones
    provinces: List<Provincia>,
    selectedProvincia: Provincia?,
    onProvinciaSelected: (Provincia?) -> Unit,
    provinciaError: String? = null,
    distritoError: String? = null,
    corregimientoError: String? = null,

    distritos: List<Distrito>,
    selectedDistrito: Distrito?,
    onDistritoSelected: (Distrito?) -> Unit,

    corregimientos: List<Corregimiento>,
    selectedCorregimiento: Corregimiento?,
    onCorregimientoSelected: (Corregimiento?) -> Unit,

    calle: String,
    onCalleChange: (String) -> Unit,
    calleError: String? = null, // Added error parameter for calle

    casa: String,
    onCasaChange: (String) -> Unit,
    casaError: String? = null, // Added error parameter for casa

    comunidad: String,
    onComunidadChange: (String) -> Unit,
    comunidadError: String? = null // Added error parameter for comunidad
) {
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Provincia Dropdown
            var expandedProvincia by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedProvincia,
                onExpandedChange = { expandedProvincia = it },
            ) {
                OutlinedTextField(
                    value = selectedProvincia?.nombre_provincia ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Provincia") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProvincia) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor() // Ensure menuAnchor is applied
                )

                ExposedDropdownMenu(
                    expanded = expandedProvincia,
                    onDismissRequest = { expandedProvincia = false },
                ) {
                    provinces.forEach { prov ->
                        DropdownMenuItem(
                            text = { Text(prov.nombre_provincia) },
                            onClick = {
                                onProvinciaSelected(prov)
                                expandedProvincia = false
                                // TODO: Add error handling for dropdowns if they are required fields
                            }
                        )
                    }
                }
            }

            // Distrito Dropdown
            var expandedDistrito by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedDistrito,
                onExpandedChange = { expandedDistrito = it },
            ) {
                OutlinedTextField(
                    value = selectedDistrito?.nombre_distrito ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Distrito") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDistrito) },
                    enabled = selectedProvincia != null, // Enable only if a province is selected
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedDistrito,
                    onDismissRequest = { expandedDistrito = false },
                ) {
                    // Filter distritos based on the selected province
                    distritos.filter { it.codigo_provincia == selectedProvincia?.codigo_provincia }.forEach { dist ->
                        DropdownMenuItem(
                            text = { Text(dist.nombre_distrito) },
                            onClick = {
                                onDistritoSelected(dist)
                                expandedDistrito = false
                                // TODO: Add error handling for dropdowns if they are required fields
                            }
                        )
                    }
                }
            }

            // Corregimiento Dropdown
            var expandedCorregimiento by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedCorregimiento,
                onExpandedChange = { expandedCorregimiento = it },
            ) {
                OutlinedTextField(
                    value = selectedCorregimiento?.nombre_corregimiento ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Corregimiento") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCorregimiento) },
                    enabled = selectedDistrito != null, // Enable only if a district is selected
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedCorregimiento,
                    onDismissRequest = { expandedCorregimiento = false },
                ) {
                    // Filter corregimientos based on the selected province and district
                    corregimientos.filter { it.codigo_provincia == selectedProvincia?.codigo_provincia && it.codigo_distrito == selectedDistrito?.codigo_distrito }.forEach { corr ->
                        DropdownMenuItem(
                            text = { Text(corr.nombre_corregimiento) },
                            onClick = {
                                onCorregimientoSelected(corr)
                                expandedCorregimiento = false
                                // TODO: Add error handling for dropdowns if they are required fields
                            }
                        )
                    }
                }
            }

            // Calle TextField
            OutlinedTextField(
                value = calle,
                onValueChange = { newValue ->
                    // Allow letters, numbers, spaces, and common address symbols (e.g., #, -, .) and limit length to 30
                    val filteredValue = newValue.filter { it.isLetterOrDigit() || it.isWhitespace() || it == '#' || it == '-' || it == '.' }.take(30)
                    onCalleChange(filteredValue)
                },
                label = { Text("Calle") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), // Suggest text keyboard
                isError = calleError != null, // Set isError based on the error state
                supportingText = { // Display error message if not null
                    if (calleError != null) {
                        Text(calleError)
                    }
                }
            )

            // Fila para Casa y Comunidad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Casa TextField
                OutlinedTextField(
                    value = casa,
                    onValueChange = { newValue ->
                        // Allow letters, numbers, and hyphens for house number/name and limit length to 10
                        val filteredValue = newValue.filter { it.isLetterOrDigit() || it == '-' }.take(10)
                        onCasaChange(filteredValue)
                    },
                    label = { Text("Casa") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), // Suggest text/number keyboard
                    isError = casaError != null, // Set isError based on the error state
                    supportingText = { // Display error message if not null
                        if (casaError != null) {
                            Text(casaError)
                        }
                    }
                )
                // Comunidad TextField
                OutlinedTextField(
                    value = comunidad,
                    onValueChange = { newValue ->
                        // Allow letters, numbers, and spaces for community name and limit length to 25
                        val filteredValue = newValue.filter { it.isLetterOrDigit() || it.isWhitespace() }.take(25)
                        onComunidadChange(filteredValue)
                    },
                    label = { Text("Comunidad") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), // Suggest text keyboard
                    isError = comunidadError != null, // Set isError based on the error state
                    supportingText = { // Display error message if not null
                        if (comunidadError != null) {
                            Text(comunidadError)
                        }
                    }
                )
            }
        }
    }
}

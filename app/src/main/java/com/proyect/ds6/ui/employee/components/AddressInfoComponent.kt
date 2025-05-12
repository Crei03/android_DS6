package com.proyect.ds6.ui.employee.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Componente que muestra los campos para la información de dirección del empleado
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressInfoComponent(
    provincia: String,
    onProvinciaChange: (String) -> Unit,
    distrito: String,
    onDistritoChange: (String) -> Unit,
    corregimiento: String,
    onCorregimientoChange: (String) -> Unit,
    calle: String,
    onCalleChange: (String) -> Unit,
    casa: String,
    onCasaChange: (String) -> Unit,
    comunidad: String,
    onComunidadChange: (String) -> Unit
) {
    // Lista de provincias de Panamá
    val provincias = listOf(
        "Bocas del Toro", "Chiriquí", "Coclé", "Colón", "Darién", 
        "Herrera", "Los Santos", "Panamá", "Panamá Oeste", "Veraguas",
        "Comarca Emberá-Wounaan", "Comarca Guna Yala", "Comarca Ngäbe-Buglé"
    )

    // Mapeo estático de distritos por provincia (solo algunos ejemplos)
    val distritosPorProvincia = mapOf(
        "Panamá" to listOf("Panamá", "San Miguelito", "Taboga", "Balboa", "Chepo"),
        "Chiriquí" to listOf("David", "Barú", "Bugaba", "Alanje", "Boquerón"),
        "Colón" to listOf("Colón", "Chagres", "Donoso", "Portobelo", "Santa Isabel")
    )

    // Mapeo estático de corregimientos por distrito (solo algunos ejemplos)
    val corregimientosPorDistrito = mapOf(
        "Panamá" to listOf("San Felipe", "El Chorrillo", "Santa Ana", "Calidonia", "Bella Vista"),
        "San Miguelito" to listOf("Amelia Denis De Icaza", "Belisario Porras", "José Domingo Espinar", "Mateo Iturralde"),
        "David" to listOf("David", "San Carlos", "San Pablo Nuevo", "San Pablo Viejo", "Cochea")
    )

    var expandedProvincia by remember { mutableStateOf(false) }
    var expandedDistrito by remember { mutableStateOf(false) }
    var expandedCorregimiento by remember { mutableStateOf(false) }

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
                text = "Información de Dirección",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Provincia
            ExposedDropdownMenuBox(
                expanded = expandedProvincia,
                onExpandedChange = { expandedProvincia = it },
            ) {
                OutlinedTextField(
                    value = provincia,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Provincia") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProvincia) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedProvincia,
                    onDismissRequest = { expandedProvincia = false },
                ) {
                    provincias.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { 
                                onProvinciaChange(option)
                                expandedProvincia = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Distrito
            ExposedDropdownMenuBox(
                expanded = expandedDistrito,
                onExpandedChange = { expandedDistrito = it },
            ) {
                OutlinedTextField(
                    value = distrito,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Distrito") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDistrito) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedDistrito,
                    onDismissRequest = { expandedDistrito = false },
                ) {
                    // Mostrar distritos de la provincia seleccionada o una lista vacía si no hay
                    val distritos = distritosPorProvincia[provincia] ?: emptyList()
                    distritos.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { 
                                onDistritoChange(option)
                                expandedDistrito = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Corregimiento
            ExposedDropdownMenuBox(
                expanded = expandedCorregimiento,
                onExpandedChange = { expandedCorregimiento = it },
            ) {
                OutlinedTextField(
                    value = corregimiento,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Corregimiento") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCorregimiento) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedCorregimiento,
                    onDismissRequest = { expandedCorregimiento = false },
                ) {
                    // Mostrar corregimientos del distrito seleccionado o una lista vacía si no hay
                    val corregimientos = corregimientosPorDistrito[distrito] ?: emptyList()
                    corregimientos.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { 
                                onCorregimientoChange(option)
                                expandedCorregimiento = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Calle
            OutlinedTextField(
                value = calle,
                onValueChange = onCalleChange,
                label = { Text("Calle") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fila para Casa y Comunidad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = casa,
                    onValueChange = onCasaChange,
                    label = { Text("Casa") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = comunidad,
                    onValueChange = onComunidadChange,
                    label = { Text("Comunidad") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
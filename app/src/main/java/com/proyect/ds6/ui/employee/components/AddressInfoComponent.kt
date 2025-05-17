package com.proyect.ds6.ui.employee.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    distritos: List<Distrito>,
    selectedDistrito: Distrito?,
    onDistritoSelected: (Distrito?) -> Unit,

    corregimientos: List<Corregimiento>,
    selectedCorregimiento: Corregimiento?,
    onCorregimientoSelected: (Corregimiento?) -> Unit,

    calle: String,
    onCalleChange: (String) -> Unit,
    casa: String,
    onCasaChange: (String) -> Unit,
    comunidad: String,
    onComunidadChange: (String) -> Unit
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
            // Provincia
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
                        .menuAnchor()
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
                            }
                        )
                    }
                }
            }

            // Distrito
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
                    enabled = selectedProvincia != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedDistrito,
                    onDismissRequest = { expandedDistrito = false },
                ) {
                    distritos.forEach { dist ->
                        DropdownMenuItem(
                            text = { Text(dist.nombre_distrito) },
                            onClick = { 
                                onDistritoSelected(dist)
                                expandedDistrito = false
                            }
                        )
                    }
                }
            }

            // Corregimiento
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
                    enabled = selectedDistrito != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedCorregimiento,
                    onDismissRequest = { expandedCorregimiento = false },
                ) {
                    corregimientos.forEach { corr ->
                        DropdownMenuItem(
                            text = { Text(corr.nombre_corregimiento) },
                            onClick = { 
                                onCorregimientoSelected(corr)
                                expandedCorregimiento = false
                            }
                        )
                    }
                }
            }

            // Calle
            OutlinedTextField(
                value = calle,
                onValueChange = onCalleChange,
                label = { Text("Calle") },
                modifier = Modifier.fillMaxWidth()
            )

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

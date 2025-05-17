package com.proyect.ds6.ui.employee.components // Adjust package as needed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.* // Import necessary Compose state APIs
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyect.ds6.model.Corregimiento // Import data classes
import com.proyect.ds6.model.Distrito
import com.proyect.ds6.model.Provincia
import com.proyect.ds6.ui.components.DropdownSelector // Import the helper dropdown composable


/**
 * Componente que muestra los campos para la información de dirección del empleado,
 * usando listas de opciones proporcionadas externamente (ej: desde un ViewModel).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressInfoComponent(
    // --- Parámetros actualizados para las listas y selecciones ---
    provinces: List<Provincia>, // Lista de provincias obtenida del ViewModel
    selectedProvincia: Provincia?, // Provincia actualmente seleccionada (objeto)
    onProvinciaSelected: (Provincia?) -> Unit, // Callback cuando se selecciona una provincia

    distritos: List<Distrito>, // Lista de distritos (filtrada por provincia, obtenida del ViewModel)
    selectedDistrito: Distrito?, // Distrito actualmente seleccionado (objeto)
    onDistritoSelected: (Distrito?) -> Unit, // Callback cuando se selecciona un distrito

    corregimientos: List<Corregimiento>, // Lista de corregimientos (filtrada por distrito, obtenida del ViewModel)
    selectedCorregimiento: Corregimiento?, // Corregimiento actualmente seleccionado (objeto)
    onCorregimientoSelected: (Corregimiento?) -> Unit, // Callback cuando se selecciona un corregimiento
    // --- Fin de parámetros actualizados ---

    calle: String,
    onCalleChange: (String) -> Unit,
    casa: String,
    onCasaChange: (String) -> Unit,
    comunidad: String,
    onComunidadChange: (String) -> Unit
) {
    // Remove static lists as they will be passed as parameters
    // val provincias = listOf(...)
    // val distritosPorProvincia = mapOf(...)
    // val corregimientosPorDistrito = mapOf(...)

    // Remove local expanded states as they are handled within DropdownSelector
    // var expandedProvincia by remember { mutableStateOf(false) }
    // var expandedDistrito by remember { mutableStateOf(false) }
    // var expandedCorregimiento by remember { mutableStateOf(false) }


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
                text = "Información de Dirección",
                style = MaterialTheme.typography.headlineSmall,
                // modifier = Modifier.padding(bottom = 16.dp) // Spacing handled by Column arrangement
            )

            // Provincia Dropdown, using DropdownSelector helper
            DropdownSelector(
                label = "Provincia",
                options = provinces, // Use the list passed from ViewModel
                selectedOption = selectedProvincia, // Use the selected object state
                onOptionSelected = onProvinciaSelected, // Use the callback to update selection
                optionToString = { it.nombre_provincia } // Define how to display a Provincia
            )

            // Distrito Dropdown, using DropdownSelector helper
            DropdownSelector(
                label = "Distrito",
                options = distritos, // Use the list passed from ViewModel (already filtered)
                selectedOption = selectedDistrito, // Use the selected object state
                onOptionSelected = onDistritoSelected, // Use the callback to update selection
                optionToString = { it.nombre_distrito }, // Define how to display a Distrito
                enabled = selectedProvincia != null // Enable only if a province is selected
            )

            // Corregimiento Dropdown, using DropdownSelector helper
            DropdownSelector(
                label = "Corregimiento",
                options = corregimientos, // Use the list passed from ViewModel (already filtered)
                selectedOption = selectedCorregimiento, // Use the selected object state
                onOptionSelected = onCorregimientoSelected, // Use the callback to update selection
                optionToString = { it.nombre_corregimiento }, // Define how to display a Corregimiento
                enabled = selectedDistrito != null // Enable only if a district is selected
            )

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

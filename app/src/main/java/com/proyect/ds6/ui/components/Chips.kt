package com.proyect.ds6.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.proyect.ds6.ui.theme.DS6Theme

/**
 * Componente que muestra un menú de filtros con chips para filtrar por estado, departamento y cargo
 */
@Composable
fun FilterChipsMenu(
    show: Boolean,
    statusOptions: List<String> = listOf("Todos", "Activos", "Inactivos"),
    departmentOptions: List<String> = emptyList(),
    roleOptions: List<String> = emptyList(),
    initialSelectedStatus: String? = null,
    initialSelectedDepartment: String? = null,
    initialSelectedRole: String? = null,
    onApplyFilters: (String?, String?, String?) -> Unit = { _, _, _ -> },
    onDismiss: () -> Unit = {}
) {
    if (!show) return
    
    var selectedStatus by remember { mutableStateOf(initialSelectedStatus) }
    var selectedDepartment by remember { mutableStateOf(initialSelectedDepartment) }
    var selectedRole by remember { mutableStateOf(initialSelectedRole) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Filtros",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Filtros por estado (Activos/Inactivos)
                Text(
                    text = "Estado:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(statusOptions) { status ->
                        FilterChip(
                            selected = selectedStatus == status,
                            onClick = {
                                selectedStatus = if (selectedStatus == status || status == "Todos") null else status
                            },
                            label = { Text(status) },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
                
                // Filtros por departamento
                Text(
                    text = "Departamento:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedDepartment == null,
                            onClick = { selectedDepartment = null },
                            label = { Text("Todos") },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                    
                    items(departmentOptions) { department ->
                        FilterChip(
                            selected = selectedDepartment == department,
                            onClick = { selectedDepartment = if (selectedDepartment == department) null else department },
                            label = { Text(department) },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
                
                // Filtros por cargo
                Text(
                    text = "Cargo:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedRole == null,
                            onClick = { selectedRole = null },
                            label = { Text("Todos") },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                    
                    items(roleOptions) { role ->
                        FilterChip(
                            selected = selectedRole == role,
                            onClick = { selectedRole = if (selectedRole == role) null else role },
                            label = { Text(role) },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botones de acción (siguiendo las guías de UI)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // Botón Cancelar (secundario)
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Cancelar")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Botón Aplicar (principal)
                    Button(
                        onClick = { 
                            onApplyFilters(selectedStatus, selectedDepartment, selectedRole)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Aplicar")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilterChipsMenuPreview() {
    DS6Theme {
        Surface {
            // Preview con datos de muestra
            FilterChipsMenu(
                show = true,
                departmentOptions = listOf("Recursos Humanos", "Contabilidad", "Tecnología", "Ventas"),
                roleOptions = listOf("Gerente", "Asistente", "Desarrollador", "Contador")
            )
        }
    }
}
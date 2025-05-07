package com.proyect.ds6.ui.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyect.ds6.ui.theme.DS6Theme

/**
 * Modelo de datos para representar un empleado en la tabla
 */
data class EmployeeData(
    val id: String,
    val fullName: String,
    val position: String,
    val dateAdded: String
)

/**
 * Componente para mostrar una tabla con los 5 empleados más recientes
 */
@Composable
fun RecentEmployeesTable(employees: List<EmployeeData>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Encabezado de la tabla
            Text(
                text = "Empleados Recientes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Encabezados de las columnas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Cédula",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Nombre",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1.5f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Cargo",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Fecha",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Lista de empleados
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)  // Establece una altura máxima fija
            ) {
                items(employees) { employee ->
                    EmployeeRow(employee)
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }
        }
    }
}

/**
 * Fila individual para un empleado en la tabla
 */
@Composable
fun EmployeeRow(employee: EmployeeData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = employee.id,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = employee.fullName,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1.5f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = employee.position,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = employee.dateAdded,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecentEmployeesTablePreview() {
    val mockEmployees = listOf(
        EmployeeData("V-12345678", "Carlos Rodriguez", "Gerente", "01/05/2025"),
        EmployeeData("V-23456789", "María González", "Analista", "30/04/2025"),
        EmployeeData("V-34567890", "Pedro Pérez", "Desarrollador", "29/04/2025"),
        EmployeeData("V-45678901", "Ana López", "Diseñadora", "28/04/2025"),
        EmployeeData("V-56789012", "Luis Torres", "Contador", "27/04/2025")
    )
    
    DS6Theme {
        RecentEmployeesTable(employees = mockEmployees)
    }
}
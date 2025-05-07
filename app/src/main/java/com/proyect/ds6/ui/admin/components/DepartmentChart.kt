package com.proyect.ds6.ui.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyect.ds6.ui.theme.*

/**
 * Modelo de datos para representar departamentos y cantidad de empleados
 */
data class DepartmentData(
    val name: String,
    val employeeCount: Int,
    val barColor: Color
)

/**
 * Componente para mostrar la cantidad de empleados por departamento
 */
@Composable
fun DepartmentChart(departments: List<DepartmentData>) {
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
            // Título del componente
            Text(
                text = "Empleados por Departamento",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Lista de departamentos
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                items(departments) { department ->
                    DepartmentItem(department)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

/**
 * Item individual para cada departamento con barra de progreso
 */
@Composable
fun DepartmentItem(department: DepartmentData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = department.name,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = department.employeeCount.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Barra de progreso personalizada
        LinearProgressIndicator(
            progress = { 1f },  // Siempre completa, solo cambia el color
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = department.barColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DepartmentChartPreview() {
    val mockDepartments = listOf(
        DepartmentData("Tecnología", 45, DS6Primary),
        DepartmentData("Ventas", 32, DS6PrimaryLight),
        DepartmentData("Recursos Humanos", 18, DS6Error),
        DepartmentData("Finanzas", 27, DS6InteractiveElement),
        DepartmentData("Operaciones", 23, DS6PrimaryDark)
    )
    
    DS6Theme {
        DepartmentChart(departments = mockDepartments)
    }
}
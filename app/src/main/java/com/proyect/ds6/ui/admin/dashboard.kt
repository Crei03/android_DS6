package com.proyect.ds6.ui.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.proyect.ds6.R
import com.proyect.ds6.ui.admin.components.*
import com.proyect.ds6.ui.theme.DS6Theme

/**
 * Dashboard principal para el administrador
 * Muestra varios componentes con información resumida
 */
@Composable
fun Dashboard() {
    val scrollState = rememberScrollState()
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // Header de bienvenida
            HeaderDashboard(
                userName = "Carlos Rodríguez",
                userRole = "Administrador de Recursos Humanos"
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tarjetas de información resumida
            InfoCardsGrid()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tabla de empleados recientes
            RecentEmployeesTable(getRecentEmployees())
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Distribución por departamento
            DepartmentChart(getDepartmentData())
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Distribución por género
            GenderDistribution(getGenderData())
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Distribución por rango de edad
            AgeRangeDistribution(getAgeRangeData())
            
            // Espacio adicional al final para mejorar el scroll
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Grid de tarjetas de información
 */
@Composable
fun InfoCardsGrid() {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        InfoCard(
            icon = Icons.Default.Person,
            title = "Total Empleados",
            value = "145",
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        
        InfoCard(
            icon = ImageVector.vectorResource(id = R.drawable.groups_24px),
            title = "Inactivos",
            value = "12",
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        InfoCard(
            icon = ImageVector.vectorResource(id = R.drawable.badge_24px),
            title = "Eliminados",
            value = "28",
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        
        InfoCard(
            icon = ImageVector.vectorResource(id = R.drawable.person_add_24px),
            title = "Nuevos (Mes)",
            value = "5",
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
    }
}

// Funciones para proporcionar datos de prueba

private fun getRecentEmployees(): List<EmployeeData> {
    return listOf(
        EmployeeData("V-12345678", "Carlos Rodriguez", "Gerente", "01/05/2025"),
        EmployeeData("V-23456789", "María González", "Analista", "30/04/2025"),
        EmployeeData("V-34567890", "Pedro Pérez", "Desarrollador", "29/04/2025"),
        EmployeeData("V-45678901", "Ana López", "Diseñadora", "28/04/2025"),
        EmployeeData("V-56789012", "Luis Torres", "Contador", "27/04/2025")
    )
}

private fun getDepartmentData(): List<DepartmentData> {
    return listOf(
        DepartmentData("Tecnología", 45, Color(0xFF6200EE)),
        DepartmentData("Ventas", 32, Color(0xFF03DAC5)),
        DepartmentData("RRHH", 18, Color(0xFFFF6F00)),
        DepartmentData("Finanzas", 27, Color(0xFF018786)),
        DepartmentData("Operaciones", 23, Color(0xFFB00020))
    )
}

private fun getGenderData(): GenderData {
    return GenderData(
        maleCount = 82,
        femaleCount = 63
    )
}

private fun getAgeRangeData(): List<AgeRangeData> {
    return listOf(
        AgeRangeData("18-35", 68, Color(0xFF4CAF50)),  // Verde
        AgeRangeData("36-59", 52, Color(0xFFFFA000)),  // Ámbar
        AgeRangeData("60+", 25, Color(0xFF9C27B0))     // Púrpura
    )
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DS6Theme {
        Dashboard()
    }
}
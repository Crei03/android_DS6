package com.proyect.ds6.ui.employee

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
import androidx.compose.material.icons.filled.Notifications
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
import com.proyect.ds6.ui.admin.components.InfoCard
import com.proyect.ds6.ui.admin.components.HeaderDashboard
import com.proyect.ds6.ui.employee.components.*
import com.proyect.ds6.ui.theme.DS6Theme

/**
 * Dashboard principal para el empleado
 * Muestra varios componentes con información personalizada para el empleado
 */
@Composable
fun EmployeeDashboard() {
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
                userName = "Ana Gómez",
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tarjetas de información resumida
            EmployeeInfoCardsGrid()
            

            // Espacio adicional al final para mejorar el scroll
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Grid de tarjetas de información para el empleado
 */
@Composable
fun EmployeeInfoCardsGrid() {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        
        InfoCard(
            icon = Icons.Default.Notifications,
            title = "Notificaciones",
            value = "2",
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
            icon = ImageVector.vectorResource(id = R.drawable.person_add_24px),
            title = "Días en la empresa",
            value = "347",
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmployeeDashboardPreview() {
    DS6Theme {
        EmployeeDashboard()
    }
}
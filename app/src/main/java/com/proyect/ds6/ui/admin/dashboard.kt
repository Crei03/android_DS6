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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.proyect.ds6.R
import com.proyect.ds6.ui.admin.components.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import com.proyect.ds6.presentation.AdminViewModel

/**
 * Dashboard principal para el administrador
 * Muestra varios componentes con información resumida
 */
@Composable
fun AdminDashboard(
    viewModel: AdminViewModel = koinViewModel()
) {
    val scrollState = rememberScrollState()
    
    // Estados para almacenar los datos
    var employeeStats by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    var recentEmployees by remember { mutableStateOf<List<EmployeeData>>(emptyList()) }
    var departmentData by remember { mutableStateOf<List<DepartmentData>>(emptyList()) }
    var genderData by remember { mutableStateOf<GenderData>(GenderData(0, 0)) }
    var ageRangeData by remember { mutableStateOf<List<AgeRangeData>>(emptyList()) }
      // Cargar datos cuando se inicia el componente
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            // Obtener estadísticas de empleados
            viewModel.getEmployeeStats().onSuccess { stats ->
                employeeStats = stats
            }
            
            // Obtener empleados recientes
            viewModel.getRecentEmployees().onSuccess { employees ->
                recentEmployees = employees.map { emp ->
                    EmployeeData(
                        id = emp["id"] ?: "",
                        fullName = emp["fullName"] ?: "",
                        dateAdded = emp["dateAdded"] ?: ""
                    )
                }
            }
              // Obtener distribución por departamento
            viewModel.getDepartmentDistribution().onSuccess { departments ->
                // Usando un solo color para todas las barras: el color principal interactivo #3498db
                val singleColor = Color(0xFF3498db)
                
                departmentData = departments.map { dept ->
                    DepartmentData(
                        name = dept["name"] as String,
                        employeeCount = dept["employeeCount"] as Int,
                        barColor = singleColor
                    )
                }
            }
            
            // Obtener distribución por género
            viewModel.getGenderDistribution().onSuccess { gender ->
                genderData = GenderData(
                    maleCount = gender["male"] ?: 0,
                    femaleCount = gender["female"] ?: 0,
                    maleColor = Color(0xFF3498db),    // Color azul para masculino 
                    femaleColor = Color(0xFFFF78A9)   // Color rosa para femenino
                )
            }
              // Obtener distribución por edad
            viewModel.getAgeDistribution().onSuccess { ages ->
                // Usando un solo color para todas las barras: el color principal interactivo #3498db
                val singleColor = Color(0xFF3498db)
                
                ageRangeData = ages.map { age ->
                    AgeRangeData(
                        label = age["label"] as String,
                        count = age["count"] as Int,
                        color = singleColor
                    )
                }
            }
        }
    }
    
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
                userName = "Admin",
            )
            
            Spacer(modifier = Modifier.height(16.dp))
              // Tarjetas de información resumida
            DashboardInfoCardsGrid(
                totalEmployees = employeeStats["total"] ?: 0,
                inactiveEmployees = employeeStats["inactive"] ?: 0,
                deletedEmployees = employeeStats["deleted"] ?: 0,
                newEmployees = employeeStats["newThisMonth"] ?: 0
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Tabla de empleados recientes
            RecentEmployeesTable(recentEmployees)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Distribución por departamento
            DepartmentChart(departmentData)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Distribución por género
            GenderDistribution(genderData)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Distribución por rango de edad
            AgeRangeDistribution(ageRangeData)
            
            // Espacio adicional al final para mejorar el scroll
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Grid de tarjetas de información
 */
@Composable
fun DashboardInfoCardsGrid(
    totalEmployees: Int,
    inactiveEmployees: Int,
    deletedEmployees: Int,
    newEmployees: Int
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        InfoCard(
            icon = Icons.Default.Person,
            title = "Total Empleados",
            value = totalEmployees.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        
        InfoCard(
            icon = ImageVector.vectorResource(id = R.drawable.groups_24px),
            title = "Inactivos",
            value = inactiveEmployees.toString(),
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
            value = deletedEmployees.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )
        
        InfoCard(
            icon = ImageVector.vectorResource(id = R.drawable.person_add_24px),
            title = "Nuevos (Mes)",
            value = newEmployees.toString(),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
    }
}
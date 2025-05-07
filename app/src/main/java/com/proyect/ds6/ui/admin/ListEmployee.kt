package com.proyect.ds6.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyect.ds6.R
import com.proyect.ds6.ui.admin.components.EmployeeCard
import com.proyect.ds6.ui.components.FilterChipsMenu
import com.proyect.ds6.ui.theme.DS6Theme

/**
 * Modelo de datos para un empleado (datos estáticos)
 */
data class Employee(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val cedula: String,
    val departamento: String,
    val cargo: String,
    val activo: Boolean
)

/**
 * Pantalla principal para listar empleados con funcionalidades de búsqueda y filtrado
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEmployeeScreen(
    onNavigateToAddEmployee: () -> Unit = {},
    onNavigateToEmployeeDetail: (Int) -> Unit = {}
) {
    // Estado para manejar la búsqueda
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    
    // Estado para manejar el filtrado
    var selectedStatusFilter by remember { mutableStateOf<String?>(null) }
    var selectedDepartmentFilter by remember { mutableStateOf<String?>(null) }
    var selectedRoleFilter by remember { mutableStateOf<String?>(null) }
    
    // Estado para mostrar el menú de filtros
    var showFilterMenu by remember { mutableStateOf(false) }
    
    // Estado para guardar el empleado seleccionado
    var selectedEmployee by remember { mutableStateOf<Int?>(null) }
    
    // Lista de empleados estática
    val employees = remember {
        mutableStateListOf(
            Employee(1, "Juan Carlos", "Pérez Rodríguez", "9-999-9999", "Recursos Humanos", "Especialista de Reclutamiento", true),
            Employee(2, "María José", "González López", "8-888-8888", "Contabilidad", "Contador Senior", true),
            Employee(3, "Roberto", "Sánchez Díaz", "7-777-7777", "Tecnología", "Desarrollador Frontend", true),
            Employee(4, "Ana María", "Martínez Flores", "6-666-6666", "Ventas", "Gerente de Cuentas", false),
            Employee(5, "Pedro", "Ramírez Castro", "5-555-5555", "Recursos Humanos", "Asistente Administrativo", true),
            Employee(6, "Sofía", "López Herrera", "4-444-4444", "Tecnología", "Ingeniero DevOps", false),
            Employee(7, "Carlos", "García Mendoza", "3-333-3333", "Contabilidad", "Asistente Contable", true),
            Employee(8, "Lucía", "Torres Vargas", "2-222-2222", "Ventas", "Representante de Ventas", true)
        )
    }
    
    // Listas únicas para los filtros
    val departments = remember { employees.map { it.departamento }.distinct() }
    val roles = remember { employees.map { it.cargo }.distinct() }
    
    // Función para filtrar empleados
    val filteredEmployees = employees.filter { employee ->
        // Filtro de búsqueda
        val matchesSearch = if (searchQuery.isNotEmpty()) {
            employee.nombre.contains(searchQuery, ignoreCase = true) ||
            employee.apellido.contains(searchQuery, ignoreCase = true) ||
            employee.cedula.contains(searchQuery, ignoreCase = true) ||
            employee.departamento.contains(searchQuery, ignoreCase = true) ||
            employee.cargo.contains(searchQuery, ignoreCase = true)
        } else true
        
        // Filtros por chips
        val matchesStatus = selectedStatusFilter?.let {
            when (it) {
                "Activos" -> employee.activo
                "Inactivos" -> !employee.activo
                else -> true
            }
        } ?: true
        
        val matchesDepartment = selectedDepartmentFilter?.let {
            employee.departamento == it
        } ?: true
        
        val matchesRole = selectedRoleFilter?.let {
            employee.cargo == it
        } ?: true
        
        matchesSearch && matchesStatus && matchesDepartment && matchesRole
    }
    
    // Componente de menú de filtros
    FilterChipsMenu(
        show = showFilterMenu,
        statusOptions = listOf("Todos", "Activos", "Inactivos"),
        departmentOptions = departments,
        roleOptions = roles,
        initialSelectedStatus = selectedStatusFilter,
        initialSelectedDepartment = selectedDepartmentFilter,
        initialSelectedRole = selectedRoleFilter,
        onApplyFilters = { status, department, role ->
            selectedStatusFilter = status
            selectedDepartmentFilter = department
            selectedRoleFilter = role
        },
        onDismiss = { showFilterMenu = false }
    )
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddEmployee,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar empleado",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Título de la pantalla
            Text(
                text = "Gestión de Empleados",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
            
            // Barra de búsqueda con botón de filtros
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Barra de búsqueda
                DockedSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { isSearchActive = false },
                    active = isSearchActive,
                    onActiveChange = { isSearchActive = it },
                    placeholder = { Text("Buscar empleado") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    // Sugerencias de búsqueda (vacías por ahora)
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Botón de filtros con icono more_vert_24px
                IconButton(
                    onClick = { showFilterMenu = true }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.more_vert_24px),
                        contentDescription = "Mostrar filtros",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Mostrar resumen de filtros activos (si hay alguno)
            if (selectedStatusFilter != null || selectedDepartmentFilter != null || selectedRoleFilter != null) {
                Text(
                    text = "Filtros activos: " + 
                           (selectedStatusFilter ?: "") + 
                           (if (selectedDepartmentFilter != null) " • $selectedDepartmentFilter" else "") +
                           (if (selectedRoleFilter != null) " • $selectedRoleFilter" else ""),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )
            }
            
            // Lista de empleados
            if (filteredEmployees.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron empleados con los filtros actuales",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp) // Espacio para el FAB
                ) {
                    items(filteredEmployees) { employee ->
                        EmployeeCard(
                            nombre = employee.nombre,
                            apellido = employee.apellido,
                            cedula = employee.cedula,
                            departamento = employee.departamento,
                            cargo = employee.cargo,
                            activo = employee.activo,
                            isSelected = selectedEmployee == employee.id,
                            onClick = { selectedEmployee = if (selectedEmployee == employee.id) null else employee.id },
                            onActiveChange = { /* En una app real, aquí actualizaríamos el estado del empleado */ },
                            onView = { onNavigateToEmployeeDetail(employee.id) },
                            onDelete = { /* En una app real, aquí eliminaríamos el empleado */ }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListEmployeeScreenPreview() {
    DS6Theme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ListEmployeeScreen()
        }
    }
}
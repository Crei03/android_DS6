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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
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
import com.proyect.ds6.model.Employee
import com.proyect.ds6.model.Departamento
import com.proyect.ds6.model.Cargo
import com.proyect.ds6.data.repository.EmployeeRepository
import com.proyect.ds6.db.supabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Contenedor de datos UI para un empleado con los campos necesarios para la visualización
 */
data class EmployeeUI(
    val id: String,
    val nombre: String,
    val apellido: String,
    val cedula: String,
    val departamentoCodigo: String?,
    val departamentoNombre: String?,
    val cargoCodigo: String?,
    val cargoNombre: String?,
    val activo: Boolean
)

/**
 * Pantalla principal para listar empleados con funcionalidades de búsqueda y filtrado
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEmployeeScreen(
    onNavigateToAddEmployee: () -> Unit = {},
    onNavigateToEmployeeDetail: (String) -> Unit = {}
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
    var selectedEmployee by remember { mutableStateOf<String?>(null) }    // Estados para manejar la carga de datos
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val employees = remember { mutableStateListOf<EmployeeUI>() }
    
    // Estados para almacenar los datos de departamentos y cargos
    val departamentos = remember { mutableStateListOf<Departamento>() }
    val cargos = remember { mutableStateListOf<Cargo>() }
    
    // Crear el repositorio
    val employeeRepository = remember { EmployeeRepository(supabase) }
    
    // Cargar datos
    LaunchedEffect(key1 = Unit) {
        isLoading = true
        try {
            // Cargar departamentos
            val depResult = withContext(Dispatchers.IO) {
                employeeRepository.getDepartamentos()
            }
            
            if (depResult.isSuccess) {
                departamentos.clear()
                departamentos.addAll(depResult.getOrNull() ?: emptyList())
            }
            
            // Cargar cargos
            val cargoResult = withContext(Dispatchers.IO) {
                employeeRepository.getCargos()
            }
            
            if (cargoResult.isSuccess) {
                cargos.clear()
                cargos.addAll(cargoResult.getOrNull() ?: emptyList())
            }
              // Crear mapas para búsqueda rápida
            val depMap = departamentos.associateBy({ it.codigo }, { it })
            val cargoMap = cargos.associateBy({ it.codigo }, { it })
            
            // Cargar empleados
            val result = withContext(Dispatchers.IO) {
                employeeRepository.getAllEmployees()
            }
            
            if (result.isSuccess) {
                // Convertir los empleados del modelo a la UI
                val employeesUI = result.getOrNull()?.map { employee ->
                    // Buscar nombres en los mapas
                    val departamentoNombre = employee.departamento?.let { 
                        depMap[it]?.nombre ?: it // Si no se encuentra, usar el código
                    }
                    
                    val cargoNombre = employee.cargo?.let { 
                        cargoMap[it]?.nombre ?: it // Si no se encuentra, usar el código
                    }
                    
                    EmployeeUI(
                        id = employee.cedula,
                        nombre = "${employee.nombre1 ?: ""} ${employee.nombre2 ?: ""}".trim(),
                        apellido = "${employee.apellido1 ?: ""} ${employee.apellido2 ?: ""}".trim(),
                        cedula = employee.cedula,
                        departamentoCodigo = employee.departamento,
                        departamentoNombre = departamentoNombre,
                        cargoCodigo = employee.cargo,
                        cargoNombre = cargoNombre,
                        activo = employee.estado == 1
                    )
                } ?: emptyList()
                
                employees.clear()
                employees.addAll(employeesUI)
            } else {
                errorMessage = "Error al cargar los empleados: ${result.exceptionOrNull()?.message}"
            }
        } catch (e: Exception) {
            errorMessage = "Error inesperado: ${e.message}"
        } finally {
            isLoading = false
        }
    }    // Listas únicas para los filtros (extraídas de los datos reales)
    val departmentNames = remember(employees) { 
        employees.mapNotNull { it.departamentoNombre }.distinct() 
    }
    val roleNames = remember(employees) { 
        employees.mapNotNull { it.cargoNombre }.distinct() 
    }
    
    // Función para filtrar empleados
    val filteredEmployees = employees.filter { employee ->
        // Filtro de búsqueda
        val matchesSearch = if (searchQuery.isNotEmpty()) {
            employee.nombre.contains(searchQuery, ignoreCase = true) ||
            employee.apellido.contains(searchQuery, ignoreCase = true) ||
            employee.cedula.contains(searchQuery, ignoreCase = true) ||
            (employee.departamentoNombre?.contains(searchQuery, ignoreCase = true) ?: false) ||
            (employee.cargoNombre?.contains(searchQuery, ignoreCase = true) ?: false)
        } else true
        
        // Filtros por chips
        val matchesStatus = if (selectedStatusFilter?.let {
                when (it) {
                    "Activos" -> employee.activo
                    "Inactivos" -> !employee.activo
                    else -> true
                }
            } ?: true) {
            true
        } else {
            false
        }
        val matchesDepartment = selectedDepartmentFilter?.let {
            employee.departamentoNombre == it
        } ?: true
        
        val matchesRole = selectedRoleFilter?.let {
            employee.cargoNombre == it
        } ?: true
        
        matchesSearch && matchesStatus && matchesDepartment && matchesRole
    }
      // Componente de menú de filtros
    FilterChipsMenu(
        show = showFilterMenu,
        statusOptions = listOf("Todos", "Activos", "Inactivos"),
        departmentOptions = departmentNames,
        roleOptions = roleNames,
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
            if (isLoading) {
                // Mostrar indicador de carga
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Cargando empleados...")
                    }
                }
            } else if (errorMessage != null) {
                // Mostrar mensaje de error
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage ?: "Error desconocido",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else if (filteredEmployees.isEmpty()) {
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
                    items(filteredEmployees) { employee ->                        EmployeeCard(
                            nombre = employee.nombre,
                            apellido = employee.apellido,
                            cedula = employee.cedula,
                            departamento = employee.departamentoNombre ?: "Sin departamento",
                            cargo = employee.cargoNombre ?: "Sin cargo",
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


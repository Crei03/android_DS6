package com.proyect.ds6.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.filled.Face
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.proyect.ds6.R
import com.proyect.ds6.ui.admin.components.EmployeeCard
import com.proyect.ds6.ui.admin.components.DeletedEmployeeCard
import com.proyect.ds6.ui.components.FilterChipsMenu
import com.proyect.ds6.ui.theme.DS6Theme
import com.proyect.ds6.model.Employee
import com.proyect.ds6.model.Departamento
import com.proyect.ds6.model.Cargo
import com.proyect.ds6.data.repository.EmployeeRepository
import com.proyect.ds6.db.supabase
import io.github.jan.supabase.postgrest.postgrest
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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
    // Estas variables ya no se usan para filtrado pero se mantienen para compatibilidad
    var selectedDepartmentFilter by remember { mutableStateOf<String?>(null) }
    var selectedRoleFilter by remember { mutableStateOf<String?>(null) }
    
    // Estado para mostrar el menú de filtros
    var showFilterMenu by remember { mutableStateOf(false) }// Estado para guardar el empleado seleccionado
    var selectedEmployee by remember { mutableStateOf<String?>(null) }    
    
    // Estados para manejar la carga de datos
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val employees = remember { mutableStateListOf<EmployeeUI>() }
    
    // Estado para empleados eliminados
    var showDeletedEmployees by remember { mutableStateOf(false) }
    val deletedEmployees = remember { mutableStateListOf<EmployeeUI>() }
    var isLoadingDeleted by remember { mutableStateOf(false) }
    var deletedErrorMessage by remember { mutableStateOf<String?>(null) }
    var selectedDeletedEmployee by remember { mutableStateOf<String?>(null) }
    
    // Estados para almacenar los datos de departamentos y cargos
    val departamentos = remember { mutableStateListOf<Departamento>() }
    val cargos = remember { mutableStateListOf<Cargo>() }
    
    // Mapas para búsqueda rápida por código
    val departamentosPorCodigo = remember { mutableMapOf<String, Departamento>() }
    val cargosPorCodigo = remember { mutableMapOf<String, Cargo>() }
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
            
            if (depResult.isSuccess) {                departamentos.clear()
                departamentos.addAll(depResult.getOrNull() ?: emptyList())
                
                // Actualizar el mapa de departamentos por código
                departamentosPorCodigo.clear()
                departamentos.forEach { departamento ->
                    departamento.codigo?.let { codigo ->
                        departamentosPorCodigo[codigo] = departamento
                    }
                }
            }
            
            // Cargar cargos
            val cargoResult = withContext(Dispatchers.IO) {
                employeeRepository.getCargos()
            }
            
            if (cargoResult.isSuccess) {                cargos.clear()
                cargos.addAll(cargoResult.getOrNull() ?: emptyList())
                
                // Actualizar el mapa de cargos por código
                cargosPorCodigo.clear()
                cargos.forEach { cargo ->
                    cargo.codigo?.let { codigo ->
                        cargosPorCodigo[codigo] = cargo
                    }
                }
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
    }

    
    // Función para cargar empleados eliminados
    fun loadDeletedEmployees() {
        isLoadingDeleted = true
        deletedErrorMessage = null
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val depMap = departamentos.associateBy({ it.codigo }, { it })
                val cargoMap = cargos.associateBy({ it.codigo }, { it })
                
                val result = employeeRepository.getDeletedEmployees()
                
                if (result.isSuccess) {
                    // Convertir los empleados del modelo a la UI
                    val deletedEmployeesUI = result.getOrNull()?.map { employee ->
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
                            activo = false // Todos los eliminados los marcamos como inactivos
                        )
                    } ?: emptyList()
                    
                    withContext(Dispatchers.Main) {
                        deletedEmployees.clear()
                        deletedEmployees.addAll(deletedEmployeesUI)
                        isLoadingDeleted = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        deletedErrorMessage = "Error al cargar los empleados eliminados: ${result.exceptionOrNull()?.message}"
                        isLoadingDeleted = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    deletedErrorMessage = "Error inesperado: ${e.message}"
                    isLoadingDeleted = false
                }
            }
        }
    }    // Función para cargar empleados con filtro de estado
    fun loadEmployees(statusFilter: String? = null, departmentFilter: String? = null, roleFilter: String? = null) {
        isLoading = true
        errorMessage = null
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Convertir el filtro de estado a Int para la consulta
                val estadoInt: Int? = when(statusFilter) {
                    "Activos" -> 1
                    "Inactivos" -> 0
                    else -> null // "Todos" o null
                }
                  // Usar el término de búsqueda si hay algo en searchQuery
                val searchTermParameter = if (searchQuery.isNotBlank()) searchQuery else null
                
                // Cargar empleados filtrados (sin filtros de departamento ni cargo)
                val result = employeeRepository.getFilteredEmployees(
                    estadoFilter = estadoInt,
                    searchTerm = searchTermParameter
                )
                
                
                if (result.isSuccess) {
                    // Convertir los empleados del modelo a la UI
                    val employeesUI = result.getOrNull()?.map { employee ->
                        // Buscar nombres en los mapas
                        val departamentoNombre = employee.departamento?.let { 
                            departamentosPorCodigo[it]?.nombre ?: it // Si no se encuentra, usar el código
                        }
                        
                        val cargoNombre = employee.cargo?.let { 
                            cargosPorCodigo[it]?.nombre ?: it // Si no se encuentra, usar el código
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
                    
                    withContext(Dispatchers.Main) {
                        employees.clear()
                        employees.addAll(employeesUI)
                        isLoading = false
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        errorMessage = "Error al cargar los empleados: ${result.exceptionOrNull()?.message}"
                        isLoading = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Error inesperado: ${e.message}"
                    isLoading = false
                }
            }
        }
    }

    // Listas únicas para los filtros (extraídas de los datos reales)    // Cargar empleados eliminados cuando se solicita
    LaunchedEffect(showDeletedEmployees) {
        if (showDeletedEmployees && deletedEmployees.isEmpty()) {
            loadDeletedEmployees()
        }
    }

    // Obtener listas de nombres de departamentos y cargos directamente de las listas cargadas
    val departmentNames = remember(departamentos) { 
        departamentos.mapNotNull { it.nombre }.distinct() 
    }
    val roleNames = remember(cargos) { 
        cargos.mapNotNull { it.nombre }.distinct() 
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
        
        // Ya no filtramos por departamento ni cargo a nivel local
        // porque ahora todo el filtrado se hace a nivel de servidor
        
        matchesSearch && matchesStatus
    }// Componente de menú de filtros
    FilterChipsMenu(
        show = showFilterMenu,
        statusOptions = listOf("Todos", "Activos", "Inactivos"),
        initialSelectedStatus = selectedStatusFilter,
        onApplyFilters = { status ->
            // Guardar los filtros seleccionados
            selectedStatusFilter = status
            selectedDepartmentFilter = null
            selectedRoleFilter = null            
            // Recargar los datos con los nuevos filtros
            loadEmployees(status, null, null)
        },
        onDismiss = { showFilterMenu = false }
    )
      Scaffold(
        floatingActionButton = {
            // Mostrar FAB solo en la sección de empleados activos
            if (!showDeletedEmployees) {
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {            // Título de la pantalla
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
            ) {                // Barra de búsqueda                
                DockedSearchBar(
                    query = searchQuery,                    onQueryChange = {
                        searchQuery = it
                        // Recargar si la búsqueda está vacía o tiene más de 3 caracteres
                        if (!showDeletedEmployees && (it.isEmpty() || it.length >= 3)) {
                            loadEmployees(selectedStatusFilter, null, null)
                        }
                    },
                    onSearch = {
                        isSearchActive = false
                        // Si no estamos en la vista de eliminados, recargar con la búsqueda
                        if (!showDeletedEmployees) {
                            loadEmployees(selectedStatusFilter, null, null)
                        }
                    },
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
            
            // Mostrar chips de filtros activos (si hay alguno)
            if (selectedStatusFilter != null) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Chip para el filtro de estado
                    SuggestionChip(
                        onClick = {
                            selectedStatusFilter = null
                            loadEmployees(null, null, null)
                        },
                        label = { Text(selectedStatusFilter!!) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Eliminar filtro de estado",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                            )
                        },
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            borderWidth = 1.dp
                        ),
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            iconContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
            
            // Pestañas para alternar entre empleados activos y eliminados
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {                // Pestaña Empleados Activos
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .clickable {                            if (showDeletedEmployees) {
                                showDeletedEmployees = false
                                // Recargar la lista de empleados activos con los filtros actuales
                                loadEmployees(selectedStatusFilter, null, null)
                            }
                        },
                    color = if (!showDeletedEmployees) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                ){
                    Text(
                        text = "Empleados Activos",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (!showDeletedEmployees) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                  // Pestaña Empleados Eliminados
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .clickable { 
                            if (!showDeletedEmployees) {
                                showDeletedEmployees = true
                                // Recargar la lista de empleados eliminados
                                loadDeletedEmployees()
                            }
                        },
                    color = if (showDeletedEmployees) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                ){
                    Text(
                        text = "Empleados Eliminados",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (showDeletedEmployees) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
              // Lista de empleados
            if (!showDeletedEmployees) {
                // Sección de empleados activos
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
                        items(filteredEmployees) { employee ->                        
                            EmployeeCard(
                                nombre = employee.nombre,
                                apellido = employee.apellido,
                                cedula = employee.cedula,
                                departamento = employee.departamentoNombre ?: "Sin departamento",
                                cargo = employee.cargoNombre ?: "Sin cargo",
                                activo = employee.activo,
                                isSelected = selectedEmployee == employee.id,
                                onClick = { selectedEmployee = if (selectedEmployee == employee.id) null else employee.id },
                                onActiveChange = { isActive -> 
                                    // Convertir el booleano a Int (1 para activo, 0 para inactivo)
                                    val nuevoEstado = if (isActive) 1 else 0
                                    
                                    // Actualizar el estado del empleado en la base de datos
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            val result = employeeRepository.updateEmployeeStatus(employee.id, nuevoEstado)
                                            if (result.isSuccess) {
                                                // Recargar los datos para asegurar que reflejen el estado actual
                                                withContext(Dispatchers.Main) {
                                                    // Recargar con los filtros actuales para mantener la coherencia
                                                    loadEmployees(selectedStatusFilter, null, null)
                                                }
                                            }else {
                                                // Mostrar un mensaje de error (en una app real se mostraría un Toast o Snackbar)
                                                println("Error al actualizar el estado del empleado: ${result.exceptionOrNull()?.message}")
                                            }
                                        } catch (e: Exception) {
                                            // Manejar el error
                                            println("Error inesperado: ${e.message}")
                                        }
                                    }
                                },                            
                                onView = { onNavigateToEmployeeDetail(employee.id) },
                                onDelete = { 
                                    // Eliminar el empleado usando el repositorio
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            val result = employeeRepository.deleteEmployee(employee.id)

                                            if (result.isSuccess) {                                                // Refrescar la lista después de eliminar
                                                withContext(Dispatchers.Main) {
                                                    // Eliminar el empleado de la lista local
                                                    employees.removeIf { it.id == employee.id }
                                                    
                                                    // Recargar empleados eliminados también para mostrar el recién eliminado
                                                    loadDeletedEmployees()
                                                }
                                            } else {
                                                // Manejar el error
                                                println("Error al eliminar empleado: ${result.exceptionOrNull()?.message}")
                                            }
                                        } catch (e: Exception) {
                                            // Manejar el error (en una app real mostraríamos un mensaje)
                                            println("Error inesperado: ${e.message}")
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                // Sección de empleados eliminados
                if (isLoadingDeleted) {
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
                            Text(text = "Cargando empleados eliminados...")
                        }
                    }
                } else if (deletedErrorMessage != null) {
                    // Mostrar mensaje de error
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = deletedErrorMessage ?: "Error desconocido",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                } else if (deletedEmployees.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay empleados eliminados",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp) // Espacio para el FAB
                    ) {
                        items(deletedEmployees) { employee ->
                            DeletedEmployeeCard(
                                nombre = employee.nombre,
                                apellido = employee.apellido,
                                cedula = employee.cedula,
                                departamento = employee.departamentoNombre ?: "Sin departamento",
                                cargo = employee.cargoNombre ?: "Sin cargo",
                                isSelected = selectedDeletedEmployee == employee.id,
                                onClick = { selectedDeletedEmployee = if (selectedDeletedEmployee == employee.id) null else employee.id },
                                onView = { onNavigateToEmployeeDetail(employee.id) },
                                onRestore = {
                                    // Restaurar el empleado usando el repositorio
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            val result = employeeRepository.reinstateEmployee(employee.id)

                                            if (result.isSuccess) {
                                                // Refrescar la lista después de restaurar
                                                withContext(Dispatchers.Main) {                                                    // Eliminar el empleado de la lista de eliminados
                                                    deletedEmployees.removeIf { it.id == employee.id }
                                                      // Recargar las listas para reflejar los cambios
                                                    loadDeletedEmployees()
                                                    loadEmployees(selectedStatusFilter, null, null)
                                                }
                                            } else {
                                                // Manejar el error
                                                println("Error al restaurar empleado: ${result.exceptionOrNull()?.message}")
                                            }
                                        } catch (e: Exception) {
                                            // Manejar el error (en una app real mostraríamos un mensaje)
                                            println("Error inesperado: ${e.message}")
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }    }
}


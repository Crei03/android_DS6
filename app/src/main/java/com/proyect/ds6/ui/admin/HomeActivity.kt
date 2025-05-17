package com.proyect.ds6.ui.admin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyect.ds6.data.repository.EmployeeRepository
import com.proyect.ds6.db.supabase
import com.proyect.ds6.ui.theme.DS6Theme
import com.proyect.ds6.ui.admin.components.BottomNavigationBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DS6Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}
@Composable
fun HomeScreen() {
    // Estado para controlar la pestaña seleccionada
    var selectedTab by remember { mutableIntStateOf(0) }
    
    // Estado para recordar la pestaña anterior cuando se navega a una subpantalla
    var previousTab by remember { mutableIntStateOf(0) }
    
    // Estado para controlar si estamos en una subpantalla
    var inSubscreen by remember { mutableStateOf(false) }
    
    // Estado para el ID de la opción seleccionada
    var selectedOption by remember { mutableStateOf("") }
    
    // Estado para almacenar la cédula del empleado seleccionado
    var selectedEmployeeCedula by remember { mutableStateOf("") }
    
    // Contexto para mostrar los Toast
    val context = androidx.compose.ui.platform.LocalContext.current
    
    Scaffold(
        bottomBar = {
            // Si no estamos en una subpantalla, mostrar la barra de navegación
            if (!inSubscreen) {
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { newTab -> selectedTab = newTab }
                )
            }
        }
    ) { paddingValues ->
        // Contenido principal según la pestaña seleccionada
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (inSubscreen) {
                // Aquí se mostraría el contenido de la subpantalla según la opción seleccionada
                when (selectedOption) {
                    "add_employee" -> AddEmployee(
                        onBackClick = {
                            // Volver a la pestaña anterior
                            selectedTab = previousTab
                            // Desactivar el modo subpantalla
                            inSubscreen = false
                        }
                    )                    
                    "add_admin" -> AddUserScreen(
                        onBackClick = {
                            // Volver a la pestaña anterior
                            selectedTab = previousTab
                            // Desactivar el modo subpantalla
                            inSubscreen = false
                        }
                    )
                    "add_department" -> AddDepartamentScreen(
                        onBackClick = {
                            // Volver a la pestaña anterior
                            selectedTab = previousTab
                            // Desactivar el modo subpantalla
                            inSubscreen = false
                        },
                        onSaveUser = { codigo, nombre ->
                            // Guardar el nuevo departamento
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val repository = EmployeeRepository(supabase)
                                    val result = repository.insertDepartamento(codigo, nombre)
                                      withContext(Dispatchers.Main) {
                                        if (result.isSuccess) {
                                            Toast.makeText(context, "Departamento guardado correctamente", Toast.LENGTH_SHORT).show()
                                            // Volver a la pestaña anterior
                                            selectedTab = previousTab
                                            // Desactivar el modo subpantalla
                                            inSubscreen = false
                                        } else {
                                            Toast.makeText(context, "Error al guardar el departamento: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                } catch (e: Exception) {                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                    )                    "add_position" -> AddWorkScreen(
                        onBackClick = {
                            // Volver a la pestaña anterior
                            selectedTab = previousTab
                            // Desactivar el modo subpantalla
                            inSubscreen = false
                        },
                        onSaveUser = { codigo, depCodigo, nombre ->
                            // Guardar el nuevo cargo
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val repository = EmployeeRepository(supabase)
                                    val result = repository.insertCargo(codigo, depCodigo, nombre)
                                    
                                    withContext(Dispatchers.Main) {
                                        if (result.isSuccess) {
                                            Toast.makeText(context, "Cargo guardado correctamente", Toast.LENGTH_SHORT).show()
                                            // Volver a la pestaña anterior
                                            selectedTab = previousTab
                                            // Desactivar el modo subpantalla
                                            inSubscreen = false
                                        } else {
                                            Toast.makeText(context, "Error al guardar el cargo: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(context, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                    )
                    "employee_detail" -> DetailsEmployeeScreen(
                        cedula = selectedEmployeeCedula,
                        onNavigateBack = {
                            // Volver a la pestaña anterior
                            selectedTab = previousTab
                            // Desactivar el modo subpantalla
                            inSubscreen = false
                        }
                    )
                    else -> Text(
                        "Opción no reconocida",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {                // Contenido normal basado en la pestaña seleccionada
                when (selectedTab) {
                    0 -> AdminDashboard()  // Mostramos el Dashboard en la pestaña de Inicio
                    1 -> ListEmployeeScreen(
                        onNavigateToAddEmployee = {
                            // Redirigir a la pantalla de añadir empleados
                            previousTab = selectedTab
                            inSubscreen = true
                            selectedOption = "add_employee"
                        },
                        onNavigateToEmployeeDetail = { cedula ->
                            // Aquí implementamos la navegación a la pantalla de detalles
                            previousTab = selectedTab
                            inSubscreen = true
                            selectedOption = "employee_detail"
                            // Guardamos la cédula del empleado seleccionado
                            selectedEmployeeCedula = cedula
                        }
                    )
                    2 -> AddOptionsScreen(
                        onOptionSelected = { optionId ->
                            // Guardamos la pestaña actual para volver a ella después
                            previousTab = selectedTab
                            // Activamos el modo subpantalla
                            inSubscreen = true
                            // Guardamos la opción seleccionada
                            selectedOption = optionId
                        },
                        onBackClick = {
                            // No se necesita esta función aquí, pero la incluimos por completitud
                        },
                        selectedTabIndex = selectedTab,                        onTabSelected = { newTab -> selectedTab = newTab }
                    )
                    3 -> ProfileContent()
                    else -> AdminDashboard() // Por defecto mostrar Dashboard
                }
            }
            
        }
    }
}

// Contenidos temporales para cada pestaña (excepto Dashboard que está implementado en dashboard.kt)

@Composable
fun EmployeesContent() {
    // Contenido temporal para la pestaña de empleados
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Lista de Empleados")
    }
}

@Composable
fun ProfileContent() {
    // Contenido temporal para la pestaña de perfil
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Perfil de Administrador")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DS6Theme {
        HomeScreen()
    }
}
package com.proyect.ds6.ui.admin

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.proyect.ds6.data.repository.EmployeeRepository
import com.proyect.ds6.db.supabase
import com.proyect.ds6.model.*
import com.proyect.ds6.ui.employee.components.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsEmployeeScreen(
    cedula: String,
    onNavigateBack: () -> Unit = {}
) {
    // Estado para controlar qué sección está expandida
    var expandedSection by remember { mutableStateOf<String?>(null) }
    
    // Estados para controlar el proceso de carga y errores
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // Estado para almacenar los datos del empleado
    var employee by remember { mutableStateOf<Employee?>(null) }
    
    // Estados para las listas de opciones
    var nacionalidades by remember { mutableStateOf<List<Nacionalidad>>(emptyList()) }
    var provincias by remember { mutableStateOf<List<Provincia>>(emptyList()) }
    var distritos by remember { mutableStateOf<List<Distrito>>(emptyList()) }
    var corregimientos by remember { mutableStateOf<List<Corregimiento>>(emptyList()) }
    var departamentos by remember { mutableStateOf<List<Departamento>>(emptyList()) }
    var cargos by remember { mutableStateOf<List<Cargo>>(emptyList()) }
    
    // Estados para los items seleccionados
    var selectedNacionalidad by remember { mutableStateOf<Nacionalidad?>(null) }
    var selectedProvincia by remember { mutableStateOf<Provincia?>(null) }
    var selectedDistrito by remember { mutableStateOf<Distrito?>(null) }
    var selectedCorregimiento by remember { mutableStateOf<Corregimiento?>(null) }
    var selectedDepartamento by remember { mutableStateOf<Departamento?>(null) }
    var selectedCargo by remember { mutableStateOf<Cargo?>(null) }
    
    // Instancia del repositorio
    val employeeRepository = remember { EmployeeRepository(supabase) }
    
    // Corrutina para cargar los datos
    val coroutineScope = rememberCoroutineScope()
    
    // Estado para controlar si hay cambios pendientes
    var hasChanges by remember { mutableStateOf(false) }
    
    // Estados para cada campo del formulario (información personal)
    var primerNombre by remember { mutableStateOf("") }
    var segundoNombre by remember { mutableStateOf("") }
    var primerApellido by remember { mutableStateOf("") }
    var segundoApellido by remember { mutableStateOf("") }
    var apellidoCasado by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") } // Usaremos String para el componente
    var estadoCivil by remember { mutableStateOf("") } // Usaremos String para el componente
    var fechaNacimiento by remember { mutableStateOf(System.currentTimeMillis()) } // Timestamp para DatePicker
    var tipoSangre by remember { mutableStateOf("") }
    
    // Estados para información de contacto
    var celular by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    // Estados para información de dirección
    var calle by remember { mutableStateOf("") }
    var casa by remember { mutableStateOf("") }
    var comunidad by remember { mutableStateOf("") }
    
    // Estados para información laboral
    var fechaContratacion by remember { mutableStateOf(System.currentTimeMillis()) } // Timestamp para DatePicker
    var estado by remember { mutableStateOf(0) } // Int para WorkInfoComponent
    
    // Función para convertir String de fecha a Long para DatePicker
    fun parseDate(dateString: String?): Long {
        if (dateString.isNullOrEmpty()) return System.currentTimeMillis()
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            format.parse(dateString)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
    
    // Función para convertir Long de DatePicker a String para la BD
    fun formatDate(timestamp: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(Date(timestamp))
    }
    
    // Cargar datos del empleado y listas de opciones
    LaunchedEffect(cedula) {
        isLoading = true
        try {
            // Cargar listas de opciones en paralelo
            val nacionalidadesDeferred = coroutineScope.launch { 
                val result = employeeRepository.getNacionalidades()
                if (result.isSuccess) {
                    nacionalidades = result.getOrNull() ?: emptyList()
                }
            }
            
            val provinciasDeferred = coroutineScope.launch {
                val result = employeeRepository.getProvincias()
                if (result.isSuccess) {
                    provincias = result.getOrNull() ?: emptyList()
                }
            }
            
            val distritosDeferred = coroutineScope.launch {
                val result = employeeRepository.getDistritos()
                if (result.isSuccess) {
                    distritos = result.getOrNull() ?: emptyList()
                }
            }
            
            val corregimientosDeferred = coroutineScope.launch {
                val result = employeeRepository.getCorregimientos()
                if (result.isSuccess) {
                    corregimientos = result.getOrNull() ?: emptyList()
                }
            }
            
            val departamentosDeferred = coroutineScope.launch {
                val result = employeeRepository.getDepartamentos()
                if (result.isSuccess) {
                    departamentos = result.getOrNull() ?: emptyList()
                }
            }
            
            val cargosDeferred = coroutineScope.launch {
                val result = employeeRepository.getCargos()
                if (result.isSuccess) {
                    cargos = result.getOrNull() ?: emptyList()
                }
            }
            
            // Cargar datos del empleado
            val employeeResult = withContext(Dispatchers.IO) {
                employeeRepository.getAllEmployees()
            }
            
            if (employeeResult.isSuccess) {
                val employees = employeeResult.getOrNull() ?: emptyList()
                val foundEmployee = employees.find { it.cedula == cedula }
                
                if (foundEmployee != null) {
                    employee = foundEmployee
                    
                    // Inicializar los estados con los datos del empleado
                    primerNombre = foundEmployee.nombre1 ?: ""
                    segundoNombre = foundEmployee.nombre2 ?: ""
                    primerApellido = foundEmployee.apellido1 ?: ""
                    segundoApellido = foundEmployee.apellido2 ?: ""
                    apellidoCasado = foundEmployee.apellidoc ?: ""
                    genero = foundEmployee.genero?.toString() ?: ""
                    estadoCivil = foundEmployee.estadoCivil?.toString() ?: ""
                    fechaNacimiento = parseDate(foundEmployee.fechaNacimiento)
                    tipoSangre = foundEmployee.tipoSangre ?: ""
                    
                    celular = foundEmployee.celular?.toString() ?: ""
                    telefono = foundEmployee.telefono?.toString() ?: ""
                    email = foundEmployee.correo ?: ""
                    password = foundEmployee.contrasena ?: ""
                    
                    calle = foundEmployee.calle ?: ""
                    casa = foundEmployee.casa ?: ""
                    comunidad = foundEmployee.comunidad ?: ""
                    
                    fechaContratacion = parseDate(foundEmployee.fechaContrato)
                    estado = foundEmployee.estado ?: 0
                    
                    // Esperar a que se carguen las listas de opciones
                    nacionalidadesDeferred.join()
                    provinciasDeferred.join()
                    distritosDeferred.join()
                    corregimientosDeferred.join()
                    departamentosDeferred.join()
                    cargosDeferred.join()
                    
                    // Seleccionar los items correspondientes en las listas
                    selectedNacionalidad = nacionalidades.find { it.pais == foundEmployee.nacionalidad }
                    selectedProvincia = provincias.find { it.codigo_provincia == foundEmployee.provincia }
                    selectedDistrito = distritos.find { it.codigo == foundEmployee.distrito }
                    selectedCorregimiento = corregimientos.find { it.codigo == foundEmployee.corregimiento }
                    selectedDepartamento = departamentos.find { it.codigo == foundEmployee.departamento }
                    selectedCargo = cargos.find { it.codigo == foundEmployee.cargo }
                    
                } else {
                    errorMessage = "No se encontró el empleado con cédula $cedula"
                }
            } else {
                errorMessage = "Error al cargar los datos del empleado: ${employeeResult.exceptionOrNull()?.message}"
            }
        } catch (e: Exception) {
            errorMessage = "Error inesperado: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalles del Empleado",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                employee?.let { emp ->
                    // Cabecera con información básica del empleado
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Empleado: ${emp.nombre1 ?: ""} ${emp.apellido1 ?: ""}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Cédula: ${emp.cedula}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    
                    // Botón para información personal
                    Button(
                        onClick = { 
                            expandedSection = if (expandedSection == "personal") null else "personal"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Información Personal")
                    }
                    
                    // Componente de información personal expandible
                    if (expandedSection == "personal") {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            PersonalInfoComponent(
                                cedula = emp.cedula,
                                onCedulaChange = { /* La cédula no se puede modificar */ },
                                primerNombre = primerNombre,
                                onPrimerNombreChange = { primerNombre = it; hasChanges = true },
                                segundoNombre = segundoNombre,
                                onSegundoNombreChange = { segundoNombre = it; hasChanges = true },
                                primerApellido = primerApellido,
                                onPrimerApellidoChange = { primerApellido = it; hasChanges = true },
                                segundoApellido = segundoApellido,
                                onSegundoApellidoChange = { segundoApellido = it; hasChanges = true },
                                fechaNacimiento = fechaNacimiento,
                                onFechaNacimientoChange = { fechaNacimiento = it; hasChanges = true },
                                genero = genero,
                                onGeneroChange = { genero = it; hasChanges = true },
                                estadoCivil = estadoCivil,
                                onEstadoCivilChange = { estadoCivil = it; hasChanges = true },
                                tipoSangre = tipoSangre,
                                onTipoSangreChange = { tipoSangre = it; hasChanges = true },
                                nationalities = nacionalidades,
                                selectedNacionalidad = selectedNacionalidad,
                                onNacionalidadSelected = { 
                                    selectedNacionalidad = it
                                    hasChanges = true 
                                }
                            )
                        }
                    }
                    
                    // Botón para información de contacto
                    Button(
                        onClick = { 
                            expandedSection = if (expandedSection == "contacto") null else "contacto"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Información de Contacto")
                    }
                    
                    // Componente de información de contacto expandible
                    if (expandedSection == "contacto") {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ContactInfoComponent(
                                celular = celular,
                                onCelularChange = { celular = it; hasChanges = true },
                                telefono = telefono,
                                onTelefonoChange = { telefono = it; hasChanges = true },
                                email = email,
                                onEmailChange = { email = it; hasChanges = true },
                                password = password,
                                onPasswordChange = { password = it; hasChanges = true }
                            )
                        }
                    }
                    
                    // Botón para información de dirección
                    Button(
                        onClick = { 
                            expandedSection = if (expandedSection == "direccion") null else "direccion"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Dirección")
                    }
                    
                    // Componente de dirección expandible
                    if (expandedSection == "direccion") {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AddressInfoComponent(
                                provinces = provincias,
                                selectedProvincia = selectedProvincia,
                                onProvinciaSelected = { 
                                    selectedProvincia = it
                                    // Resetear distrito y corregimiento cuando cambia la provincia
                                    selectedDistrito = null
                                    selectedCorregimiento = null
                                    hasChanges = true 
                                },
                                distritos = distritos.filter { 
                                    selectedProvincia?.codigo_provincia == it.codigo_provincia 
                                },
                                selectedDistrito = selectedDistrito,
                                onDistritoSelected = { 
                                    selectedDistrito = it
                                    // Resetear corregimiento cuando cambia el distrito
                                    selectedCorregimiento = null
                                    hasChanges = true 
                                },
                                corregimientos = corregimientos.filter { 
                                    selectedDistrito?.codigo == it.codigo_distrito &&
                                    selectedProvincia?.codigo_provincia == it.codigo_provincia
                                },
                                selectedCorregimiento = selectedCorregimiento,
                                onCorregimientoSelected = { 
                                    selectedCorregimiento = it
                                    hasChanges = true 
                                },
                                calle = calle,
                                onCalleChange = { calle = it; hasChanges = true },
                                casa = casa,
                                onCasaChange = { casa = it; hasChanges = true },
                                comunidad = comunidad,
                                onComunidadChange = { comunidad = it; hasChanges = true }
                            )
                        }
                    }
                    
                    // Botón para información laboral
                    Button(
                        onClick = { 
                            expandedSection = if (expandedSection == "laboral") null else "laboral"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Información Laboral")
                    }
                    
                    // Componente de información laboral expandible
                    if (expandedSection == "laboral") {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            WorkInfoComponent(
                                departamentos = departamentos,
                                selectedDepartamento = selectedDepartamento,
                                onDepartamentoSelected = { 
                                    selectedDepartamento = it
                                    // Resetear cargo cuando cambia el departamento
                                    if (selectedCargo?.dep_codigo != it?.codigo) {
                                        selectedCargo = null
                                    }
                                    hasChanges = true 
                                },
                                cargos = cargos.filter { 
                                    selectedDepartamento?.codigo == it.dep_codigo || it.dep_codigo == null
                                },
                                selectedCargo = selectedCargo,
                                onCargoSelected = { 
                                    selectedCargo = it
                                    hasChanges = true 
                                },
                                fechaContratacion = fechaContratacion,
                                onFechaContratacionChange = { fechaContratacion = it; hasChanges = true },
                                estado = estado,
                                onEstadoChange = { estado = it; hasChanges = true }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Botones de acción (Guardar y Cancelar)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Botón Guardar
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        // Convertir valores String a Int donde corresponda
                                        val generoInt = genero.toIntOrNull()
                                        val estadoCivilInt = estadoCivil.toIntOrNull()
                                        val celularInt = celular.toIntOrNull()
                                        val telefonoInt = telefono.toIntOrNull()
                                        
                                        // Crear el objeto Employee actualizado
                                        val updatedEmployee = Employee(
                                            cedula = emp.cedula,
                                            prefijo = emp.prefijo,
                                            tomo = emp.tomo,
                                            asiento = emp.asiento,
                                            nombre1 = primerNombre.takeIf { it.isNotEmpty() },
                                            nombre2 = segundoNombre.takeIf { it.isNotEmpty() },
                                            apellido1 = primerApellido.takeIf { it.isNotEmpty() },
                                            apellido2 = segundoApellido.takeIf { it.isNotEmpty() },
                                            apellidoc = apellidoCasado.takeIf { it.isNotEmpty() },
                                            genero = generoInt,
                                            estadoCivil = estadoCivilInt,
                                            tipoSangre = tipoSangre.takeIf { it.isNotEmpty() },
                                            usaAc = emp.usaAc, // Mantener valor original
                                            fechaNacimiento = formatDate(fechaNacimiento).takeIf { it.isNotEmpty() },
                                            celular = celularInt,
                                            telefono = telefonoInt,
                                            correo = email.takeIf { it.isNotEmpty() },
                                            contrasena = password.takeIf { it.isNotEmpty() },
                                            provincia = selectedProvincia?.codigo_provincia,
                                            distrito = selectedDistrito?.codigo,
                                            corregimiento = selectedCorregimiento?.codigo,
                                            calle = calle.takeIf { it.isNotEmpty() },
                                            casa = casa.takeIf { it.isNotEmpty() },
                                            comunidad = comunidad.takeIf { it.isNotEmpty() },
                                            nacionalidad = selectedNacionalidad?.pais,
                                            fechaContrato = formatDate(fechaContratacion).takeIf { it.isNotEmpty() },
                                            cargo = selectedCargo?.codigo,
                                            departamento = selectedDepartamento?.codigo,
                                            estado = estado
                                        )
                                        
                                        // Actualizar el empleado en la base de datos
                                        // Note: Esta es una versión simplificada, dependiendo de la API real
                                        // del repositorio podrías necesitar un método específico para actualizar
                                        val result = withContext(Dispatchers.IO) {
                                            // Supongamos que tienes un método para actualizar
                                            // employeeRepository.updateEmployee(updatedEmployee)
                                            
                                            // De momento solo mostraremos un log
                                            Log.d("DetailsEmployee", "Actualizando empleado: ${updatedEmployee}")
                                            Result.success(Unit)
                                        }
                                        
                                        if (result.isSuccess) {
                                            hasChanges = false
                                            onNavigateBack() // Volver a la pantalla anterior
                                        } else {
                                            // Mostrar error
                                            errorMessage = "Error al actualizar el empleado: ${result.exceptionOrNull()?.message}"
                                        }
                                    } catch (e: Exception) {
                                        // Manejar errores
                                        errorMessage = "Error inesperado: ${e.message}"
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = hasChanges
                        ) {
                            Text("Guardar")
                        }
                        
                        // Botón Cancelar
                        OutlinedButton(
                            onClick = {
                                if (hasChanges) {
                                    // Aquí podrías mostrar un diálogo de confirmación
                                    // Por simplicidad, solo volvemos a la pantalla anterior
                                }
                                onNavigateBack()
                            },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Cancelar")
                        }
                    }
                }
            }
        }
    }
}

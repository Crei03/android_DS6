package com.proyect.ds6.ui.admin

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.proyect.ds6.data.repository.EmployeeRepository
import com.proyect.ds6.db.supabase
import com.proyect.ds6.model.* // Asegúrate de que Employee, Nacionalidad, Provincia, Distrito, Corregimiento, Departamento, Cargo estén aquí
import com.proyect.ds6.ui.admin.components.InfoCard
import com.proyect.ds6.ui.employee.components.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

import android.util.Patterns // Import for email validation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsEmployeeScreen(
    cedula: String, // Cédula del empleado a mostrar/editar
    onNavigateBack: () -> Unit = {}
) {
    var expandedSection by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) } // General error message state
    var employee by remember { mutableStateOf<Employee?>(null) } // Empleado original cargado

    // Estados para las listas de opciones (se cargan una vez)
    var nacionalidades by remember { mutableStateOf<List<Nacionalidad>>(emptyList()) }
    var provincias by remember { mutableStateOf<List<Provincia>>(emptyList()) }
    // Guardamos las listas completas para poder filtrar reactivamente
    var allDistritos by remember { mutableStateOf<List<Distrito>>(emptyList()) }
    var allCorregimientos by remember { mutableStateOf<List<Corregimiento>>(emptyList()) }
    var departamentos by remember { mutableStateOf<List<Departamento>>(emptyList()) }
    var allCargos by remember { mutableStateOf<List<Cargo>>(emptyList()) } // Guardamos la lista completa de cargos


    // Estados para los valores del formulario (editables)
    var primerNombre by remember { mutableStateOf("") }
    var segundoNombre by remember { mutableStateOf("") }
    var primerApellido by remember { mutableStateOf("") }
    var segundoApellido by remember { mutableStateOf("") }
    var apellidoCasado by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") } // UI usa String (Masculino, Femenino, etc.)
    var estadoCivil by remember { mutableStateOf("") } // UI usa String (Soltero/a, Casado/a, etc.)
    var fechaNacimiento by remember { mutableStateOf(0L) } // UI usa Long timestamp
    var tipoSangre by remember { mutableStateOf("") }
    var usaAc by remember { mutableStateOf(0) } // UI usa Int: 0=No, 1=Sí

    var celular by remember { mutableStateOf("") } // UI usa String (puede tener guiones)
    var telefono by remember { mutableStateOf("") } // UI usa String (puede tener guiones)
    var email by remember { mutableStateOf("") }
    // REMOVED: var password by remember { mutableStateOf("") } // **SECURITY WARNING: Handle passwords securely!**

    var selectedNacionalidad by remember { mutableStateOf<Nacionalidad?>(null) }
    var selectedProvincia by remember { mutableStateOf<Provincia?>(null) }
    var selectedDistrito by remember { mutableStateOf<Distrito?>(null) } // Objeto Distrito seleccionado
    var selectedCorregimiento by remember { mutableStateOf<Corregimiento?>(null) } // Objeto Corregimiento seleccionado
    var calle by remember { mutableStateOf("") }
    var casa by remember { mutableStateOf("") } // Corrected state type
    var comunidad by remember { mutableStateOf("") }

    var selectedDepartamento by remember { mutableStateOf<Departamento?>(null) }
    var selectedCargo by remember { mutableStateOf<Cargo?>(null) }
    var fechaContratacion by remember { mutableStateOf(0L) } // UI usa Long timestamp
    var estado by remember { mutableStateOf(1) } // UI usa Int (1=Activo, 0=Inactivo, etc.)

    // Estados para mensajes de error de validación por campo
    var primerNombreError by remember { mutableStateOf<String?>(null) }
    var segundoNombreError by remember { mutableStateOf<String?>(null) }
    var primerApellidoError by remember { mutableStateOf<String?>(null) }
    var segundoApellidoError by remember { mutableStateOf<String?>(null) }
    var apellidoCasadoError by remember { mutableStateOf<String?>(null) }
    var tipoSangreError by remember { mutableStateOf<String?>(null) }
    var celularError by remember { mutableStateOf<String?>(null) }
    var telefonoError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var calleError by remember { mutableStateOf<String?>(null) }
    var casaError by remember { mutableStateOf<String?>(null) }
    var comunidadError by remember { mutableStateOf<String?>(null) }
    var departamentoError by remember { mutableStateOf<String?>(null) } // Added error state for departamento
    var cargoError by remember { mutableStateOf<String?>(null) } // Added error state for cargo
    var nacionalidadError by remember { mutableStateOf<String?>(null) } // Added error state for Nacionalidad
    var provinciaError by remember { mutableStateOf<String?>(null) } // Added error state for Provincia
    var distritoError by remember { mutableStateOf<String?>(null) } // Added error state for Distrito
    var corregimientoError by remember { mutableStateOf<String?>(null) } // Added error state for Corregimiento
    var fechaNacimientoError by remember { mutableStateOf<String?>(null) } // Added error state for Fecha Nacimiento
    var generoError by remember { mutableStateOf<String?>(null) } // Added error state for Genero
    var estadoCivilError by remember { mutableStateOf<String?>(null) } // Added error state for Estado Civil


    val employeeRepository = remember { EmployeeRepository(supabase) }
    val coroutineScope = rememberCoroutineScope()
    var hasChanges by remember { mutableStateOf(false) } // Estado para habilitar el botón Guardar

    // Funciones de utilidad para fechas (pueden ser movidas a un archivo común)
    fun parseDate(dateString: String?): Long {
        if (dateString.isNullOrEmpty()) return 0L // Usar 0L para indicar que no hay fecha seleccionada
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            // Parsear y obtener el timestamp en milisegundos
            format.parse(dateString)?.time ?: 0L
        } catch (e: Exception) {
            Log.e("DetailsEmployee", "Error parsing date string: $dateString", e)
            0L // Retornar 0L en caso de error
        }
    }

    fun formatDate(timestamp: Long): String? {
        if (timestamp == 0L) return null // Retornar null si el timestamp es 0L
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            format.format(Date(timestamp))
        } catch (e: Exception) {
            Log.e("DetailsEmployee", "Error formatting timestamp: $timestamp", e)
            null // Retornar null en caso de error
        }
    }

    // Mapeo de códigos Int a nombres String para Género
    fun mapGeneroCodeToName(code: Int?): String {
        return when (code) {
            0 -> "Masculino"
            1 -> "Femenino"
            else -> "" // Valor por defecto si es null o desconocido
        }
    }

    // Mapeo de códigos Int a nombres String para Estado Civil
    fun mapEstadoCivilCodeToName(code: Int?): String {
        return when (code) {
            0 -> "Soltero/a"
            1 -> "Casado/a"
            2 -> "Divorciado/a"
            3 -> "Viudo/a"
            else -> "" // Valor por defecto si es null o desconocido
        }
    }

    // Mapeo de nombres String a códigos Int para Género (para guardar)
    fun mapGeneroNameToCode(name: String): Int? {
        return when (name) {
            "Masculino" -> 0
            "Femenino" -> 1
            else -> null // Si el string no coincide, guardar como null
        }
    }

    // Mapeo de nombres String a códigos Int para Estado Civil (para guardar)
    fun mapEstadoCivilNameToCode(name: String): Int? {
        return when (name) {
            "Soltero/a" -> 0
            "Casado/a" -> 1
            "Divorciado/a" -> 2
            "Viudo/a" -> 3
            else -> null // Si el string no coincide, guardar como null
        }
    }


    // LaunchedEffect para cargar los datos del empleado y las listas de opciones al iniciar
    LaunchedEffect(cedula) {
        isLoading = true
        errorMessage = null // Limpiar error previo

        try {
            // Cargar todas las listas de opciones en paralelo
            val optionsResults = withContext(Dispatchers.IO) {
                listOf(
                    async { employeeRepository.getNacionalidades() },
                    async { employeeRepository.getProvincias() },
                    async { employeeRepository.getDistritos() }, // Cargar lista completa
                    async { employeeRepository.getCorregimientos() }, // Cargar lista completa
                    async { employeeRepository.getDepartamentos() },
                    async { employeeRepository.getCargos() } // Cargar lista completa
                )
            }

            // Procesar resultados de opciones con casting seguro y ordenar alfabéticamente
            nacionalidades = (optionsResults[0].await().getOrNull() as? List<Nacionalidad>)?.sortedBy { it.pais } ?: emptyList()
            provincias = (optionsResults[1].await().getOrNull() as? List<Provincia>)?.sortedBy { it.nombre_provincia } ?: emptyList()
            allDistritos = (optionsResults[2].await().getOrNull() as? List<Distrito>)?.sortedBy { it.nombre_distrito } ?: emptyList() // Guardar lista completa y ordenar
            allCorregimientos = (optionsResults[3].await().getOrNull() as? List<Corregimiento>)?.sortedBy { it.nombre_corregimiento } ?: emptyList() // Guardar lista completa y ordenar
            departamentos = (optionsResults[4].await().getOrNull() as? List<Departamento>)?.sortedBy { it.nombre } ?: emptyList()
            allCargos = (optionsResults[5].await().getOrNull() as? List<Cargo>)?.sortedBy { it.nombre } ?: emptyList() // Guardar lista completa y ordenar


            // Cargar los datos del empleado específico
            val employeeResult = withContext(Dispatchers.IO) {
                employeeRepository.getEmployee(cedula) // Usar la función getEmployee
            }

            if (employeeResult.isSuccess) {
                val foundEmployee = employeeResult.getOrNull()

                if (foundEmployee != null) {
                    employee = foundEmployee // Guardar el empleado original cargado

                    // Rellenar los estados del formulario con los datos del empleado
                    primerNombre = foundEmployee.nombre1 ?: ""
                    segundoNombre = foundEmployee.nombre2 ?: ""
                    primerApellido = foundEmployee.apellido1 ?: ""
                    segundoApellido = foundEmployee.apellido2 ?: ""
                    apellidoCasado = foundEmployee.apellidoc ?: ""
                    genero = mapGeneroCodeToName(foundEmployee.genero) // Mapear código a nombre
                    estadoCivil = mapEstadoCivilCodeToName(foundEmployee.estadoCivil) // Mapear código a nombre
                    fechaNacimiento = parseDate(foundEmployee.fechaNacimiento)
                    tipoSangre = foundEmployee.tipoSangre ?: ""
                    usaAc = foundEmployee.usaAc ?: 0 // Usar 0 como valor por defecto si es null

                    // Asegurarse de que los números de teléfono se muestren como String
                    celular = foundEmployee.celular?.toString() ?: ""
                    telefono = foundEmployee.telefono?.toString() ?: ""
                    email = foundEmployee.correo ?: ""
                    // REMOVED: password = "" // Dejar la contraseña vacía al cargar para edición

                    calle = foundEmployee.calle ?: ""
                    casa = foundEmployee.casa ?: ""
                    comunidad = foundEmployee.comunidad ?: ""

                    fechaContratacion = parseDate(foundEmployee.fechaContrato)
                    estado = foundEmployee.estado ?: 1 // Usar 1 como valor por defecto si es null

                    // Seleccionar los objetos en los Dropdowns basándose en los códigos cargados
                    selectedNacionalidad = nacionalidades.find { it.codigo == foundEmployee.nacionalidad } // Buscar por código
                    selectedProvincia = provincias.find { it.codigo_provincia == foundEmployee.provincia } // Buscar por código
                    selectedDistrito = allDistritos.find { it.codigo_distrito == foundEmployee.distrito && it.codigo_provincia == foundEmployee.provincia } // Buscar por código de distrito y provincia
                    selectedCorregimiento = allCorregimientos.find { it.codigo == foundEmployee.corregimiento && it.codigo_distrito == foundEmployee.distrito && it.codigo_provincia == foundEmployee.provincia } // Buscar por código de corregimiento, distrito y provincia
                    selectedDepartamento = departamentos.find { it.codigo == foundEmployee.departamento } // Buscar por código
                    selectedCargo = allCargos.find { it.codigo == foundEmployee.cargo && it.dep_codigo == foundEmployee.departamento } // Buscar por código de cargo y departamento


                    hasChanges = false // No hay cambios al cargar
                } else {
                    errorMessage = "No se encontró el empleado con cédula $cedula"
                }
            } else {
                errorMessage = "Error al cargar los datos del empleado: ${employeeResult.exceptionOrNull()?.message}"
                Log.e("DetailsEmployee", "Error loading employee", employeeResult.exceptionOrNull())
            }
        } catch (e: Exception) {
            errorMessage = "Error inesperado durante la carga: ${e.message}"
            Log.e("DetailsEmployee", "Unexpected error during load", e)
        } finally {
            isLoading = false
        }
    }

    // Derived states for filtered lists based on current selections
    val filteredDistritos = remember(selectedProvincia, allDistritos) {
        // Capture the delegated property value in a local variable
        val currentProvincia = selectedProvincia
        if (currentProvincia == null) {
            emptyList()
        } else {
            // Use the local variable for filtering
            allDistritos.filter { it.codigo_provincia == currentProvincia.codigo_provincia }
        }
    }

    val filteredCorregimientos = remember(selectedProvincia, selectedDistrito, allCorregimientos) {
        // Capture the delegated property values in local variables
        val currentProvincia = selectedProvincia
        val currentDistrito = selectedDistrito
        if (currentProvincia == null || currentDistrito == null) {
            emptyList()
        } else {
            // Use the local variables for filtering
            allCorregimientos.filter {
                it.codigo_provincia == currentProvincia.codigo_provincia && it.codigo_distrito == currentDistrito.codigo_distrito
            }
        }
    }

    val filteredCargos = remember(selectedDepartamento, allCargos) {
        // Capture the delegated property value in a local variable
        val currentDepartamento = selectedDepartamento
        if (currentDepartamento == null) {
            emptyList()
        } else {
            // Use the local variable for filtering
            allCargos.filter { it.dep_codigo == currentDepartamento.codigo || it.dep_codigo == null } // Include cargos without department assigned if needed
        }
    }


    // Función para validar todos los campos
    fun validateFields(): Boolean {
        var isValid = true

        // Resetear errores previos
        primerNombreError = null
        segundoNombreError = null
        primerApellidoError = null
        segundoApellidoError = null
        apellidoCasadoError = null
        tipoSangreError = null
        celularError = null
        telefonoError = null
        emailError = null
        calleError = null
        casaError = null
        comunidadError = null
        departamentoError = null // Resetear error de departamento
        cargoError = null // Resetear error de cargo
        nacionalidadError = null // Reset error state for Nacionalidad
        provinciaError = null // Reset error state for Provincia
        distritoError = null // Reset error state for Distrito
        corregimientoError = null // Reset error state for Corregimiento
        fechaNacimientoError = null // Reset error state for Fecha Nacimiento
        generoError = null // Reset error state for Genero
        estadoCivilError = null // Reset error state for Estado Civil
        errorMessage = null // Reset general error message


        // Validaciones de longitud (basado en varchar(X)) y obligatoriedad
        // Note: Real-time input filtering handles character types and some length constraints.
        // This validation is for required fields and final length checks.

        // Personal Info Validation
        // Cedula is NOT editable in this screen, so no validation needed here for it.

        if (primerNombre.isEmpty()) { primerNombreError = "Primer Nombre es obligatorio"; isValid = false }
        if (primerNombre.length > 25) { primerNombreError = "Máximo 25 caracteres"; isValid = false }

        if (primerApellido.isEmpty()) { primerApellidoError = "Primer Apellido es obligatorio"; isValid = false }
        if (primerApellido.length > 25) { primerApellidoError = "Máximo 25 caracteres"; isValid = false }

        if (segundoNombre.length > 25) { segundoNombreError = "Máximo 25 caracteres"; isValid = false }
        if (segundoApellido.length > 25) { segundoApellidoError = "Máximo 25 caracteres"; isValid = false }
        if (apellidoCasado.length > 25) { apellidoCasadoError = "Máximo 25 caracteres"; isValid = false }

        // Dropdown Validations (now mandatory)
        if (fechaNacimiento == 0L) { fechaNacimientoError = "Fecha de Nacimiento es obligatoria"; isValid = false }
        if (genero.isEmpty()) { generoError = "Género es obligatorio"; isValid = false }
        if (estadoCivil.isEmpty()) { estadoCivilError = "Estado Civil es obligatorio"; isValid = false }
        if (tipoSangre.isEmpty()) { tipoSangreError = "Tipo de Sangre es obligatorio"; isValid = false }
        if (selectedNacionalidad == null) { nacionalidadError = "Nacionalidad es obligatoria"; isValid = false }


        // Address Info Validation
        // Dropdown Validations (now mandatory)
        if (selectedProvincia == null) { provinciaError = "Provincia es obligatoria"; isValid = false }
        if (selectedDistrito == null) { distritoError = "Distrito es obligatorio"; isValid = false }
        if (selectedCorregimiento == null) { corregimientoError = "Corregimiento es obligatorio"; isValid = false }


        if (calle.length > 30) { calleError = "Máximo 30 caracteres"; isValid = false }
        if (casa.length > 10) { casaError = "Máximo 10 caracteres"; isValid = false }
        if (comunidad.length > 25) { comunidadError = "Máximo 25 caracteres"; isValid = false }


        // Contact Info Validation
        // Validación de números de teléfono (después de limpiar guiones)
        val celularCleaned = celular.replace("-", "")
        // Character validation is done in the component now, just check length if needed
        if (celularCleaned.length > 15) { celularError = "Demasiados dígitos"; isValid = false }

        val telefonoCleaned = telefono.replace("-", "")
        // Character validation is done in the component now, just check length if needed
        if (telefonoCleaned.length > 15) { telefonoError = "Demasiados dígitos"; isValid = false }

        // Validación de email (formato básico y longitud)
        if (email.isEmpty()) { emailError = "Correo electrónico es obligatorio"; isValid = false } // Assuming email is required
        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Formato de correo inválido"
            isValid = false
        }
        if (email.length > 40) { emailError = "Máximo 40 caracteres"; isValid = false }


        // Work Info Validation
        // Dropdown Validations (now mandatory)
        if (selectedDepartamento == null) {
            departamentoError = "Seleccione un departamento"
            isValid = false
        }
        if (selectedCargo == null) {
            cargoError = "Seleccione un cargo"
            isValid = false
        }
        // TODO: Add validation for required fields like Fecha de Contratacion if needed
        // TODO: Add validation for Estado if needed (currently defaults to 1)


        if (!isValid) {
            errorMessage = "Por favor, corrige los errores en el formulario."
        }

        return isValid
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
        } else if (errorMessage != null && !hasChanges) { // Mostrar error general solo si no hay cambios pendientes
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
        }
        else { // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                employee?.let { emp ->
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
                                // Mostrar nombres editables si no están vacíos, de lo contrario usar los originales
                                text = "Empleado: ${primerNombre.takeIf { it.isNotEmpty() } ?: emp.nombre1 ?: ""} ${primerApellido.takeIf { it.isNotEmpty() } ?: emp.apellido1 ?: ""}",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Cédula: ${emp.cedula}", // Cédula no editable en esta vista
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            InfoCard(
                                icon = Icons.Default.Person,
                                title = "Información Personal",
                                subtitle = "Datos básicos del empleado",
                                backgroundColor = if (expandedSection == "personal")
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (expandedSection == "personal")
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                iconTint = if (expandedSection == "personal")
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.primary,
                                onClick = {
                                    expandedSection = if (expandedSection == "personal") null else "personal"
                                }
                            )

                            InfoCard(
                                icon = Icons.Default.LocationOn,
                                title = "Dirección",
                                subtitle = "Ubicación del empleado",
                                backgroundColor = if (expandedSection == "direccion")
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (expandedSection == "direccion")
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                iconTint = if (expandedSection == "direccion")
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.primary,
                                onClick = {
                                    expandedSection = if (expandedSection == "direccion") null else "direccion"
                                }
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            InfoCard(
                                icon = Icons.Default.Phone,
                                title = "Información de Contacto",
                                subtitle = "Teléfono y correo",
                                backgroundColor = if (expandedSection == "contacto")
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (expandedSection == "contacto")
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                iconTint = if (expandedSection == "contacto")
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.primary,
                                onClick = {
                                    expandedSection = if (expandedSection == "contacto") null else "contacto"
                                }
                            )

                            InfoCard(
                                icon = Icons.Default.LocationOn,
                                title = "Información Laboral",
                                subtitle = "Cargo y departamento",
                                backgroundColor = if (expandedSection == "laboral")
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = if (expandedSection == "laboral")
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                iconTint = if (expandedSection == "laboral")
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.primary,
                                onClick = {
                                    expandedSection = if (expandedSection == "laboral") null else "laboral"
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Contenido expandido de las secciones
                    if (expandedSection != null) {
                        if (expandedSection == "personal") {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Pasar los estados editables y de error a los componentes
                                PersonalInfoComponent(
                                    cedula = emp.cedula, // Cédula no editable
                                    onCedulaChange = { }, // No permitir cambio de cédula
                                    readOnlyCedula = true, // Set cedula as read-only
                                    cedulaError = null, // No error state needed for non-editable field

                                    primerNombre = primerNombre,
                                    onPrimerNombreChange = { primerNombre = it; hasChanges = true; primerNombreError = null }, // Reset error on change
                                    primerNombreError = primerNombreError, // Pass error state

                                    segundoNombre = segundoNombre,
                                    onSegundoNombreChange = { segundoNombre = it; hasChanges = true; segundoNombreError = null }, // Reset error on change
                                    segundoNombreError = segundoNombreError, // Pass error state

                                    primerApellido = primerApellido,
                                    onPrimerApellidoChange = { primerApellido = it; hasChanges = true; primerApellidoError = null }, // Reset error on change
                                    primerApellidoError = primerApellidoError, // Pass error state

                                    segundoApellido = segundoApellido,
                                    onSegundoApellidoChange = { segundoApellido = it; hasChanges = true; segundoApellidoError = null }, // Reset error on change
                                    segundoApellidoError = segundoApellidoError, // Pass error state

                                    apellidoCasado = apellidoCasado,
                                    onApellidoCasadoChange = { apellidoCasado = it; hasChanges = true; apellidoCasadoError = null }, // Reset error on change
                                    apellidoCasadoError = apellidoCasadoError, // Pass error state

                                    fechaNacimiento = fechaNacimiento,
                                    onFechaNacimientoChange = { fechaNacimiento = it; hasChanges = true; fechaNacimientoError = null }, // Reset error on change
                                    fechaNacimientoError = fechaNacimientoError, // Pass error state

                                    genero = genero,
                                    onGeneroChange = { genero = it; hasChanges = true; generoError = null }, // Reset error on change
                                    generoError = generoError, // Pass error state

                                    estadoCivil = estadoCivil,
                                    onEstadoCivilChange = { estadoCivil = it; hasChanges = true; estadoCivilError = null }, // Reset error on change
                                    estadoCivilError = estadoCivilError, // Pass error state

                                    tipoSangre = tipoSangre,
                                    onTipoSangreChange = { tipoSangre = it.uppercase(); hasChanges = true; tipoSangreError = null }, // UI String, Reset error on change
                                    tipoSangreError = tipoSangreError, // Pass error state

                                    usaAc = usaAc, onUsaAcChange = { usaAc = it; hasChanges = true }, // 0=No, 1=Sí

                                    // Pass the list of nationalities and handle selection
                                    nationalities = nacionalidades, // Pass the list
                                    selectedNacionalidad = selectedNacionalidad, // Pass current selection
                                    onNacionalidadSelected = { nacionalidad -> selectedNacionalidad = nacionalidad; hasChanges = true; nacionalidadError = null }, // Handle selection, Reset error on change
                                    nacionalidadError = nacionalidadError // Pass error state
                                )
                            }
                        }

                        if (expandedSection == "contacto") {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Pasar los estados editables y de error a los componentes
                                ContactInfoComponent(
                                    celular = celular,
                                    onCelularChange = { celular = it; hasChanges = true; celularError = null },
                                    celularError = celularError, // Pasar estado de error
                                    telefono = telefono,
                                    onTelefonoChange = { telefono = it; hasChanges = true; telefonoError = null },
                                    telefonoError = telefonoError, // Pasar estado de error
                                    email = email,
                                    onEmailChange = { email = it; hasChanges = true; emailError = null },
                                    emailError = emailError, // Pasar estado de error
                                    // REMOVED: password and passwordError are no longer passed
                                )
                            }
                        }

                        if (expandedSection == "direccion") {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Pasar los estados editables y de error a los componentes
                                AddressInfoComponent(
                                    provinces = provincias,
                                    selectedProvincia = selectedProvincia,
                                    onProvinciaSelected = {
                                        selectedProvincia = it
                                        selectedDistrito = null // Resetear distrito y corregimiento
                                        selectedCorregimiento = null
                                        hasChanges = true
                                        provinciaError = null // Reset error on selection change
                                        distritoError = null // Reset dependent errors
                                        corregimientoError = null // Reset dependent errors
                                    },
                                    provinciaError = provinciaError, // Pass error state

                                    // **Pasar la lista de distritos FILTRADA**
                                    distritos = filteredDistritos,
                                    selectedDistrito = selectedDistrito,
                                    onDistritoSelected = {
                                        selectedDistrito = it
                                        selectedCorregimiento = null
                                        hasChanges = true
                                        distritoError = null // Reset error on selection change
                                        corregimientoError = null // Reset dependent error
                                    },
                                    distritoError = distritoError, // Pass error state

                                    // **Pasar la lista de corregimientos FILTRADA**
                                    corregimientos = filteredCorregimientos,
                                    selectedCorregimiento = selectedCorregimiento,
                                    onCorregimientoSelected = { corregimiento -> selectedCorregimiento = corregimiento; hasChanges = true; corregimientoError = null }, // Handle selection, Reset error on change
                                    corregimientoError = corregimientoError, // Pass error state

                                    calle = calle, onCalleChange = { calle = it; hasChanges = true; calleError = null }, // UI String, Reset error on change
                                    calleError = calleError, // Pass error state

                                    casa = casa, onCasaChange = { casa = it; hasChanges = true; casaError = null }, // UI String, Reset error on change
                                    casaError = casaError, // Pass error state

                                    comunidad = comunidad, onComunidadChange = { comunidad = it; hasChanges = true; comunidadError = null }, // UI String, Reset error on change
                                    comunidadError = comunidadError // Pass error state
                                )
                            }
                        }

                        if (expandedSection == "laboral") {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Pasar los estados editables y de error a los componentes
                                WorkInfoComponent(
                                    departamentos = departamentos,
                                    selectedDepartamento = selectedDepartamento,
                                    onDepartamentoSelected = {
                                        selectedDepartamento = it
                                        // Si el departamento cambia, resetear el cargo si el cargo seleccionado no pertenece al nuevo departamento
                                        if (selectedCargo?.dep_codigo != it?.codigo) {
                                            selectedCargo = null
                                        }
                                        hasChanges = true
                                        departamentoError = null // Reset error on selection change
                                        cargoError = null // Reset dependent error
                                    },
                                    departamentoError = departamentoError, // Pass error state

                                    // **Pasar la lista de cargos FILTRADA**
                                    cargos = filteredCargos, // Pass the filtered list
                                    selectedCargo = selectedCargo, // Pass current selection
                                    onCargoSelected = { cargo -> selectedCargo = cargo; hasChanges = true; cargoError = null }, // Handle selection, Reset error on change
                                    cargoError = cargoError, // Pass error state

                                    fechaContratacion = fechaContratacion, onFechaContratacionChange = { fechaContratacion = it; hasChanges = true }, // UI Long
                                    estado = estado, onEstadoChange = { estado = it; hasChanges = true } // UI Int
                                )
                            }
                        }
                    }

                    // Mostrar error general del formulario si existe y hay cambios pendientes
                    if (errorMessage != null && hasChanges) {
                        Text(
                            text = errorMessage ?: "Por favor, corrige los errores en el formulario.",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                        )
                    }


                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                // Validar campos antes de intentar guardar
                                if (validateFields()) {
                                    coroutineScope.launch {
                                        try {
                                            // --- Realizar las conversiones y extracciones antes de crear el objeto Employee ---

                                            // Limpiar guiones de teléfonos y convertir a Int?
                                            val celularCleaned = celular.replace("-", "")
                                            val celularInt = celularCleaned.toIntOrNull()

                                            val telefonoCleaned = telefono.replace("-", "")
                                            val telefonoInt = telefonoCleaned.toIntOrNull()

                                            // Mapear String de Género a Int?
                                            val generoInt = mapGeneroNameToCode(genero)

                                            // Mapear String de Estado Civil a Int?
                                            val estadoCivilInt = mapEstadoCivilNameToCode(estadoCivil)

                                            // Extraer prefijo, tomo, asiento de la cédula (emp.cedula)
                                            // NOTA: La cédula no es editable en esta vista, usamos la original del empleado cargado.
                                            val cedulaParts = emp.cedula.split("-")
                                            val prefijoValue = cedulaParts.getOrNull(0).takeIf { !it.isNullOrEmpty() }
                                            val tomoValue = cedulaParts.getOrNull(1).takeIf { !it.isNullOrEmpty() }
                                            val asientoValue = cedulaParts.getOrNull(2).takeIf { !it.isNullOrEmpty() }

                                            // Manejar usaAc - solo relevante para mujeres casadas o viudas
                                            val usaAcInt: Int? = if (genero == "Femenino" && (estadoCivil == "Casado/a" || estadoCivil == "Viudo/a")) {
                                                usaAc // Usamos el estado usaAc de la UI (0 o 1)
                                            } else {
                                                null // Valor nulo si no es relevante
                                            }


                                            // Crear el objeto Employee actualizado con los datos del formulario
                                            val updatedEmployee = Employee(
                                                cedula = emp.cedula, // Usar la cédula original del empleado
                                                prefijo = prefijoValue, // Usar valor extraído
                                                tomo = tomoValue, // Usar valor extraído
                                                asiento = asientoValue, // Usar valor extraído
                                                nombre1 = primerNombre.takeIf { it.isNotEmpty() },
                                                nombre2 = segundoNombre.takeIf { it.isNotEmpty() },
                                                apellido1 = primerApellido.takeIf { it.isNotEmpty() },
                                                apellido2 = segundoApellido.takeIf { it.isNotEmpty() },
                                                apellidoc = apellidoCasado.takeIf { it.isNotEmpty() },
                                                genero = generoInt, // Usar el Int mapeado
                                                estadoCivil = estadoCivilInt, // Usar el Int mapeado
                                                tipoSangre = tipoSangre.takeIf { it.isNotEmpty() },
                                                usaAc = usaAcInt, // Usar el Int mapeado (o null)
                                                fechaNacimiento = formatDate(fechaNacimiento), // Formatear a String?
                                                celular = celularInt, // Usar el Int? limpio
                                                telefono = telefonoInt, // Usar el Int? limpio
                                                correo = email.takeIf { it.isNotEmpty() },
                                                contrasena = null, // Do not update password here
                                                provincia = selectedProvincia?.codigo_provincia, // Usar código del objeto seleccionado
                                                distrito = selectedDistrito?.codigo_distrito, // Usar código del objeto seleccionado
                                                corregimiento = selectedCorregimiento?.codigo, // Usar código del objeto seleccionado
                                                calle = calle.takeIf { it.isNotEmpty() },
                                                casa = casa.takeIf { it.isNotEmpty() },
                                                comunidad = comunidad.takeIf { it.isNotEmpty() },
                                                nacionalidad = selectedNacionalidad?.codigo, // Usar código del objeto seleccionado
                                                fechaContrato = formatDate(fechaContratacion), // Formatear a String?
                                                cargo = selectedCargo?.codigo, // Usar código del objeto seleccionado
                                                departamento = selectedDepartamento?.codigo, // Usar código del objeto seleccionado
                                                estado = estado // Usar el Int directamente
                                            )

                                            Log.d("DetailsEmployee", "Intentando actualizar empleado: ${updatedEmployee}")
                                            // --- Llamar a la función de actualización en el Repository ---
                                            val result = withContext(Dispatchers.IO) {
                                                employeeRepository.updateEmployee(updatedEmployee) // Llamada real a la actualización
                                            }

                                            if (result.isSuccess) {
                                                hasChanges = false // Resetear estado de cambios
                                                // Mostrar mensaje de éxito si es necesario antes de navegar
                                                // O simplemente navegar de vuelta
                                                onNavigateBack()
                                            } else {
                                                errorMessage = "Error al actualizar el empleado: ${result.exceptionOrNull()?.message}"
                                                Log.e("DetailsEmployee", "Error updating employee", result.exceptionOrNull())
                                            }
                                        } catch (e: Exception) {
                                            errorMessage = "Error inesperado al guardar: ${e.message}"
                                            Log.e("DetailsEmployee", "Unexpected error during save", e)
                                        }
                                    }
                                } else {
                                    // Si la validación falla, errorMessage ya se habrá actualizado en validateFields()
                                    // El usuario verá el mensaje de error general y los errores específicos en los campos.
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = hasChanges // Habilitar solo si hay cambios
                        ) {
                            Text("Guardar Cambios")
                        }

                        OutlinedButton(
                            onClick = {
                                // TODO: Implementar confirmación si hasChanges es true
                                // if (hasChanges) { showConfirmDialog = true } else { onNavigateBack() }
                                onNavigateBack() // Por ahora, simplemente regresa
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }
                    }
                }
            }
        }

    }
}
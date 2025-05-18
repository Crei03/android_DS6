package com.proyect.ds6.ui.admin

import android.annotation.SuppressLint
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

// Import necessary components for ViewModel and data
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.proyect.ds6.model.* // Import all your data classes
import com.proyect.ds6.data.repository.AppContainer // Import the repository provider (located in data.repository)
import com.proyect.ds6.presentation.AddEmployeeViewModel // Import the ViewModel
import com.proyect.ds6.presentation.AddEmployeeViewModelFactory // Import the ViewModel Factory
import com.proyect.ds6.presentation.SaveState // Import the SaveState sealed class

// Assume these components are defined elsewhere in your ui.employee.components package
import com.proyect.ds6.ui.employee.components.PersonalInfoComponent
import com.proyect.ds6.ui.employee.components.AddressInfoComponent
import com.proyect.ds6.ui.employee.components.ContactInfoComponent
import com.proyect.ds6.ui.employee.components.WorkInfoComponent
import com.proyect.ds6.ui.admin.components.StepProgressIndicator // Assuming this is in ui.admin.components

import android.util.Patterns // Import for email validation

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployee(
    onBackClick: () -> Unit = {},
    // Get the ViewModel instance using the factory from the presentation layer,
    // which uses the repository provided by AppContainer.
    viewModel: AddEmployeeViewModel = viewModel(
        factory = AddEmployeeViewModelFactory(AppContainer.employeeRepository)
    )
) {
    // --- States for all form fields (these hold the input values in the UI) ---
    var cedula by remember { mutableStateOf("") }
    var primerNombre by remember { mutableStateOf("") }
    var segundoNombre by remember { mutableStateOf("") }
    var primerApellido by remember { mutableStateOf("") }
    var segundoApellido by remember { mutableStateOf("") }
    var apellidoCasado by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf(0L) } // UI uses Long timestamp for date
    var genero by remember { mutableStateOf("") } // UI uses String from PersonalInfoComponent
    var estadoCivil by remember { mutableStateOf("") } // UI uses String from PersonalInfoComponent
    var tipoSangre by remember { mutableStateOf("") } // UI uses String from PersonalInfoComponent
    var usaAc by remember { mutableStateOf(0) } // UI usa Int: 0=No, 1=Sí

    // States for Address Info - will hold the SELECTED OBJECTS or their identifiers
    var selectedProvincia by remember { mutableStateOf<Provincia?>(null) }
    var selectedDistrito by remember { mutableStateOf<Distrito?>(null) }
    var selectedCorregimiento by remember { mutableStateOf<Corregimiento?>(null) }
    var calle by remember { mutableStateOf("") }
    var casa by remember { mutableStateOf("") }
    var comunidad by remember { mutableStateOf("") }
    var selectedNacionalidad by remember { mutableStateOf<Nacionalidad?>(null) } // State for selected Nacionalidad

    // States for Contact Info
    var celular by remember { mutableStateOf("") } // UI uses String for phone
    var telefono by remember { mutableStateOf("") } // UI uses String for phone
    var email by remember { mutableStateOf("") }
    // REMOVED: var password by remember { mutableStateOf("") } // **SECURITY WARNING: Handle passwords securely!**

    // States for Work Info - will hold the SELECTED OBJECTS
    var selectedDepartamento by remember { mutableStateOf<Departamento?>(null) }
    var selectedCargo by remember { mutableStateOf<Cargo?>(null) }
    var fechaContratacion by remember { mutableStateOf(0L) } // UI uses Long timestamp for date
    var estado by remember { mutableStateOf(1) } // UI uses Int from WorkInfoComponent


    // --- State for controlling the current step of the form ---
    var currentStep by remember { mutableStateOf(1) }
    val totalSteps = 4

    // State for controlling TopAppBar color based on scroll (existing)
    val scrollState = rememberScrollState()
    // val topAppBarColor ... (existing)

    // Titles for the form steps (existing)
    val stepTitles = listOf(
        "Información Personal",
        "Información de Dirección",
        "Información de Contacto",
        "Información de Trabajo"
    )

    // --- Collect the lists of options from the ViewModel ---
    val nationalities by viewModel.nationalities.collectAsState()
    val provinces by viewModel.provinces.collectAsState()
    val filteredDistritos by viewModel.filteredDistritos.collectAsState() // Observe filtered list
    val filteredCorregimientos by viewModel.filteredCorregimientos.collectAsState() // Observe filtered list
    val cargos by viewModel.cargos.collectAsState()
    val departamentos by viewModel.departamentos.collectAsState()

    // --- Collect loading and error states for options ---
    val isLoadingOptions by viewModel.isLoadingOptions.collectAsState()
    val optionsError by viewModel.optionsError.collectAsState()


    // Collect the save state from the ViewModel to react to changes
    val saveState by viewModel.saveState.collectAsState()

    // States for showing feedback (e.g., Dialogs)
    var showFeedbackDialog by remember { mutableStateOf(false) }
    var feedbackTitle by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }

    // --- States for validation errors ---
    var cedulaError by remember { mutableStateOf<String?>(null) }
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
    var departamentoError by remember { mutableStateOf<String?>(null) }
    var cargoError by remember { mutableStateOf<String?>(null) }
    var nacionalidadError by remember { mutableStateOf<String?>(null) } // Added error state for Nacionalidad
    var provinciaError by remember { mutableStateOf<String?>(null) } // Added error state for Provincia
    var distritoError by remember { mutableStateOf<String?>(null) } // Added error state for Distrito
    var corregimientoError by remember { mutableStateOf<String?>(null) } // Added error state for Corregimiento
    var fechaNacimientoError by remember { mutableStateOf<String?>(null) } // Added error state for Fecha Nacimiento
    var generoError by remember { mutableStateOf<String?>(null) } // Added error state for Genero
    var estadoCivilError by remember { mutableStateOf<String?>(null) } // Added error state for Estado Civil
    var errorMessage: String? by remember { mutableStateOf(null) } // General error message state


    // LaunchedEffect to react to changes in the saveState
    LaunchedEffect(saveState) {
        when (saveState) {
            is SaveState.Success -> {
                feedbackTitle = "Éxito"
                feedbackMessage = "Empleado guardado correctamente."
                showFeedbackDialog = true
                viewModel.resetSaveState() // Reset state after handling
                // Redirigir inmediatamente al listado de empleados
                onBackClick()
            }
            is SaveState.Error -> {
                feedbackTitle = "Error al Guardar"
                feedbackMessage = (saveState as SaveState.Error).message
                showFeedbackDialog = true
                viewModel.resetSaveState() // Reset state after handling
            }
            SaveState.Loading -> {
                // You might want to show a progress indicator here,
                // or disable buttons, which is handled below via `enabled`.
            }
            SaveState.Idle -> {
                // Do nothing when idle
            }
        }
    }

    // LaunchedEffect to show error when loading options
    LaunchedEffect(optionsError) {
        optionsError?.let {
            feedbackTitle = "Error de Carga"
            feedbackMessage = it
            showFeedbackDialog = true
            viewModel.resetOptionsError() // Reset error state after showing
        }
    }

    // --- Validation function ---
    fun validateFields(): Boolean {
        var isValid = true

        // Reset previous errors
        cedulaError = null
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
        departamentoError = null
        cargoError = null
        nacionalidadError = null // Reset error state for Nacionalidad
        provinciaError = null // Reset error state for Provincia
        distritoError = null // Reset error state for Distrito
        corregimientoError = null // Reset error state for Corregimiento
        fechaNacimientoError = null // Reset error state for Fecha Nacimiento
        generoError = null // Reset error state for Genero
        estadoCivilError = null // Reset error state for Estado Civil
        errorMessage = null // Reset general error message


        // Step-specific validation
        when (currentStep) {
            1 -> {
                // Personal Info Validation
                if (cedula.isEmpty()) { cedulaError = "Cédula es obligatoria"; isValid = false }
                if (cedula.length > 13) { cedulaError = "Máximo 13 caracteres"; isValid = false }
                // TODO: Add more specific cédula format validation if needed (e.g., regex)

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

            }
            2 -> {
                // Address Info Validation
                // Dropdown Validations (now mandatory)
                if (selectedProvincia == null) { provinciaError = "Provincia es obligatoria"; isValid = false }
                if (selectedDistrito == null) { distritoError = "Distrito es obligatorio"; isValid = false }
                if (selectedCorregimiento == null) { corregimientoError = "Corregimiento es obligatorio"; isValid = false }

                if (calle.length > 30) { calleError = "Máximo 30 caracteres"; isValid = false }
                if (casa.length > 10) { casaError = "Máximo 10 caracteres"; isValid = false }
                if (comunidad.length > 25) { comunidadError = "Máximo 25 caracteres"; isValid = false }
            }
            3 -> {
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
            }
            4 -> {
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
            }
        }


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
                        "Agregar Empleado",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Añadimos el indicador de progreso de pasos
            StepProgressIndicator(
                currentStep = currentStep,
                totalSteps = totalSteps,
                stepTitle = stepTitles[currentStep - 1]
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress indicator for options loading
            if (isLoadingOptions) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                // Display the correct form component based on the current step
                when (currentStep) {
                    1 -> PersonalInfoComponent(
                        cedula = cedula, onCedulaChange = { cedula = it; cedulaError = null }, // Reset error on change
                        readOnlyCedula = false, // Cedula IS editable in AddEmployee screen
                        cedulaError = cedulaError, // Pass error state

                        primerNombre = primerNombre, onPrimerNombreChange = { primerNombre = it; primerNombreError = null }, // Reset error on change
                        primerNombreError = primerNombreError, // Pass error state

                        segundoNombre = segundoNombre, onSegundoNombreChange = { segundoNombre = it; segundoNombreError = null }, // Reset error on change
                        segundoNombreError = segundoNombreError, // Pass error state

                        primerApellido = primerApellido, onPrimerApellidoChange = { primerApellido = it; primerApellidoError = null }, // Reset error on change
                        primerApellidoError = primerApellidoError, // Pass error state

                        segundoApellido = segundoApellido, onSegundoApellidoChange = { segundoApellido = it; segundoApellidoError = null }, // Reset error on change
                        segundoApellidoError = segundoApellidoError, // Pass error state

                        apellidoCasado = apellidoCasado, onApellidoCasadoChange = { apellidoCasado = it; apellidoCasadoError = null }, // Reset error on change
                        apellidoCasadoError = apellidoCasadoError, // Pass error state

                        fechaNacimiento = fechaNacimiento,
                        onFechaNacimientoChange = { fechaNacimiento = it; fechaNacimientoError = null }, // Reset error on change
                        fechaNacimientoError = fechaNacimientoError, // Pass error state

                        genero = genero,
                        onGeneroChange = { genero = it; generoError = null }, // Reset error on change
                        generoError = generoError, // Pass error state

                        estadoCivil = estadoCivil,
                        onEstadoCivilChange = { estadoCivil = it; estadoCivilError = null }, // Reset error on change
                        estadoCivilError = estadoCivilError, // Pass error state

                        tipoSangre = tipoSangre,
                        onTipoSangreChange = { tipoSangre = it.uppercase(); tipoSangreError = null }, // UI String, Reset error on change
                        tipoSangreError = tipoSangreError, // Pass error state

                        usaAc = usaAc, onUsaAcChange = { usaAc = it }, // 0=No, 1=Sí

                        // Pass the list of nationalities and handle selection
                        nationalities = nationalities, // Pass the list
                        selectedNacionalidad = selectedNacionalidad, // Pass current selection
                        onNacionalidadSelected = { nacionalidad -> selectedNacionalidad = nacionalidad; nacionalidadError = null }, // Handle selection, Reset error on change
                        nacionalidadError = nacionalidadError // Pass error state
                    )
                    2 -> AddressInfoComponent(
                        // Pass the lists of provinces, filtered districts, and filtered corregimientos
                        provinces = provinces, // Pass the list
                        selectedProvincia = selectedProvincia, // Pass current selection
                        onProvinciaSelected = { provincia ->
                            selectedProvincia = provincia // Update selected province
                            selectedDistrito = null // Reset district when province changes
                            selectedCorregimiento = null // Reset corregimiento when province changes
                            viewModel.filterDistritosByProvincia(provincia?.codigo_provincia) // Filter districts
                            provinciaError = null // Reset error on selection change
                            distritoError = null // Reset dependent errors
                            corregimientoError = null // Reset dependent errors
                        },
                        provinciaError = provinciaError, // Pass error state

                        distritos = filteredDistritos, // Pass the filtered list
                        selectedDistrito = selectedDistrito, // Pass current selection
                        onDistritoSelected = { distrito ->
                            selectedDistrito = distrito // Update selected district
                            selectedCorregimiento = null // Reset corregimiento when district changes
                            viewModel.filterCorregimientosByDistrito(selectedProvincia?.codigo_provincia, distrito?.codigo_distrito) // Filter corregimientos
                            distritoError = null // Reset error on selection change
                            corregimientoError = null // Reset dependent error
                        },
                        distritoError = distritoError, // Pass error state

                        corregimientos = filteredCorregimientos, // Pass the filtered list
                        selectedCorregimiento = selectedCorregimiento, // Pass current selection
                        onCorregimientoSelected = { corregimiento -> selectedCorregimiento = corregimiento; corregimientoError = null }, // Handle selection, Reset error on change
                        corregimientoError = corregimientoError, // Pass error state

                        calle = calle, onCalleChange = { calle = it; calleError = null }, // UI String, Reset error on change
                        calleError = calleError, // Pass error state

                        casa = casa, onCasaChange = { casa = it; casaError = null }, // UI String, Reset error on change
                        casaError = casaError, // Pass error state

                        comunidad = comunidad, onComunidadChange = { comunidad = it; comunidadError = null }, // UI String, Reset error on change
                        comunidadError = comunidadError // Pass error state
                    )
                    3 -> ContactInfoComponent(
                        celular = celular, onCelularChange = { celular = it; celularError = null }, // UI String, Reset error on change
                        celularError = celularError, // Pass error state

                        telefono = telefono, onTelefonoChange = { telefono = it; telefonoError = null }, // UI String, Reset error on change
                        telefonoError = telefonoError, // Pass error state

                        email = email, onEmailChange = { email = it; emailError = null }, // UI String, Reset error on change
                        emailError = emailError, // Pass error state
                        // REMOVED: password and onPasswordChange
                        // TODO: Add error parameter for Password if needed (handle securely)
                    )
                    4 -> WorkInfoComponent(
                        // Pass the lists of departments and cargos
                        departamentos = departamentos, // Pass the list
                        selectedDepartamento = selectedDepartamento, // Pass current selection
                        onDepartamentoSelected = { departamento ->
                            selectedDepartamento = departamento
                            // Si el departamento cambia, resetear el cargo si el cargo seleccionado no pertenece al nuevo departamento
                            if (selectedCargo?.dep_codigo != departamento?.codigo) {
                                selectedCargo = null
                            }
                            departamentoError = null // Reset error on selection change
                            cargoError = null // Reset dependent error
                        },
                        departamentoError = departamentoError, // Pass error state

                        cargos = cargos, // Pass the list
                        selectedCargo = selectedCargo, // Pass current selection
                        onCargoSelected = { cargo -> selectedCargo = cargo; cargoError = null }, // Handle selection, Reset error on change
                        cargoError = cargoError, // Pass error state

                        fechaContratacion = fechaContratacion, onFechaContratacionChange = { fechaContratacion = it }, // UI Long
                        estado = estado, onEstadoChange = { estado = it } // UI Int
                        // TODO: Add validation for required fields like Fecha de Contratacion if needed
                    )
                }
            }


            // Mostrar error general del formulario si existe
            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Por favor, corrige los errores en el formulario.",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            // Buttons to navigate between steps and save
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Next/Save Button
                Button(
                    onClick = {
                        // Validate fields for the current step
                        if (validateFields()) {
                            if (currentStep < totalSteps) {
                                // Move to the next step if validation passes
                                currentStep++
                            } else {
                                // On the last step: gather data and trigger save via ViewModel
                                // Perform data formatting and conversions here before creating the Employee object

                                // Convert date timestamps (Long) to String? (YYYY-MM-DD)
                                // Use the ViewModel's function or a utility function if moved
                                val fNacimientoString = viewModel.convertTimestampToDateString(fechaNacimiento) // Or convertTimestampToDateString(fechaNacimiento) if moved
                                val fContratacionString = viewModel.convertTimestampToDateString(fechaContratacion) // Or convertTimestampToDateString(fechaContratacion) if moved

                                // Clean and convert phone numbers (String) to Int?
                                // Remove hyphens before converting to Int
                                val celularCleaned = celular.replace("-", "")
                                val celularInt = celularCleaned.toIntOrNull()

                                val telefonoCleaned = telefono.replace("-", "")
                                val telefonoInt = telefonoCleaned.toIntOrNull()

                            // Map Gender String to Int?
                            val generoInt = when (genero) {
                                "Masculino" -> 0
                                "Femenino" -> 1
                                else -> null // Handle empty or unknown string
                            }

                            // Map Estado Civil String to Int?
                            val estadoCivilInt = when (estadoCivil) {
                                "Soltero/a" -> 0
                                "Casado/a" -> 1
                                "Divorciado/a" -> 2
                                "Viudo/a" -> 3
                                else -> null // Handle empty or unknown string
                            }

                                // --- Extract prefijo, tomo, asiento from cedula ---
                                val cedulaParts = cedula.split("-")
                                val prefijoValue = cedulaParts.getOrNull(0).takeIf { !it.isNullOrEmpty() }
                                val tomoValue = cedulaParts.getOrNull(1).takeIf { !it.isNullOrEmpty() }
                                val asientoValue = cedulaParts.getOrNull(2).takeIf { !it.isNullOrEmpty() }
                                // Note: This assumes the format is always "prefijo-tomo-asiento".
                                // You might need more robust parsing if the format varies.

                                // --- Handle usa_ac ---
                                // Usamos directamente el valor de usaAc que ya tenemos como Int (0=No, 1=Sí)
                                // Solo es relevante para mujeres casadas o viudas.
                                val usaAcInt: Int? = if (genero == "Femenino" && (estadoCivil == "Casado/a" || estadoCivil == "Viudo/a")) {
                                    usaAc // Ya es un Int (0 o 1)
                                } else {
                                    null // Valor nulo si no es relevante
                                }

                                // Create the Employee object using the corrected property names and converted/cleaned types
                                val newEmployee = Employee(
                                    cedula = cedula, // String (NOT NULL) - Keep the original cedula string
                                    prefijo = prefijoValue, // Use extracted value
                                    tomo = tomoValue, // Use extracted value
                                    asiento = asientoValue, // Use extracted value
                                    nombre1 = primerNombre.ifEmpty { null }, // String?
                                    nombre2 = segundoNombre.ifEmpty { null }, // String?
                                    apellido1 = primerApellido.ifEmpty { null }, // String?
                                    apellido2 = segundoApellido.ifEmpty { null }, // String?
                                    apellidoc = apellidoCasado.ifEmpty { null }, // Use apellidoCasado
                                    genero = generoInt, // Use mapped Int?
                                    estadoCivil = estadoCivilInt, // Use mapped Int?
                                    tipoSangre = tipoSangre.ifEmpty { null }, // String?
                                    usaAc = usaAcInt, // Use collected/default value
                                    fechaNacimiento = fNacimientoString, // Converted Long -> String?
                                    celular = celularInt, // Use cleaned and converted Int?
                                    telefono = telefonoInt, // Use cleaned and converted Int?
                                    correo = email.ifEmpty { null }, // String?
                                    contrasena = null, // Do not include password here for security
                                    provincia = selectedProvincia?.codigo_provincia, // Use code from selected object
                                    distrito = selectedDistrito?.codigo_distrito, // Use code from selected object
                                    corregimiento = selectedCorregimiento?.codigo_corregimiento, // Use the 'codigo' field from Corregimiento
                                    calle = calle.ifEmpty { null }, // String?
                                    casa = casa.ifEmpty { null }, // String?
                                    comunidad = comunidad.ifEmpty { null }, // String?
                                    nacionalidad = selectedNacionalidad?.codigo, // Use code from selected object
                                    fechaContrato = fContratacionString, // Converted Long -> String?
                                    cargo = selectedCargo?.codigo, // Use code from selected object
                                    departamento = selectedDepartamento?.codigo, // Use code from selected object
                                    estado = estado // Int -> Int? (already matches well)
                                )

                                // Call the ViewModel function to save the employee
                                viewModel.saveEmployee(newEmployee)
                            }
                        } else {
                            // If validation fails, errorMessage is set and displayed
                        }
                    },
                    // Disable button while saving OR while options are loading
                    enabled = saveState != SaveState.Loading && !isLoadingOptions,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (saveState == SaveState.Loading) {
                        // Show loading indicator when saving
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(if (currentStep < totalSteps) "Siguiente" else "Guardar")
                    }
                }

                // Cancel/Back Button
                OutlinedButton(
                    onClick = {
                        if (currentStep > 1) {
                            // Go back to the previous step
                            currentStep--
                        } else {
                            // On the first step, cancel and go back
                            onBackClick()
                        }
                    },
                    // Disable button while saving OR while options are loading
                    enabled = saveState != SaveState.Loading && !isLoadingOptions,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (currentStep > 1) "Retroceder" else "Cancelar")
                }
            }
        }

        // Dialog to show success or error feedback
        if (showFeedbackDialog) {
            AlertDialog(
                onDismissRequest = {
                    showFeedbackDialog = false
                },
                title = { Text(feedbackTitle) },
                text = { Text(feedbackMessage) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showFeedbackDialog = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

// NOTE: The implementations of PersonalInfoComponent, AddressInfoComponent,
// ContactInfoComponent, and WorkInfoComponent are assumed to be correct
// based on previous corrections, accepting lists and callbacks for dropdowns
// and handling their own internal state/UI.
// The DropdownSelector helper composable is also assumed to be available.

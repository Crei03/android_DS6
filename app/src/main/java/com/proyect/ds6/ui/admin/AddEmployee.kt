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

// Import the utility function if you moved it (Optional)
// import com.proyect.ds6.utils.convertTimestampToDateString

// Assume these components are defined elsewhere in your ui.employee.components package
import com.proyect.ds6.ui.employee.components.PersonalInfoComponent
import com.proyect.ds6.ui.employee.components.AddressInfoComponent
import com.proyect.ds6.ui.employee.components.ContactInfoComponent
import com.proyect.ds6.ui.employee.components.WorkInfoComponent
import com.proyect.ds6.ui.admin.components.StepProgressIndicator // Assuming this is in ui.admin.components


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
    var password by remember { mutableStateOf("") } // **SECURITY WARNING: Handle passwords securely!**

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
                .verticalScroll(scrollState),
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
                        cedula = cedula, onCedulaChange = { cedula = it },
                        primerNombre = primerNombre, onPrimerNombreChange = { primerNombre = it },
                        segundoNombre = segundoNombre, onSegundoNombreChange = { segundoNombre = it },                        primerApellido = primerApellido, onPrimerApellidoChange = { primerApellido = it },
                        segundoApellido = segundoApellido, onSegundoApellidoChange = { segundoApellido = it },
                        fechaNacimiento = fechaNacimiento, onFechaNacimientoChange = { fechaNacimiento = it }, // UI Long
                        genero = genero, onGeneroChange = { genero = it }, // UI String
                        estadoCivil = estadoCivil, onEstadoCivilChange = { estadoCivil = it }, // UI String
                        tipoSangre = tipoSangre, onTipoSangreChange = { tipoSangre = it }, // UI String
                        usaAc = usaAc, onUsaAcChange = { usaAc = it }, // 0=No, 1=Sí
                        // Pass the list of nationalities and handle selection
                        nationalities = nationalities, // Pass the list
                        selectedNacionalidad = selectedNacionalidad, // Pass current selection
                        onNacionalidadSelected = { nacionalidad -> selectedNacionalidad = nacionalidad } // Handle selection
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
                        },
                        distritos = filteredDistritos, // Pass the filtered list
                        selectedDistrito = selectedDistrito, // Pass current selection
                        onDistritoSelected = { distrito ->
                            selectedDistrito = distrito // Update selected district
                            selectedCorregimiento = null // Reset corregimiento when district changes
                            viewModel.filterCorregimientosByDistrito(selectedProvincia?.codigo_provincia, distrito?.codigo_distrito) // Filter corregimientos
                        },
                        corregimientos = filteredCorregimientos, // Pass the filtered list
                        selectedCorregimiento = selectedCorregimiento, // Pass current selection
                        onCorregimientoSelected = { corregimiento -> selectedCorregimiento = corregimiento }, // Handle selection
                        calle = calle, onCalleChange = { calle = it }, // UI String
                        casa = casa, onCasaChange = { casa = it }, // UI String
                        comunidad = comunidad, onComunidadChange = { comunidad = it } // UI String
                    )
                    3 -> ContactInfoComponent(
                        celular = celular, onCelularChange = { celular = it }, // UI String
                        telefono = telefono, onTelefonoChange = { telefono = it }, // UI String
                        email = email, onEmailChange = { email = it }, // UI String
                        password = password, onPasswordChange = { password = it } // UI String **SECURITY WARNING**
                    )
                    4 -> WorkInfoComponent(
                        // Pass the lists of departments and cargos
                        departamentos = departamentos, // Pass the list
                        selectedDepartamento = selectedDepartamento, // Pass current selection
                        onDepartamentoSelected = { departamento -> selectedDepartamento = departamento }, // Handle selection
                        cargos = cargos, // Pass the list
                        selectedCargo = selectedCargo, // Pass current selection
                        onCargoSelected = { cargo -> selectedCargo = cargo }, // Handle selection
                        fechaContratacion = fechaContratacion, onFechaContratacionChange = { fechaContratacion = it }, // UI Long
                        estado = estado, onEstadoChange = { estado = it } // UI Int
                    )
                }
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
                        if (currentStep < totalSteps) {
                            // Move to the next step
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
                            val prefijoValue = cedulaParts.getOrNull(0).let { if (it.isNullOrEmpty()) null else it }
                            val tomoValue = cedulaParts.getOrNull(1).let { if (it.isNullOrEmpty()) null else it }
                            val asientoValue = cedulaParts.getOrNull(2).let { if (it.isNullOrEmpty()) null else it }                            // Note: This assumes the format is always "prefijo-tomo-asiento".
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
                                apellidoc = null, // Assuming not in UI
                                genero = generoInt, // Use mapped Int?
                                estadoCivil = estadoCivilInt, // Use mapped Int?
                                tipoSangre = tipoSangre.ifEmpty { null }, // String?
                                usaAc = usaAcInt, // Use collected/default value
                                fechaNacimiento = fNacimientoString, // Converted Long -> String?
                                celular = celularInt, // Use cleaned and converted Int?
                                telefono = telefonoInt, // Use cleaned and converted Int?
                                correo = email.ifEmpty { null }, // String?
                                contrasena = password.ifEmpty { null }, // String? **SECURITY WARNING**
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


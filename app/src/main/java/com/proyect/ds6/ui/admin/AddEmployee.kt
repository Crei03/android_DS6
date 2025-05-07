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
import com.proyect.ds6.ui.admin.components.StepProgressIndicator
import com.proyect.ds6.ui.employee.components.*

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployee(
    onBackClick: () -> Unit = {},
) {
    // Estados para todos los campos del formulario
    
    // Estados para información personal
    var cedula by remember { mutableStateOf("") }
    var primerNombre by remember { mutableStateOf("") }
    var segundoNombre by remember { mutableStateOf("") }
    var primerApellido by remember { mutableStateOf("") }
    var segundoApellido by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf(0L) }
    var genero by remember { mutableStateOf("") }
    var estadoCivil by remember { mutableStateOf("") }
    var tipoSangre by remember { mutableStateOf("") }

    // Estados para información de dirección
    var provincia by remember { mutableStateOf("") }
    var distrito by remember { mutableStateOf("") }
    var corregimiento by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var casa by remember { mutableStateOf("") }
    var comunidad by remember { mutableStateOf("") }

    // Estados para información de contacto
    var celular by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estados para información de trabajo
    var departamento by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("") }
    var fechaContratacion by remember { mutableStateOf(0L) }
    var estado by remember { mutableStateOf(1) }

    // Estado para controlar el paso actual del formulario
    var currentStep by remember { mutableStateOf(1) }
    val totalSteps = 4
    
    // Estado para controlar el color del TopAppBar según el scroll
    val scrollState = rememberScrollState()
    val topAppBarColor = if (scrollState.value > 0) {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
    } else {
        MaterialTheme.colorScheme.surface
    }
    
    // Títulos de los pasos
    val stepTitles = listOf(
        "Información Personal",
        "Información de Dirección",
        "Información de Contacto",
        "Información de Trabajo"
    )

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
            // Indicador de progreso
            StepProgressIndicator(
                currentStep = currentStep,
                totalSteps = totalSteps,
                stepTitle = stepTitles[currentStep - 1]
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Mostrar el componente según el paso actual
            when (currentStep) {
                1 -> PersonalInfoComponent(
                    cedula = cedula,
                    onCedulaChange = { cedula = it },
                    primerNombre = primerNombre,
                    onPrimerNombreChange = { primerNombre = it },
                    segundoNombre = segundoNombre,
                    onSegundoNombreChange = { segundoNombre = it },
                    primerApellido = primerApellido,
                    onPrimerApellidoChange = { primerApellido = it },
                    segundoApellido = segundoApellido,
                    onSegundoApellidoChange = { segundoApellido = it },
                    fechaNacimiento = fechaNacimiento,
                    onFechaNacimientoChange = { fechaNacimiento = it },
                    genero = genero,
                    onGeneroChange = { genero = it },
                    estadoCivil = estadoCivil,
                    onEstadoCivilChange = { estadoCivil = it },
                    tipoSangre = tipoSangre,
                    onTipoSangreChange = { tipoSangre = it }
                )
                2 -> AddressInfoComponent(
                    provincia = provincia,
                    onProvinciaChange = { provincia = it },
                    distrito = distrito,
                    onDistritoChange = { distrito = it },
                    corregimiento = corregimiento,
                    onCorregimientoChange = { corregimiento = it },
                    calle = calle,
                    onCalleChange = { calle = it },
                    casa = casa,
                    onCasaChange = { casa = it },
                    comunidad = comunidad,
                    onComunidadChange = { comunidad = it }
                )
                3 -> ContactInfoComponent(
                    celular = celular,
                    onCelularChange = { celular = it },
                    telefono = telefono,
                    onTelefonoChange = { telefono = it },
                    email = email,
                    onEmailChange = { email = it },
                    password = password,
                    onPasswordChange = { password = it }
                )
                4 -> WorkInfoComponent(
                    departamento = departamento,
                    onDepartamentoChange = { departamento = it },
                    cargo = cargo,
                    onCargoChange = { cargo = it },
                    fechaContratacion = fechaContratacion,
                    onFechaContratacionChange = { fechaContratacion = it },
                    estado = estado,
                    onEstadoChange = { estado = it }
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Botones para navegar entre pasos
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Siguiente/Guardar (arriba)
                Button(
                    onClick = {
                        if (currentStep < totalSteps) {
                            // Avanzar al siguiente paso
                            currentStep++
                        } else {
                            // En el último paso, guardar y salir
                            // Aquí iría la lógica para guardar los datos
                            onBackClick()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (currentStep < totalSteps) "Siguiente" else "Guardar")
                }
                
                // Botón Cancelar/Retroceder (abajo)
                OutlinedButton(
                    onClick = {
                        if (currentStep > 1) {
                            // Retroceder al paso anterior
                            currentStep--
                        } else {
                            // En el primer paso, cancelar y salir
                            onBackClick()
                        }
                    },
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
    }
}

package com.proyect.ds6.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyect.ds6.R
import com.proyect.ds6.data.repository.AppContainer
import com.proyect.ds6.presentation.AdminSaveState
import com.proyect.ds6.presentation.AdminViewModel
import com.proyect.ds6.presentation.AdminViewModelFactory
import com.proyect.ds6.ui.theme.DS6InteractiveElement
import com.proyect.ds6.ui.theme.DS6Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    onBackClick: () -> Unit = {},
    viewModel: AdminViewModel = viewModel(
        factory = AdminViewModelFactory(AppContainer.employeeRepository)
    )
) {
    // Estados para los campos del formulario
    var cedula by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    // Para manejar el foco entre campos
    val focusManager = LocalFocusManager.current
    
    // Observar el estado de guardado del ViewModel
    val saveState by viewModel.saveState.collectAsState()
    
    // Estados para mostrar feedback
    var showFeedbackDialog by remember { mutableStateOf(false) }
    var feedbackTitle by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    
    // Mostrar diálogo de feedback cuando cambia el estado
    LaunchedEffect(saveState) {
        when (saveState) {
            is AdminSaveState.Success -> {
                feedbackTitle = "Éxito"
                feedbackMessage = "El administrador se ha guardado correctamente"
                showFeedbackDialog = true
                // Limpiar los campos
                cedula = ""
                email = ""
                password = ""
            }
            is AdminSaveState.Error -> {
                feedbackTitle = "Error"
                feedbackMessage = (saveState as AdminSaveState.Error).message
                showFeedbackDialog = true
            }
            else -> { /* No action needed for other states */ }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Agregar administrador", 
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
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Espaciador superior
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 16.dp))
            
            // Campo de Cédula
            OutlinedTextField(
                value = cedula,
                onValueChange = { cedula = it },
                label = { Text("Cédula") },
                leadingIcon = { 
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Ícono de Cédula",
                        tint = DS6InteractiveElement
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            // Campo de Correo Electrónico
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                leadingIcon = { 
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Ícono de Email",
                        tint = DS6InteractiveElement
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = { 
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Ícono de Contraseña",
                        tint = DS6InteractiveElement
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) 
                                ImageVector.vectorResource(id = R.drawable.visibility_24px) 
                            else 
                                ImageVector.vectorResource(id = R.drawable.visibility_off_24px),
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = DS6InteractiveElement
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            // Espaciador
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
              // Botón para guardar (arriba)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Button(
                    onClick = { 
                        // Llamada al ViewModel para guardar el administrador
                        viewModel.saveAdmin(cedula, email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    // Deshabilitar el botón durante la carga
                    enabled = saveState != AdminSaveState.Loading
                ) {
                    if (saveState == AdminSaveState.Loading) {
                        // Mostrar indicador de carga
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Guardar")
                    }
                }
                
                // Botón de cancelar (abajo)
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth(),
                    // Deshabilitar el botón durante la carga
                    enabled = saveState != AdminSaveState.Loading
                ) {
                    Text("Cancelar")
                }
            }        }
        
        // Diálogo de feedback para mostrar éxito o error
        if (showFeedbackDialog) {
            AlertDialog(
                onDismissRequest = {
                    showFeedbackDialog = false
                    // Resetear el estado solo si es éxito
                    if (saveState is AdminSaveState.Success) {
                        viewModel.resetSaveState()
                    }
                },
                title = { Text(feedbackTitle) },
                text = { Text(feedbackMessage) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showFeedbackDialog = false
                            // Resetear el estado solo si es éxito
                            if (saveState is AdminSaveState.Success) {
                                viewModel.resetSaveState()
                            }
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddUserScreenPreview() {
    DS6Theme {
        AddUserScreen()
    }
}
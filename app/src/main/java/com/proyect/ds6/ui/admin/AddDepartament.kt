package com.proyect.ds6.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.proyect.ds6.R
import com.proyect.ds6.data.repository.EmployeeRepository
import com.proyect.ds6.db.supabase
import com.proyect.ds6.model.Departamento
import com.proyect.ds6.ui.theme.DS6InteractiveElement
import com.proyect.ds6.ui.theme.DS6Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDepartamentScreen(
    onBackClick: () -> Unit = {},
    onSaveUser: (String, String) -> Unit = { _, _ -> }
) {
    // Estados para los campos del formulario
    var departamento by remember { mutableStateOf("") }
    var nextDepartmentCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // Para manejar el foco entre campos
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val employeeRepository = remember { EmployeeRepository(supabase) }

    // Cargar departamentos existentes y calcular el siguiente código
    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            try {
                isLoading = true
                val result = withContext(Dispatchers.IO) {
                    employeeRepository.getDepartamentos()
                }
                
                if (result.isSuccess) {
                    val departamentos = result.getOrNull() ?: emptyList()
                    // Calcular el siguiente código
                    nextDepartmentCode = calculateNextDepartmentCode(departamentos)
                } else {
                    errorMessage = "Error al cargar departamentos: ${result.exceptionOrNull()?.message}"
                    // Establecer un valor predeterminado en caso de error
                    nextDepartmentCode = "01"
                }
            } catch (e: Exception) {
                errorMessage = "Error inesperado: ${e.message}"
                nextDepartmentCode = "01"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Agregar Departamento", 
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
              // El campo de código ya no se muestra en la interfaz
            // El código nextDepartmentCode se sigue generando en segundo plano
            
            // Campo de nombre de departamento
            OutlinedTextField(
                value = departamento,
                onValueChange = { departamento = it },
                label = { Text("Nombre de Departamento") },
                leadingIcon = { 
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.apartment_24px),
                        contentDescription = "Ícono de Departamento",
                        tint = DS6InteractiveElement
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            // Mostrar mensaje de error si existe
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
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
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { 
                            onSaveUser(nextDepartmentCode, departamento)
                            // Limpiamos el campo después de guardar
                            departamento = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        enabled = departamento.isNotEmpty() // Habilitar solo si hay un nombre
                    ) {
                        Text("Guardar")
                    }
                }
                
                // Botón de cancelar (abajo)
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}

/**
 * Calcula el siguiente código de departamento basado en los existentes.
 * Si el código más alto es "04", devolverá "05".
 */
private fun calculateNextDepartmentCode(departamentos: List<Departamento>): String {
    // Si no hay departamentos, empezamos con "01"
    if (departamentos.isEmpty()) {
        return "01"
    }
    
    // Encontrar el código numérico más alto
    val highestCode = departamentos.mapNotNull { it.codigo.toIntOrNull() }.maxOrNull() ?: 0
    
    // Incrementar y formatear con ceros a la izquierda
    val nextCode = highestCode + 1
    return String.format("%02d", nextCode)
}

@Preview(showBackground = true)
@Composable
fun AddDepartmentScreenPreview() {
    DS6Theme {
        AddDepartamentScreen()
    }
}
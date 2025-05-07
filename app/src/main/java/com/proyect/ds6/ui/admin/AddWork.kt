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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.proyect.ds6.R
import com.proyect.ds6.ui.theme.DS6InteractiveElement
import com.proyect.ds6.ui.theme.DS6Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkScreen(
    onBackClick: () -> Unit = {},
    onSaveUser: (Int, String) -> Unit = { _, _ -> }
) {
    // Estados para los campos del formulario
    var idDepartament by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("") }
    
    // Estado para controlar la expansión del menú desplegable
    var isExpanded by remember { mutableStateOf(false) }
    
    // Lista de departamentos disponibles
    val departamentos = listOf(
        "Recursos Humanos",
        "Tecnología",
        "Finanzas",
        "Marketing",
        "Operaciones",
        "Ventas",
        "Administración"
    )

    // Para manejar el foco entre campos
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Agregar Puesto de Trabajo", 
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
            
            // Campo desplegable de Departamento
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = departamento,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                    leadingIcon = { 
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.apartment_24px),
                            contentDescription = "Ícono de Departamento",
                            tint = DS6InteractiveElement
                        )
                    },
                    label = { Text("Departamento") },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    departamentos.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                departamento = opcion
                                isExpanded = false
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = cargo,
                onValueChange = { cargo = it },
                label = { Text("Cargo") },
                leadingIcon = { 
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.badge_24px),
                        contentDescription = "Ícono de Cargo",
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
                        onSaveUser(id.toIntOrNull() ?: 0, departamento)
                        // Limpiamos los campos después de guardar
                        idDepartament = ""
                        departamento = ""
                        id = ""
                        cargo = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text("Guardar")
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

@Preview(showBackground = true)
@Composable
fun AddWorkScreenPreview() {
    DS6Theme {
        AddWorkScreen()
    }
}
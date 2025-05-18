package com.proyect.ds6.ui.employee.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
// REMOVED: import androidx.compose.ui.res.painterResource
// REMOVED: import com.proyect.ds6.R // Assuming R is correctly imported for drawable resources
// REMOVED: import androidx.compose.ui.text.input.PasswordVisualTransformation
// REMOVED: import androidx.compose.ui.text.input.VisualTransformation


/**
 * Componente que muestra los campos para la información de contacto del empleado.
 * Ahora incluye parámetros para mostrar mensajes de error de validación y validación de entrada en tiempo real,
 * incluyendo límite de longitud.
 * NOTA DE SEGURIDAD: El campo de contraseña ha sido removido por motivos de seguridad.
 * El manejo de contraseñas debe realizarse en un flujo separado y seguro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactInfoComponent(
    celular: String,
    onCelularChange: (String) -> Unit,
    celularError: String? = null, // Added error parameter for celular

    telefono: String,
    onTelefonoChange: (String) -> Unit,
    telefonoError: String? = null, // Added error parameter for telefono

    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String? = null, // Added error parameter for email

    // REMOVED: password: String,
    // REMOVED: onPasswordChange: (String) -> Unit
) {
    // REMOVED: var passwordVisible by remember { mutableStateOf(false) } // No longer needed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            // Celular TextField
            OutlinedTextField(
                value = celular,
                onValueChange = { newValue ->
                    // Allow only digits and hyphens and limit length (using 15 as a reasonable max for phone numbers)
                    val filteredValue = newValue.filter { it.isDigit() || it == '-' }.take(15)
                    onCelularChange(filteredValue)
                },
                label = { Text("Celular") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), // Suggest phone keyboard
                isError = celularError != null, // Set isError based on the error state
                supportingText = { // Display error message if not null
                    if (celularError != null) {
                        Text(celularError)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Teléfono TextField
            OutlinedTextField(
                value = telefono,
                onValueChange = { newValue ->
                    // Allow only digits and hyphens and limit length (using 15 as a reasonable max for phone numbers)
                    val filteredValue = newValue.filter { it.isDigit() || it == '-' }.take(15)
                    onTelefonoChange(filteredValue)
                },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), // Suggest phone keyboard
                isError = telefonoError != null, // Set isError based on the error state
                supportingText = { // Display error message if not null
                    if (telefonoError != null) {
                        Text(telefonoError)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Correo Electrónico TextField
            OutlinedTextField(
                value = email,
                onValueChange = { newValue ->
                    // Limit length to 40 characters
                    val filteredValue = newValue.take(40)
                    onEmailChange(filteredValue)
                },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // Suggest email keyboard
                isError = emailError != null, // Set isError based on the error state
                supportingText = { // Display error message if not null
                    if (emailError != null) {
                        Text(emailError)
                    }
                }
            )

            // REMOVED: Password field and related logic have been removed for security reasons.
            // Handle password changes in a separate, secure flow.

        }
    }
}

package com.proyect.ds6.ui.auth

import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.ds6.R
import com.proyect.ds6.db.supabase
import com.proyect.ds6.model.Admin
import com.proyect.ds6.model.Employee
import com.proyect.ds6.ui.admin.HomeActivity
import com.proyect.ds6.ui.employee.EmployeeActivity
import com.proyect.ds6.ui.theme.DS6Primary
import com.proyect.ds6.ui.theme.DS6PrimaryDark
import com.proyect.ds6.ui.theme.DS6Theme
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Función para autenticar al usuario.
 * Primero intenta con la tabla de administradores, luego con la tabla de empleados.
 * Usa el cliente Supabase definido en db/supabase.kt
 */
private fun authenticateUser(
    cedula: String,
    password: String,
    onAdminSuccess: () -> Unit,
    onEmployeeSuccess: () -> Unit,
    onError: (String) -> Unit
) {

    // Log de las credenciales enviadas (evita esto en producción)
    Log.d("LoginDebug", "Cédula: $cedula, Contraseña: $password")
    // Usar un scope para la corutina
    CoroutineScope(Dispatchers.IO).launch {
        try {            // Primero buscar en la tabla de usuarios (administradores)
            val adminResult = try {
                supabase.from("usuarios")
                    .select(columns = Columns.list("cedula, contraseña")) {
                        filter {
                            eq("cedula", cedula)
                        }
                    }
                    .decodeSingleOrNull<Admin>()
            } catch (e: Exception) {
                null
            }

            if (adminResult != null && adminResult.contrasena == password) {
                withContext(Dispatchers.Main) {
                    onAdminSuccess()
                }
                return@launch
            }            // Si no está en la tabla de administradores, buscar en la tabla de empleados
            val employeeResult = try {
                supabase.from("empleados")
                    .select(columns = Columns.list("cedula, contraseña")) {
                        filter {
                            eq("cedula", cedula)
                        }
                    }
                    .decodeSingleOrNull<Employee>()
            } catch (e: Exception) {
                null
            }

            if (employeeResult != null && employeeResult.contrasena == password) {
                withContext(Dispatchers.Main) {
                    onEmployeeSuccess()
                }
                return@launch
            }

            // Si no se encontró en ninguna tabla
            withContext(Dispatchers.Main) {
                onError("Cédula o contraseña incorrecta")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("LoginError", "Error al autenticar: ${e.localizedMessage}")
                onError("Error al conectar con el servidor: ${e.localizedMessage}")
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val scrollState = rememberScrollState()
    var cedula by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoginEnabled by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Degradado para el fondo
    val gradientColors = listOf(
        DS6PrimaryDark.copy(alpha = 0.9f),  // Color oscuro arriba
        DS6Primary.copy(alpha = 0.6f)        // Color más claro abajo
    )

    // Validación para habilitar/deshabilitar el botón de login
    isLoginEnabled = cedula.length >= 1 && password.length >= 1

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors
                )
            )
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { },  // TopAppBar sin título
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent  // Fondo transparente
                    )
                )
            },
            containerColor = Color.Transparent  // Fondo transparente para el Scaffold
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título "FormAntro" animado, 20px debajo del TopAppBar
                AnimatedFormAntroTitle(modifier = Modifier.padding(top = 20.dp))
                
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = cedula,
                    onValueChange = { newValue ->
                        // Validar: solo números y hasta 3 guiones, no guiones consecutivos ni al principio
                        val isValid = newValue.all { it.isDigit() || it == '-' } && 
                                newValue.count { it == '-' } <= 2 && 
                                !newValue.contains("--") && 
                                !(newValue.isNotEmpty() && newValue[0] == '-') 
                        
                        if (isValid) {
                            cedula = newValue
                        }
                    },
                    label = { Text("Cédula") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible)
                                    ImageVector.vectorResource(id = R.drawable.visibility_24px)
                                else
                                    ImageVector.vectorResource(id = R.drawable.visibility_off_24px),
                                contentDescription = if (isPasswordVisible)
                                    "Ocultar contraseña"
                                else
                                    "Mostrar contraseña"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))                // Estado para mostrar mensajes de error
                var errorMessage by remember { mutableStateOf<String?>(null) }
                var isLoading by remember { mutableStateOf(false) }
                val context = LocalContext.current
                
                // Mostrar mensaje de error si existe
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                // Botón de inicio de sesión
                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        
                        // Validar credenciales
                        if (cedula.isBlank()) {
                            errorMessage = "Por favor, ingrese su cédula"
                            isLoading = false
                            return@Button
                        }
                        
                        if (password.isBlank()) {
                            errorMessage = "Por favor, ingrese su contraseña"
                            isLoading = false
                            return@Button
                        }
                                  // Intentar autenticación
                        authenticateUser(
                            cedula = cedula,
                            password = password,
                            onAdminSuccess = {
                                // Navegar a la pantalla de administrador
                                val intent = Intent(context, HomeActivity::class.java)
                                context.startActivity(intent)
                                (context as? ComponentActivity)?.finish()
                            },
                            onEmployeeSuccess = {
                                // Navegar a la pantalla de empleado
                                val intent = Intent(context, EmployeeActivity::class.java)
                                context.startActivity(intent)
                                (context as? ComponentActivity)?.finish()
                            },
                            onError = { message ->
                                errorMessage = message
                                isLoading = false
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(48.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    enabled = isLoginEnabled && !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Iniciar Sesión")
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedFormAntroTitle(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "FormAntro Animation")
    
    // Animación de escala
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Text(
        text = "FormAntro",
        style = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,  // Título en color blanco
            textAlign = TextAlign.Center
        ),
        modifier = modifier
            .scale(scale)
            .padding(horizontal = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    DS6Theme {
        LoginScreen()
    }
}
package com.proyect.ds6.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.proyect.ds6.ui.theme.DS6Primary
import com.proyect.ds6.ui.theme.DS6PrimaryDark
import com.proyect.ds6.ui.theme.DS6Theme

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
    isLoginEnabled = cedula.length >= 8 && password.length >= 6

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
                    onValueChange = {
                        // Solo permite números
                        if (it.all { char -> char.isDigit() } || it.isEmpty()) {
                            cedula = it
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
                        keyboardType = KeyboardType.Number,
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

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de inicio de sesión
                Button(
                    onClick = {
                        // Aquí iría la lógica de autenticación
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(48.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    enabled = isLoginEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Iniciar Sesión")
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
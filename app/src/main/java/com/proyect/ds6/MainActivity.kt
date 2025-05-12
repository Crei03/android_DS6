package com.proyect.ds6

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.proyect.ds6.ui.admin.HomeActivity
import com.proyect.ds6.ui.theme.DS6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DS6Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    // En este caso, simplemente redireccionamos a HomeActivity
    val context = LocalContext.current
    
    LaunchedEffect(key1 = true) {
        // Iniciamos HomeActivity y finalizamos MainActivity
        val intent = Intent(context, HomeActivity::class.java)
        context.startActivity(intent)
        (context as? ComponentActivity)?.finish()
    }
    
    // Mientras se realiza la redirección, podemos mostrar un indicador de carga
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
        )
    }
}
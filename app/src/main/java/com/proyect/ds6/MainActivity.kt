package com.proyect.ds6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proyect.ds6.model.staticEmployees
import com.proyect.ds6.ui.theme.Ds6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ds6Theme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "employeeList",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("employeeList") {
                            EmployeeListScreen(navController)
                        }
                        composable("addEmployee") {
                            AddEmployeeScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmployeeListScreen(navController: NavController, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Button(onClick = { navController.navigate("addEmployee") }) {
            Text("Agregar Empleado")
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(staticEmployees) { employee ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Nombre: ${employee.nombre1}", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Apellido: ${employee.apellido1}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Nacionalidad: ${employee.nacionalidad}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Departamento: ${employee.departamento}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Cargo: ${employee.cargo}", style = MaterialTheme.typography.bodySmall)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = { /* TODO: Ver más */ }, modifier = Modifier.padding(end = 8.dp)) {
                            Text("Ver más")
                        }
                        Button(onClick = { /* TODO: Editar */ }, modifier = Modifier.padding(end = 8.dp)) {
                            Text("Editar")
                        }
                        Button(
                            onClick = { /* TODO: Eliminar */ },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddEmployeeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Nuevo Empleado") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PersonalInfoSection()
            ContactInfoSection()
            AddressInfoSection()
            WorkInfoSection()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { /* TODO: Guardar */ }) {
                    Text("Guardar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(onClick = { navController.popBackStack() }) {
                    Text("Cancelar")
                }
            }
        }
    }
}

@Composable
fun PersonalInfoSection() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Información Personal", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Cédula") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Primer Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Segundo Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Primer Apellido") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Segundo Apellido") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Apellido de Casada") }, modifier = Modifier.fillMaxWidth())
            // Aquí puedes agregar selects para género, estado civil, tipo de sangre, nacionalidad, etc.
        }
    }
}

@Composable
fun ContactInfoSection() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Información de Contacto", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Celular") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun AddressInfoSection() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Dirección", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Provincia") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Distrito") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Corregimiento") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Calle") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Casa") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Comunidad") }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun WorkInfoSection() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Información Laboral", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Departamento") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Cargo") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Fecha de Contratación") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Estado") }, modifier = Modifier.fillMaxWidth())
        }
    }
}
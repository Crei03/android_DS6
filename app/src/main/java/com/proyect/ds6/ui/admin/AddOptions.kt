package com.proyect.ds6.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.vectorResource
import com.proyect.ds6.R
import com.proyect.ds6.ui.admin.components.InfoCard
import com.proyect.ds6.ui.theme.DS6InteractiveElement
import com.proyect.ds6.ui.theme.DS6Theme

data class AddOption(
    val id: String,
    val subtitle : String,
    val description: String,
    val icon: ImageVector,
    val iconTint: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOptionsScreen(
    onOptionSelected: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    selectedTabIndex: Int = 2,
    onTabSelected: (Int) -> Unit = {}
) {
    val options = listOf(
        AddOption(
            id = "add_employee",
            subtitle = "Empleado",
            description = "Agregar Empleado",
            icon = ImageVector.vectorResource(id = R.drawable.person_add_24px),
            iconTint = DS6InteractiveElement
        ),
        AddOption(
            id = "add_admin",
            subtitle = "Administrador",
            description = "Agregar Empleado",
            icon = ImageVector.vectorResource(id = R.drawable.person_add_24px),
            iconTint = DS6InteractiveElement
        ),
        AddOption(
            id = "add_department",
            subtitle = "Departamento",
            description = "Agregar Empleado",
            icon = ImageVector.vectorResource(id = R.drawable.apartment_24px),
            iconTint = DS6InteractiveElement
        ),
        AddOption(
            id = "add_position",
            subtitle = "Cargo",
            description = "Agregar Empleado",
            icon = ImageVector.vectorResource(id = R.drawable.badge_24px),
            iconTint = DS6InteractiveElement
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Opciones de Agregar",
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
        ) {
            Text(
                text = "Seleccione lo que desea agregar",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(options) { option ->
                    InfoCard(
                        icon = option.icon,
                        title = option.subtitle,
                        subtitle = option.description,
                        backgroundColor = Color.White, // Fondo blanco para todas las tarjetas
                        contentColor = Color.Black, // Texto en negro para contraste
                        iconTint = option.iconTint, // Mantenemos el color de los iconos
                        modifier = Modifier.fillMaxSize(),
                        onClick = { onOptionSelected(option.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddOptionsScreenPreview() {
    DS6Theme {
        AddOptionsScreen()
    }
}
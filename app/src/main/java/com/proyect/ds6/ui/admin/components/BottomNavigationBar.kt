package com.proyect.ds6.ui.admin.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.proyect.ds6.R

/**
 * Componente de barra de navegación inferior reutilizable para la aplicación
 */
@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        // Inicio
        NavigationItem(
            icon = Icons.Default.Home,
            label = "Inicio",
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        
        // Empleados - Usamos el icono personalizado groups_24px
        NavigationItem(
            icon = ImageVector.vectorResource(id = R.drawable.groups_24px),
            label = "Empleados",
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        
        // Botón de Agregar (en el centro)
        NavigationItem(
            icon = Icons.Default.Add,
            label = "Agregar",
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
        
        // Perfil
        NavigationItem(
            icon = Icons.Default.Person,
            label = "Perfil",
            selected = selectedTab == 4,
            onClick = { onTabSelected(4) }
        )
    }
}

@Composable
fun RowScope.NavigationItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        icon = { Icon(imageVector = icon, contentDescription = label) },
        label = { Text(text = label) },
        selected = selected,
        onClick = onClick
    )
}
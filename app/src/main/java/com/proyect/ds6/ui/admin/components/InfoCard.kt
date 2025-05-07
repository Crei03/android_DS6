package com.proyect.ds6.ui.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import com.proyect.ds6.ui.theme.DS6Theme

/**
 * Componente reutilizable de tarjeta de información
 * Muestra un icono, un título y un valor numérico
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoCard(
    icon: ImageVector,
    title: String,
    subtitle : String? = null,
    value: String? = null,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    onClick: (() -> Unit)? = null
) {
    val cardModifier = modifier
        .height(130.dp)
        .fillMaxWidth()
    
    if (onClick != null) {
        // Con Material3, usamos Card con soporte para clics
        Card(
            onClick = onClick,
            modifier = cardModifier,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp // Elevación cuando se presiona
            )
        ) {
            CardContent(
                icon = icon,
                title = title,
                subtitle = subtitle ,
                value = value ,
                contentColor = contentColor,
                iconTint = iconTint
            )
        }
    } else {
        // Versión no clicable de la tarjeta
        Card(
            modifier = cardModifier,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor,
                contentColor = contentColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            CardContent(
                icon = icon,
                title = title,
                value = value,
                contentColor = contentColor,
                iconTint = iconTint,
                subtitle = subtitle
            )
        }
    }
}

@Composable
private fun CardContent(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    value: String?,
    contentColor: Color,
    iconTint: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Icono en la parte superior
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(32.dp)
        )
        
        Spacer(modifier = Modifier.height(4.dp)) // Espacio separar el icono y el texto
        
        // Texto y valor en la parte inferior
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre el título y el subtítulo

            Text(
                text = subtitle ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.7f),
            )
            
            Text(
                text = value ?: "",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfoCardPreview() {
    DS6Theme {
        InfoCard(
            icon = Icons.Default.Person,
            title = "Total Empleados",
            value = "145",
            onClick = {}
        )
    }
}
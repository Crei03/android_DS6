package com.proyect.ds6.ui.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.proyect.ds6.ui.theme.*

/**
 * Modelo de datos para representar rangos de edad
 */
data class AgeRangeData(
    val label: String,  // Ejemplo: "18-35"
    val count: Int,
    val color: Color
)

/**
 * Componente para mostrar la distribución de empleados por rango de edad
 */
@Composable
fun AgeRangeDistribution(ageRanges: List<AgeRangeData>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título del componente
            Text(
                text = "Distribución por Edad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Distribución de barras horizontales
            ageRanges.forEach { ageRange ->
                AgeRangeBar(ageRange)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

/**
 * Barra individual para cada rango de edad
 */
@Composable
fun AgeRangeBar(ageRange: AgeRangeData) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Etiqueta del rango
            Text(
                text = ageRange.label,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.width(60.dp)
            )
            
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                // Barra con color específico
                LinearProgressIndicator(
                    progress = { 1f },  // Siempre completa, solo cambia el color
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    color = ageRange.color,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                
                // Número dentro de la barra - ahora usando un Box en vez de offset
                Text(
                    text = ageRange.count.toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgeRangeDistributionPreview() {
    val mockAgeRanges = listOf(
        AgeRangeData("18-35", 68, DS6Primary),    // Verde reemplazado por azul primario
        AgeRangeData("36-59", 52, DS6PrimaryLight), // Ámbar reemplazado por azul claro
        AgeRangeData("60+", 25, DS6InteractiveElement)  // Púrpura reemplazado por color interactivo
    )
    
    DS6Theme {
        AgeRangeDistribution(ageRanges = mockAgeRanges)
    }
}
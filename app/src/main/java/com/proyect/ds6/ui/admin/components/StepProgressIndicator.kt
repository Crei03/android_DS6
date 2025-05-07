package com.proyect.ds6.ui.admin.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * Componente que muestra un indicador de progreso circular con el paso actual
 * y el título correspondiente a ese paso
 */
@Composable
fun StepProgressIndicator(
    currentStep: Int,
    totalSteps: Int = 4,
    stepTitle: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo con progreso
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(48.dp)
            ) {
                // Círculo de fondo (gris)
                CircularProgressIndicator(
                    progress = 1f,
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    strokeWidth = 4.dp
                )
                
                // Círculo de progreso (verde/primario)
                CircularProgressIndicator(
                    progress = currentStep.toFloat() / totalSteps,
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
                
                // Texto dentro del círculo
                Text(
                    text = "$currentStep/$totalSteps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Título del paso actual
            Text(
                text = stepTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
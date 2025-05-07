package com.proyect.ds6.ui.admin.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyect.ds6.ui.theme.DS6Theme
import com.proyect.ds6.ui.theme.DS6Primary
import com.proyect.ds6.ui.theme.DS6Error


/**
 * Modelo de datos para representar distribución por género
 */
data class GenderData(
    val maleCount: Int,
    val femaleCount: Int,
    val maleColor: Color = DS6Primary, // Azul para masculino del tema
    val femaleColor: Color = DS6Error  // Rojo para femenino del tema
)

/**
 * Componente para mostrar la distribución de empleados por género
 */
@Composable
fun GenderDistribution(genderData: GenderData) {
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título del componente
            Text(
                text = "Distribución por Género",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Gráfico de pastel simple
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                SimplePieChart(genderData)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Leyenda
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                GenderLegendItem(
                    color = genderData.maleColor,
                    label = "Masculino",
                    count = genderData.maleCount
                )
                
                GenderLegendItem(
                    color = genderData.femaleColor,
                    label = "Femenino",
                    count = genderData.femaleCount
                )
            }
        }
    }
}

/**
 * Gráfico de pastel simple
 */
@Composable
fun SimplePieChart(genderData: GenderData) {
    val total = genderData.maleCount + genderData.femaleCount
    val maleSweepAngle = 360f * genderData.maleCount / total
    val femaleSweepAngle = 360f - maleSweepAngle
    
    Canvas(modifier = Modifier.size(180.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val radius = canvasWidth.coerceAtMost(canvasHeight) / 2
        val center = Offset(canvasWidth / 2, canvasHeight / 2)
        
        // Dibujar sector para masculino
        drawArc(
            color = genderData.maleColor,
            startAngle = 0f,
            sweepAngle = maleSweepAngle,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )
        
        // Dibujar sector para femenino
        drawArc(
            color = genderData.femaleColor,
            startAngle = maleSweepAngle,
            sweepAngle = femaleSweepAngle,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )
        
        // Borde del círculo
        drawCircle(
            color = Color.White,
            radius = radius,
            center = center,
            style = Stroke(width = 2f)
        )
    }
}

/**
 * Elemento de la leyenda para el gráfico de género
 */
@Composable
fun GenderLegendItem(color: Color, label: String, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Indicador de color
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color)
        )
        
        Spacer(modifier = Modifier.width(4.dp))
        
        // Texto de la leyenda
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GenderDistributionPreview() {
    val mockGenderData = GenderData(
        maleCount = 82,
        femaleCount = 63
    )
    
    DS6Theme {
        GenderDistribution(genderData = mockGenderData)
    }
}
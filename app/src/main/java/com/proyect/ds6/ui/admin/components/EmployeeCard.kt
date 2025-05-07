package com.proyect.ds6.ui.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyect.ds6.R
import com.proyect.ds6.ui.theme.DS6Error
import com.proyect.ds6.ui.theme.DS6Primary
import com.proyect.ds6.ui.theme.DS6Theme

/**
 * Componente que muestra la información de un empleado en formato de tarjeta.
 * Incluye datos personales, departamento, cargo, estado y acciones disponibles.
 *
 * @param nombre Nombre del empleado
 * @param apellido Apellido del empleado
 * @param cedula Número de cédula del empleado
 * @param departamento Departamento al que pertenece el empleado
 * @param cargo Cargo que ocupa el empleado
 * @param activo Estado actual del empleado (activo o inactivo)
 * @param isSelected Indica si la tarjeta está seleccionada actualmente
 * @param onClick Acción a ejecutar cuando la tarjeta es pulsada
 * @param onActiveChange Acción a ejecutar cuando se cambia el estado activo/inactivo
 * @param onView Acción a ejecutar cuando se pulsa el botón ver
 * @param onDelete Acción a ejecutar cuando se pulsa el botón eliminar
 */
@Composable
fun EmployeeCard(
    nombre: String,
    apellido: String,
    cedula: String,
    departamento: String,
    cargo: String,
    activo: Boolean = true,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onActiveChange: (Boolean) -> Unit = {},
    onView: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        )
    ) {
        // Indicador de selección en la parte superior
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna izquierda: Datos del empleado
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Nombre completo
                Text(
                    text = "$nombre $apellido",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Cédula
                Row {
                    Text(
                        text = "Cédula: ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = cedula,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                // Departamento
                Row {
                    Text(
                        text = "Departamento: ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = departamento,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Cargo
                Row {
                    Text(
                        text = "Cargo: ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = cargo,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            // Columna derecha: Estado y acciones
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Cinta de estado (activo/inactivo)
                var estadoActivo by remember { mutableStateOf(activo) }
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(
                            width = 1.dp,
                            color = if (estadoActivo) DS6Primary else DS6Error,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            estadoActivo = !estadoActivo
                            onActiveChange(estadoActivo)
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (estadoActivo) "Activo" else "Inactivo",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (estadoActivo) DS6Primary else DS6Error
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Botones de acción
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Botón Ver
                    IconButton(
                        onClick = onView,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.visibility_24px),
                            contentDescription = "Ver detalles",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    // Botón Eliminar
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar empleado",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmployeeCardPreview() {
    DS6Theme {
        Surface {
            Column {
                // Tarjeta no seleccionada
                EmployeeCard(
                    nombre = "Juan Carlos",
                    apellido = "Pérez Rodríguez",
                    cedula = "9-999-9999",
                    departamento = "Recursos Humanos",
                    cargo = "Especialista de Reclutamiento",
                    activo = true,
                    isSelected = false
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Tarjeta seleccionada e inactiva
                EmployeeCard(
                    nombre = "María José",
                    apellido = "González López",
                    cedula = "8-888-8888",
                    departamento = "Contabilidad",
                    cargo = "Contador Senior",
                    activo = false,
                    isSelected = true
                )
            }
        }
    }
}
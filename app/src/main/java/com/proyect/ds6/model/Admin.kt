package com.proyect.ds6.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Admin(
    // El ID del usuario, generado automáticamente por la base de datos
    // Aunque serial primary key suele ser NOT NULL, lo mantenemos como anulable por si acaso la consulta no lo incluye
    @SerialName("id")
    val id: Int? = null,

    // La cédula del usuario - Hacemos anulable para coincidir con DEFAULT NULL en la BD
    @SerialName("cedula")
    val cedula: String? = null,

    // La contraseña del usuario - **CRÍTICO: Hacemos anulable para coincidir con DEFAULT NULL en la BD**
    // Esto es lo más probable que cause el error si la contraseña es NULL en la BD
    @SerialName("contraseña")
    val contrasena: String? = null,

    // El correo institucional del usuario
    @SerialName("correo_institucional")
    val email: String? = null
)
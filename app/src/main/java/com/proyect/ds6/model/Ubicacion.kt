package com.proyect.ds6.model

import kotlinx.serialization.Serializable

@Serializable
data class Distrito(
    val codigo_provincia: String,
    val codigo_distrito: String,
    val codigo: String, // Assuming this is the district's own code within the province/district structure
    val nombre_distrito: String
)

@Serializable
data class Cargo(
    val dep_codigo: String? = null, // Assuming DEFAULT NULL maps to nullable String
    val codigo: String,
    val nombre: String? = null // Assuming DEFAULT NULL maps to nullable String
)

@Serializable
data class Corregimiento(
    val codigo_provincia: String,
    val codigo_distrito: String,
    val codigo: String, // Assuming this is the corregimiento's own code within the structure
    val codigo_corregimiento: String, // This seems redundant with the 'codigo' field, but following the schema
    val nombre_corregimiento: String
)


@Serializable
data class Departamento(
    val codigo: String,
    val nombre: String? = null // Assuming DEFAULT NULL maps to nullable String
)

@Serializable
data class Nacionalidad(
    val id: Int, // SERIAL PRIMARY KEY maps to Int
    val codigo: String? = null, // Assuming DEFAULT NULL maps to nullable String
    val prefijo: String? = null, // Assuming DEFAULT NULL maps to nullable String
    val pais: String? = null // Assuming DEFAULT NULL maps to nullable String
)

@Serializable
data class Provincia(
    val codigo_provincia: String,
    val nombre_provincia: String
)
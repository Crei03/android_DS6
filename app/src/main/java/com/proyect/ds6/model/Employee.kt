package com.proyect.ds6.model

// Modelo de datos para Employee basado en la clase PHP
// Puedes expandirlo según los campos y métodos necesarios

data class Employee(
    val id: Int? = null,
    val cedula: String = "",
    val prefijo: String = "",
    val tomo: String = "",
    val asiento: String = "",
    val nombre1: String = "",
    val nombre2: String = "",
    val apellido1: String = "",
    val apellido2: String = "",
    val apellidoc: String = "",
    val usaAc: Boolean = false,
    val genero: String = "",
    val estadoCivil: String = "",
    val tipoSangre: String = "",
    val fNacimiento: String = "",
    val nacionalidad: String = "",
    val celular: String = "",
    val telefono: String = "",
    val correo: String = "",
    val provincia: String = "",
    val distrito: String = "",
    val corregimiento: String = "",
    val calle: String = "",
    val casa: String = "",
    val comunidad: String = "",
    val departamento: String = "",
    val cargo: String = "",
    val fContra: String = "",
    val estado: String = ""
)

// Puedes crear una lista estática de empleados para pruebas
val staticEmployees = listOf(
    Employee(
        id = 1,
        cedula = "8-123-456",
        nombre1 = "Carlos",
        apellido1 = "Pérez",
        genero = "Masculino",
        estadoCivil = "Soltero/a",
        tipoSangre = "A+",
        fNacimiento = "1990-01-01",
        nacionalidad = "Panameña",
        celular = "60000000",
        correo = "carlos@empresa.com",
        departamento = "TI",
        cargo = "Desarrollador",
        estado = "Activo"
    ),
    Employee(
        id = 2,
        cedula = "4-567-890",
        nombre1 = "Ana",
        apellido1 = "Gómez",
        genero = "Femenino",
        estadoCivil = "Casado/a",
        tipoSangre = "O-",
        fNacimiento = "1985-05-10",
        nacionalidad = "Panameña",
        celular = "61234567",
        correo = "ana@empresa.com",
        departamento = "Recursos Humanos",
        cargo = "Jefa RRHH",
        estado = "Activo"
    )
)

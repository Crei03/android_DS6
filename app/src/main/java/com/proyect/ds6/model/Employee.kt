package com.proyect.ds6.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



// Data class que representa un empleado de la tabla "empleados" en Supabase
@Serializable
data class Employee(
    // La cédula del empleado, es la clave primaria y no puede ser nula
    @SerialName("cedula")
    val cedula: String, // NOT NULL en la BD

    // Prefijo del empleado
    @SerialName("prefijo")
    val prefijo: String? = null, // DEFAULT NULL en la BD

    // Tomo del empleado
    @SerialName("tomo")
    val tomo: String? = null, // DEFAULT NULL en la BD

    // Asiento del empleado
    @SerialName("asiento")
    val asiento: String? = null, // DEFAULT NULL en la BD

    // Primer nombre del empleado
    @SerialName("nombre1")
    val nombre1: String? = null, // DEFAULT NULL en la BD

    // Segundo nombre del empleado
    @SerialName("nombre2")
    val nombre2: String? = null, // DEFAULT NULL en la BD

    // Primer apellido del empleado
    @SerialName("apellido1")
    val apellido1: String? = null, // DEFAULT NULL en la BD

    // Segundo apellido del empleado
    @SerialName("apellido2")
    val apellido2: String? = null, // DEFAULT NULL en la BD

    // Apellido de casado del empleado
    @SerialName("apellidoc")
    val apellidoc: String? = null, // DEFAULT NULL en la BD

    // Género del empleado (usamos Int para smallint)
    @SerialName("genero")
    val genero: Int? = null, // DEFAULT NULL en la BD

    // Estado civil del empleado (usamos Int para smallint)
    @SerialName("estado_civil")
    val estadoCivil: Int? = null, // DEFAULT NULL en la BD

    // Tipo de sangre del empleado
    @SerialName("tipo_sangre")
    val tipoSangre: String? = null, // DEFAULT NULL en la BD

    // Indica si usa aire acondicionado (usamos Int para smallint)
    @SerialName("usa_ac")
    val usaAc: Int? = null, // DEFAULT NULL en la BD

    // Fecha de nacimiento (usamos String? para date con DEFAULT NULL)
    // Considera usar un tipo de fecha más específico si necesitas operaciones con fechas
    @SerialName("f_nacimiento")
    val fechaNacimiento: String? = null, // DEFAULT NULL en la BD

    // Número de celular (usamos Int? para integer con DEFAULT NULL)
    @SerialName("celular")
    val celular: Int? = null, // DEFAULT NULL en la BD

    // Número de teléfono (usamos Int? para integer con DEFAULT NULL)
    @SerialName("telefono")
    val telefono: Int? = null, // DEFAULT NULL en la BD

    // Correo electrónico del empleado
    @SerialName("correo")
    val correo: String? = null, // DEFAULT NULL en la BD

    // Contraseña del empleado
    // Nota: Es importante manejar contraseñas de forma segura (hashing) y no almacenarlas en texto plano.
    @SerialName("contraseña")
    val contrasena: String? = null, // DEFAULT NULL en la BD

    // Provincia (usamos String? para varchar con DEFAULT NULL)
    @SerialName("provincia")
    val provincia: String? = null, // DEFAULT NULL en la BD

    // Distrito (usamos String? para varchar con DEFAULT NULL)
    @SerialName("distrito")
    val distrito: String? = null, // DEFAULT NULL en la BD

    // Corregimiento (usamos String? para varchar con DEFAULT NULL)
    @SerialName("corregimiento")
    val corregimiento: String? = null, // DEFAULT NULL en la BD

    // Calle (usamos String? para varchar con DEFAULT NULL)
    @SerialName("calle")
    val calle: String? = null, // DEFAULT NULL en la BD

    // Casa (usamos String? para varchar con DEFAULT NULL)
    @SerialName("casa")
    val casa: String? = null, // DEFAULT NULL en la BD

    // Comunidad (usamos String? para varchar con DEFAULT NULL)
    @SerialName("comunidad")
    val comunidad: String? = null, // DEFAULT NULL en la BD

    // Nacionalidad (usamos String? para varchar con DEFAULT NULL)
    @SerialName("nacionalidad")
    val nacionalidad: String? = null, // DEFAULT NULL en la BD

    // Fecha de contratación (usamos String? para date con DEFAULT NULL)
    // Considera usar un tipo de fecha más específico si necesitas operaciones con fechas
    @SerialName("f_contra")
    val fechaContrato: String? = null, // DEFAULT NULL en la BD

    // Cargo (usamos String? para varchar con DEFAULT NULL)
    @SerialName("cargo")
    val cargo: String? = null, // DEFAULT NULL en la BD

    // Departamento (usamos String? para varchar con DEFAULT NULL)
    @SerialName("departamento")
    val departamento: String? = null, // DEFAULT NULL en la BD

    // Estado (usamos Int? para smallint con DEFAULT NULL)
    @SerialName("estado")
    val estado: Int? = null // DEFAULT NULL en la BD
)


// Puedes crear una lista estática de empleados para pruebas
val staticEmployees = listOf(
    Employee(
        cedula = "8-123-456",
        nombre1 = "Carlos",
        apellido1 = "Pérez",
        genero = 1, // Masculino
        estadoCivil = 1, // Soltero/a
        tipoSangre = "A+",
        fechaNacimiento = "1990-01-01",
        nacionalidad = "Panameña",
        celular = 60000000,
        correo = "carlos@empresa.com",
        departamento = "TI",
        cargo = "Desarrollador",
        estado = 1 // Activo
    ),
    Employee(
        cedula = "4-567-890",
        nombre1 = "Ana",
        apellido1 = "Gómez",
        genero = 2, // Femenino
        estadoCivil = 2, // Casado/a
        tipoSangre = "O-",
        fechaNacimiento = "1985-05-10",
        nacionalidad = "Panameña",
        celular = 61234567,
        correo = "ana@empresa.com",
        departamento = "Recursos Humanos",
        cargo = "Jefa RRHH",
        estado = 1 // Activo
    )
)
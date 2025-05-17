package com.proyect.ds6.data.repository

import com.proyect.ds6.db.supabase // Import the Supabase instance

// Nota: Este contenedor de dependencias se mantiene por compatibilidad con el código existente.
// Para nuevos desarrollos, prefiere usar la inyección de dependencias de Koin configurada en el módulo AppModule.
@Deprecated("Utilizar inyección de dependencias de Koin en lugar de este contenedor")
object AppContainer {
    // Provides an instance of EmployeeRepository, injecting the Supabase client.
    val employeeRepository = EmployeeRepository(supabase)

    // Add other dependencies here if you need them
    // val otherRepository = OtherRepository(...)
}
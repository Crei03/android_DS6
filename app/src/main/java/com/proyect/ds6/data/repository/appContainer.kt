package com.proyect.ds6.data.repository

import com.proyect.ds6.db.supabase // Import the Supabase instance

// Simple object acting as a dependency container.
// It provides instances of repositories or other dependencies to the presentation layer.
// In a larger application, using a dependency injection library like Hilt or Koin is recommended.
object AppContainer {
    // Provides an instance of EmployeeRepository, injecting the Supabase client.
    val employeeRepository = EmployeeRepository(supabase)

    // Add other dependencies here if you need them
    // val otherRepository = OtherRepository(...)
}
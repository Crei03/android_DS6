package com.proyect.ds6.di

import com.proyect.ds6.data.repository.EmployeeRepository
import com.proyect.ds6.db.supabase
import com.proyect.ds6.presentation.AdminViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// Módulo de Koin para la inyección de dependencias
val appModule = module {
    // Singleton del repositorio de empleados
    single { EmployeeRepository(supabase) }
    
    // ViewModel para la administración
    viewModel { AdminViewModel(get()) }
}

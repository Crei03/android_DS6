package com.proyect.ds6.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.proyect.ds6.data.repository.EmployeeRepository
import com.proyect.ds6.model.Admin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Factory para AdminViewModel
class AdminViewModelFactory(private val repository: EmployeeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// Estados posibles para la operación de guardado
sealed class AdminSaveState {
    object Idle : AdminSaveState()
    object Loading : AdminSaveState()
    object Success : AdminSaveState()
    data class Error(val message: String) : AdminSaveState()
}

// ViewModel para la pantalla de Admin
class AdminViewModel(private val repository: EmployeeRepository) : ViewModel() {
    
    // Estado de la operación de guardado
    private val _saveState = MutableStateFlow<AdminSaveState>(AdminSaveState.Idle)
    val saveState: StateFlow<AdminSaveState> = _saveState

    /**
     * Guarda un nuevo usuario administrador en la base de datos
     * @param cedula Cédula del administrador
     * @param email Correo electrónico del administrador
     * @param contrasena Contraseña del administrador
     */
    fun saveAdmin(cedula: String, email: String, contrasena: String) {
        // Validación básica de datos
        if (cedula.isBlank() || contrasena.isBlank()) {
            _saveState.value = AdminSaveState.Error("La cédula y la contraseña son obligatorias")
            return
        }

        // Actualizar estado a "cargando"
        _saveState.value = AdminSaveState.Loading

        viewModelScope.launch {
            try {
                // Crear el objeto Admin
                val admin = Admin(
                    cedula = cedula,
                    email = email.takeIf { it.isNotBlank() },
                    contrasena = contrasena
                )

                // Llamar al repositorio para guardar el admin
                val result = repository.addUser(admin)

                // Actualizar el estado según el resultado
                _saveState.value = if (result.isSuccess) {
                    AdminSaveState.Success
                } else {
                    AdminSaveState.Error(result.exceptionOrNull()?.message ?: "Error desconocido al guardar el usuario")
                }
            } catch (e: Exception) {
                _saveState.value = AdminSaveState.Error("Error: ${e.message}")
            }
        }
    }

    // Resetea el estado de guardado a Idle
    fun resetSaveState() {
        _saveState.value = AdminSaveState.Idle
    }
}

package com.proyect.ds6.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.proyect.ds6.data.repository.EmployeeRepository // Import the Repository
import com.proyect.ds6.model.* // Import all your data classes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
// Imports for date/time conversion
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
// Depending on how you want to format the date to String, you might need:
// import java.text.SimpleDateFormat // If using java.text
// import java.util.Date // If using java.util.Date

import kotlinx.datetime.toLocalDateTime


// Factory for AddEmployeeViewModel that takes the repository as a dependency
class AddEmployeeViewModelFactory(private val repository: EmployeeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEmployeeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // Necessary boilerplate for ViewModel factories
            return AddEmployeeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



// Sealed class defining the possible states of a save operation (or similar).
sealed class SaveState {
    // Initial state or after an operation has finished and been handled.
    object Idle : SaveState()
    // State while the save operation is in progress.
    object Loading : SaveState()
    // State when the save operation completed successfully.
    object Success : SaveState()
    // State when the save operation failed, containing an error message.
    data class Error(val message: String) : SaveState()
}





// ViewModel for the Add Employee screen.
// It is responsible for handling the UI logic and state related to adding employees.
class AddEmployeeViewModel(private val repository: EmployeeRepository) : ViewModel() {

    // StateFlow privado mutable para actualizar el estado de guardado internamente.
    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    // StateFlow público de solo lectura que la UI puede observar.
    val saveState: StateFlow<SaveState> = _saveState

    // --- StateFlows para las listas de opciones ---
    private val _nationalities = MutableStateFlow<List<Nacionalidad>>(emptyList())
    val nationalities: StateFlow<List<Nacionalidad>> = _nationalities

    private val _provinces = MutableStateFlow<List<Provincia>>(emptyList())
    val provinces: StateFlow<List<Provincia>> = _provinces

    // Guardamos la lista completa de distritos y corregimientos para filtrar localmente
    private val _allDistritos = MutableStateFlow<List<Distrito>>(emptyList())
    private val _allCorregimientos = MutableStateFlow<List<Corregimiento>>(emptyList())

    // StateFlows para las listas filtradas que la UI observará
    private val _filteredDistritos = MutableStateFlow<List<Distrito>>(emptyList())
    val filteredDistritos: StateFlow<List<Distrito>> = _filteredDistritos

    private val _filteredCorregimientos = MutableStateFlow<List<Corregimiento>>(emptyList())
    val filteredCorregimientos: StateFlow<List<Corregimiento>> = _filteredCorregimientos

    private val _cargos = MutableStateFlow<List<Cargo>>(emptyList())
    val cargos: StateFlow<List<Cargo>> = _cargos

    private val _departamentos = MutableStateFlow<List<Departamento>>(emptyList())
    val departamentos: StateFlow<List<Departamento>> = _departamentos

    // StateFlow para indicar si las listas de opciones están cargando
    private val _isLoadingOptions = MutableStateFlow(false)
    val isLoadingOptions: StateFlow<Boolean> = _isLoadingOptions

    // StateFlow para manejar errores al cargar las listas de opciones
    private val _optionsError = MutableStateFlow<String?>(null)
    val optionsError: StateFlow<String?> = _optionsError


    init {
        // Al inicializar el ViewModel, cargamos todas las listas de opciones
        loadOptions()
    }

    /**
     * Carga todas las listas de opciones (Nacionalidades, Provincias, etc.) desde el repositorio.
     */
    private fun loadOptions() {
        viewModelScope.launch {
            _isLoadingOptions.value = true
            _optionsError.value = null // Reset error state

            // Cargar Nacionalidades
            val nationalitiesResult = repository.getNacionalidades()
            if (nationalitiesResult.isSuccess) {
                _nationalities.value = nationalitiesResult.getOrThrow().sortedBy { it.pais }
            } else {
                _optionsError.value = nationalitiesResult.exceptionOrNull()?.message ?: "Error al cargar nacionalidades"
            }

            // Cargar Provincias
            val provincesResult = repository.getProvincias()
            if (provincesResult.isSuccess) {
                _provinces.value = provincesResult.getOrThrow().sortedBy { it.nombre_provincia }
            } else {
                _optionsError.value = provincesResult.exceptionOrNull()?.message ?: "Error al cargar provincias"
            }

            // Cargar Distritos (lista completa)
            val distritosResult = repository.getDistritos()
            if (distritosResult.isSuccess) {
                _allDistritos.value = distritosResult.getOrThrow().sortedBy { it.nombre_distrito }
            } else {
                _optionsError.value = distritosResult.exceptionOrNull()?.message ?: "Error al cargar distritos"
            }

            // Cargar Corregimientos (lista completa)
            val corregimientosResult = repository.getCorregimientos()
            if (corregimientosResult.isSuccess) {
                _allCorregimientos.value = corregimientosResult.getOrThrow().sortedBy { it.nombre_corregimiento }
            } else {
                _optionsError.value = corregimientosResult.exceptionOrNull()?.message ?: "Error al cargar corregimientos"
            }

            // Cargar Cargos
            val cargosResult = repository.getCargos()
            if (cargosResult.isSuccess) {
                _cargos.value = cargosResult.getOrThrow().sortedBy { it.nombre }
            } else {
                _optionsError.value = cargosResult.exceptionOrNull()?.message ?: "Error al cargar cargos"
            }

            // Cargar Departamentos
            val departamentosResult = repository.getDepartamentos()
            if (departamentosResult.isSuccess) {
                _departamentos.value = departamentosResult.getOrThrow().sortedBy { it.nombre }
            } else {
                _optionsError.value = departamentosResult.exceptionOrNull()?.message ?: "Error al cargar departamentos"
            }

            _isLoadingOptions.value = false // Terminó la carga
        }
    }

    /**
     * Filtra la lista de distritos basada en la provincia seleccionada.
     * @param provinciaCodigo El código de la provincia seleccionada.
     */
    fun filterDistritosByProvincia(provinciaCodigo: String?) {
        _filteredDistritos.value = if (provinciaCodigo.isNullOrEmpty()) {
            emptyList()
        } else {
            _allDistritos.value.filter { it.codigo_provincia == provinciaCodigo }
        }
        // Reset corregimientos when province changes
        _filteredCorregimientos.value = emptyList()
    }

    /**
     * Filtra la lista de corregimientos basada en la provincia y distrito seleccionados.
     * @param provinciaCodigo El código de la provincia seleccionada.
     * @param distritoCodigo El código del distrito seleccionado.
     */
    fun filterCorregimientosByDistrito(provinciaCodigo: String?, distritoCodigo: String?) {
        _filteredCorregimientos.value = if (provinciaCodigo.isNullOrEmpty() || distritoCodigo.isNullOrEmpty()) {
            emptyList()
        } else {
            _allCorregimientos.value.filter { it.codigo_provincia == provinciaCodigo && it.codigo_distrito == distritoCodigo }
        }
    }


    /**
     * Triggers the process to save an employee to the database.
     * Updates the saveState based on the result of the repository operation.
     * @param employee The Employee object to save.
     */
    fun saveEmployee(employee: Employee) {
        // Launch a coroutine in the ViewModel's scope
        viewModelScope.launch {
            _saveState.value = SaveState.Loading // Set state to loading
            val result = repository.insertEmployee(employee) // Call the repository function
            _saveState.value = if (result.isSuccess) {
                SaveState.Success // Set state to success on success
            } else {
                // Set state to error on failure, extracting error message
                SaveState.Error(result.exceptionOrNull()?.message ?: "Error desconocido al guardar")
            }
        }
    }

    /**
     * Converts a timestamp (Long) from the UI (e.g., from a DatePicker) to a String
     * in "YYYY-MM-DD" format, suitable for your Employee data class.
     * This function is now a utility function, consider moving it to a separate file.
     * @param timestamp The timestamp in milliseconds.
     * @return A date String in "YYYY-MM-DD" format or null if the timestamp is 0L or invalid.
     */
    // **NOTE:** As discussed, this function could be moved to a separate utility file.
    fun convertTimestampToDateString(timestamp: Long): String? {
        return try {
            if (timestamp == 0L) return null // Handle the case where no date was selected

            // Convert timestamp to Instant and then to LocalDateTime in the system time zone.
            val localDateTime = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.currentSystemDefault())

            // Format to "YYYY-MM-DD" String.
            val year = localDateTime.year
            val month = String.format("%02d", localDateTime.monthNumber) // Ensure 2 digits
            val day = String.format("%02d", localDateTime.dayOfMonth) // Ensure 2 digits

            "$year-$month-$day" // Return the string in "YYYY-MM-DD" format

        } catch (e: Exception) {
            e.printStackTrace()
            null // Return null in case of error
        }
    }


    /**
     * Resets the save state to Idle.
     * Useful after the UI has handled a Success or Error state (ej: cerrando un diálogo).
     */
    fun resetSaveState() {
        _saveState.value = SaveState.Idle
    }

    /**
     * Resets the options error state.
     */
    fun resetOptionsError() {
        _optionsError.value = null
    }
}
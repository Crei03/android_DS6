package com.proyect.ds6.data.repository

import com.proyect.ds6.model.* // Import all your data classes
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

// Repository responsible for employee-related data operations and fetching lookup data
class EmployeeRepository(private val supabaseClient: SupabaseClient) {

    /**
     * Inserts a new employee into the 'empleados' table in Supabase.
     * @param employee The Employee object to insert.
     * @return Result<Unit> indicating success or failure.
     */
    suspend fun insertEmployee(employee: Employee): Result<Unit> {
        return try {
            // Access the 'empleados' table and perform the insertion.
            supabaseClient.postgrest["empleados"].insert(employee)
            Result.success(Unit) // Indicate that the operation was successful
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Fetches the list of users from the 'usuarios' table in Supabase.
     * @return Result<List<Admin>> indicating success or failure.
     */
    // funcion para agregar usuarios (admin)
    suspend fun addUser(admin: Admin): Result<Unit> {
        return try {
            supabaseClient.postgrest["usuarios"].insert(admin)
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Fetches the list of Nacionalidades from the database.
     * @return Result<List<Nacionalidad>> indicating success or failure.
     */
    suspend fun getNacionalidades(): Result<List<Nacionalidad>> {
        return try {
            val data = supabaseClient.postgrest["nacionalidad"].select().decodeList<Nacionalidad>()
            Result.success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Fetches the list of Provincias from the database.
     * @return Result<List<Provincia>> indicating success or failure.
     */
    suspend fun getProvincias(): Result<List<Provincia>> {
        return try {
            val data = supabaseClient.postgrest["provincia"].select().decodeList<Provincia>()
            Result.success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Fetches the list of Distritos from the database.
     * @return Result<List<Distrito>> indicating success or failure.
     */
    suspend fun getDistritos(): Result<List<Distrito>> {
        return try {
            val data = supabaseClient.postgrest["distrito"].select().decodeList<Distrito>()
            Result.success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Fetches the list of Corregimientos from the database.
     * @return Result<List<Corregimiento>> indicating success or failure.
     */
    suspend fun getCorregimientos(): Result<List<Corregimiento>> {
        return try {
            val data = supabaseClient.postgrest["corregimiento"].select().decodeList<Corregimiento>()
            Result.success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Fetches the list of Cargos from the database.
     * @return Result<List<Cargo>> indicating success or failure.
     */
    suspend fun getCargos(): Result<List<Cargo>> {
        return try {
            val data = supabaseClient.postgrest["cargo"].select().decodeList<Cargo>()
            Result.success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Fetches the list of Departamentos from the database.
     * @return Result<List<Departamento>> indicating success or failure.
     */
    suspend fun getDepartamentos(): Result<List<Departamento>> {
        return try {
            val data = supabaseClient.postgrest["departamento"].select().decodeList<Departamento>()
            Result.success(data)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Inserta un nuevo departamento en la base de datos.
     * @param codigo El código del departamento (por ejemplo, "01").
     * @param nombre El nombre del departamento.
     * @return Result<Unit> indicando éxito o fracaso.
     */
    suspend fun insertDepartamento(codigo: String, nombre: String): Result<Unit> {
        return try {
            val departamento = Departamento(codigo = codigo, nombre = nombre)
            supabaseClient.postgrest["departamento"].insert(departamento)
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Inserta un nuevo cargo en la base de datos.
     * @param codigo El código completo del cargo (dep_codigo-XX).
     * @param depCodigo El código del departamento al que pertenece.
     * @param nombre El nombre del cargo.
     * @return Result<Unit> indicando éxito o fracaso.
     */
    suspend fun insertCargo(codigo: String, depCodigo: String, nombre: String): Result<Unit> {
        return try {
            val cargo = Cargo(
                codigo = codigo,
                dep_codigo = depCodigo,
                nombre = nombre
            )
            supabaseClient.postgrest["cargo"].insert(cargo)
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Obtiene todos los empleados de la tabla 'empleados'.
     * @return Result<List<Employee>> con la lista de empleados en caso de éxito o un error en caso de fallo.
     */    
    suspend fun getAllEmployees(): Result<List<Employee>> {
        return try {
            val employees = supabaseClient.postgrest["empleados"].select().decodeList<Employee>()
            Result.success(employees)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Elimina un empleado usando el procedimiento almacenado 'eliminar_empleado'.
     * @param cedula La cédula del empleado que se va a eliminar.
     * @return Result<Unit> indicando éxito o fracaso.
     */
    suspend fun deleteEmployee(cedula: String): Result<Unit> {
        return try {
            // Crear objeto para la solicitud RPC
            val rpcParams = DeleteEmployeeRequest(cedula)

            // Convertir el objeto a JsonObject usando serialización
            val jsonObject = Json.encodeToJsonElement(rpcParams).jsonObject

            // Llamar al procedimiento RPC con el formato correcto
            supabaseClient.postgrest.rpc("eliminar_empleado", jsonObject)

            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }    

    /**
     * Obtiene un empleado específico por su cédula.
     * @param cedula La cédula del empleado a buscar.
     * @return Result<Employee?> con el empleado encontrado o null si no se encuentra.
     */
    suspend fun reinstateEmployee(cedula: String): Result<Unit>  {
        return try {
            val rpcParams = ReinstateEmployeeRequest(cedula)
            val jsonObject = Json.encodeToJsonElement(rpcParams).jsonObject

            supabaseClient.postgrest.rpc("reintegrar_empleado", jsonObject)

            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
        
    /**
     * Obtiene todos los empleados eliminados de la tabla 'e_eliminados'.
     * @return Result<List<Employee>> con la lista de empleados eliminados en caso de éxito o un error en caso de fallo.
     */    
    suspend fun getDeletedEmployees(): Result<List<Employee>> {
        return try {
            val employees = supabaseClient.postgrest["e_eliminados"].select().decodeList<Employee>()
            Result.success(employees)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun updateEmployeeStatus(cedula: String, estado: Int): Result<Unit> {
        return try {
            val updates = mapOf("estado" to estado)
            supabaseClient.postgrest.from("empleados")
                .update(updates) {
                    filter {
                        eq("cedula", cedula)
                    }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Obtiene todos los empleados aplicando filtros opcionales.
     * @param estadoFilter El estado por el cual filtrar (1 = activo, 0 = inactivo, null = todos)
     * @param departamentoFilter El código de departamento por el cual filtrar (null = todos)
     * @param cargoFilter El código de cargo por el cual filtrar (null = todos)
     * @param searchTerm Término para buscar en nombres, apellidos o cédula (null = sin búsqueda)
     * @return Result<List<Employee>> con la lista filtrada de empleados en caso de éxito o un error en caso de fallo.
     */    suspend fun getFilteredEmployees(
        estadoFilter: Int? = null,
        departamentoFilter: String? = null,
        cargoFilter: String? = null,
        searchTerm: String? = null
    ): Result<List<Employee>> {
        return try {
            val query = supabaseClient.postgrest.from("empleados").select {
                // Aplicar filtros dentro del bloque
                filter {
                    // Filtro por estado
                    if (estadoFilter != null) {
                        eq("estado", estadoFilter.toString())
                    }
                    
                    // Filtro por departamento
                    if (departamentoFilter != null) {
                        eq("departamento", departamentoFilter)
                    }
                    
                    // Filtro por cargo
                    if (cargoFilter != null) {
                        eq("cargo", cargoFilter)
                    }
                    
                    // Búsqueda en múltiples campos
                    if (!searchTerm.isNullOrBlank()) {
                        or {
                            ilike("nombre1", "%$searchTerm%")
                            ilike("nombre2", "%$searchTerm%")
                            ilike("apellido1", "%$searchTerm%")
                            ilike("apellido2", "%$searchTerm%")
                            ilike("cedula", "%$searchTerm%")
                        }
                    }
                }
            }
            
            val employees = query.decodeList<Employee>()
            Result.success(employees)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}
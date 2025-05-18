package com.proyect.ds6.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.proyect.ds6.model.* // Import all your data classes
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import java.time.LocalDate
import java.time.Period

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
     * Updates an existing employee in the 'empleados' table in Supabase.
     * The employee is identified by their 'cedula'.
     * @param employee The Employee object with updated data.
     * @return Result<Unit> indicating success or failure.
     */
    suspend fun updateEmployee(employee: Employee): Result<Unit> {
        return try {
            // Access the 'empleados' table and perform the update operation.
            // The 'eq("cedula", employee.cedula)' clause specifies which row to update.
            supabaseClient.postgrest["empleados"].update(employee) {
                filter {
                    eq("cedula", employee.cedula)
                }
            }
            Result.success(Unit) // Indicate that the operation was successful
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
     * Obtiene un empleado específico por su cédula.
     * @param cedula La cédula del empleado a buscar.
     * @return Result<Employee?> con el empleado encontrado o null si no se encuentra.
     */
    suspend fun getEmployee(cedula: String): Result<Employee?> {
        return try {
            // Select a single employee where the 'cedula' matches the provided cedula.
            // using singleOrNull() to get a single result or null if not found.
            val employee = supabaseClient.postgrest["empleados"].select {
                filter {
                    eq("cedula", cedula)
                }
            }.decodeSingleOrNull<Employee>()
            Result.success(employee)
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
     * Reintegra un empleado usando el procedimiento almacenado 'reintegrar_empleado'.
     * @param cedula La cédula del empleado que se va a reintegrar.
     * @return Result<Unit> indicando éxito o fracaso.
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
    }    /**
     * Obtiene todos los empleados aplicando filtros opcionales.
     * @param estadoFilter El estado por el cual filtrar (1 = activo, 0 = inactivo, null = todos)
     * @param searchTerm Término para buscar en nombres, apellidos o cédula (null = sin búsqueda)
     * @return Result<List<Employee>> con la lista filtrada de empleados en caso de éxito o un error en caso de fallo.
     */suspend fun getFilteredEmployees(
        estadoFilter: Int? = null,
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



    /**
     * Obtiene conteo de empleados por estado
     * @return Result<Map<String, Int>> con conteo de empleados: activos, inactivos, total
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getEmployeeStats(): Result<Map<String, Int>> {
        return try {
            val allEmployees = getAllEmployees().getOrDefault(emptyList())
            val totalCount = allEmployees.size
            val activeCount = allEmployees.count { it.estado == 1 }
            val inactiveCount = allEmployees.count { it.estado == 0 }
            
            val deletedEmployees = getDeletedEmployees().getOrDefault(emptyList())
            val deletedCount = deletedEmployees.size
            
            // Contar empleados añadidos en el último mes
            val currentDate = java.time.LocalDate.now()
            val oneMonthAgo = currentDate.minusMonths(1)
            val newEmployeesCount = allEmployees.count {
                if (it.fechaContrato.isNullOrBlank()) {
                    false
                } else {
                    val contractDate = java.time.LocalDate.parse(it.fechaContrato)
                    contractDate.isAfter(oneMonthAgo)
                }
            }
            
            val stats = mapOf(
                "total" to totalCount,
                "active" to activeCount,
                "inactive" to inactiveCount,
                "deleted" to deletedCount,
                "newThisMonth" to newEmployeesCount
            )
            
            Result.success(stats)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene los empleados añadidos recientemente (últimos 5)
     * @param limit Número de empleados a retornar (por defecto 5)
     * @return Result<List<EmployeeData>> con la lista de empleados recientes
     */    suspend fun getRecentEmployees(limit: Int = 5): Result<List<Map<String, String>>> {
        return try {
            val employees = supabaseClient.postgrest["empleados"]
                .select {
                    order("f_contra", Order.DESCENDING)
                    limit(limit.toLong())
                } 
                .decodeList<Employee>()
                
            val recentEmployees = employees.map { employee ->
                val fullName = "${employee.nombre1 ?: ""} ${employee.apellido1 ?: ""}"
                
                mapOf(
                    "id" to employee.cedula,
                    "fullName" to fullName.trim(),
                    "dateAdded" to (employee.fechaContrato ?: "")
                )
            }
            
            Result.success(recentEmployees)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene la distribución de empleados por departamento
     * @return Result<List<Map<String, Any>>> con la lista de departamentos y conteos
     */
    suspend fun getDepartmentDistribution(): Result<List<Map<String, Any>>> {
        return try {
            val employees = getAllEmployees().getOrDefault(emptyList())
            val departmentCounts = employees
                .filter { it.estado == 1 } // Solo contar empleados activos
                .groupBy { it.departamento ?: "Sin asignar" }
                .mapValues { it.value.size }
                
            // Obtener los nombres de departamentos completos si es posible
            val departamentos = getDepartamentos().getOrDefault(emptyList())
            val departmentMap = departamentos.associateBy { it.codigo }
            
            val departmentData = departmentCounts.map { (depCode, count) ->
                val departmentName = departmentMap[depCode]?.nombre ?: depCode
                mapOf(
                    "name" to departmentName,
                    "employeeCount" to count
                )
            }.sortedByDescending { it["employeeCount"] as Int }
            
            Result.success(departmentData)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene la distribución de empleados por género
     * @return Result<Map<String, Int>> con conteo por género
     */
    suspend fun getGenderDistribution(): Result<Map<String, Int>> {
        return try {
            val employees = getAllEmployees().getOrDefault(emptyList())
                .filter { it.estado == 1 } // Solo contar empleados activos
            
            val maleCount = employees.count { it.genero == 0 }
            val femaleCount = employees.count { it.genero == 1 }
            
            val genderData = mapOf(
                "male" to maleCount,
                "female" to femaleCount
            )
            
            Result.success(genderData)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene la distribución de empleados por rango de edad
     * @return Result<List<Map<String, Any>>> con rangos de edad y conteos
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAgeDistribution(): Result<List<Map<String, Any>>> {
        return try {
            val employees = getAllEmployees().getOrDefault(emptyList())
                .filter { it.estado == 1 } // Solo contar empleados activos
            
            val today = java.time.LocalDate.now()
            
            // Calcular edades y agrupar por rango
            val ageGroups = employees
                .filter { !it.fechaNacimiento.isNullOrBlank() }.mapNotNull { employee ->
                    try {
                        val birthDate = LocalDate.parse(employee.fechaNacimiento)
                        Period.between(birthDate, today).years
                    } catch (e: Exception) {
                        null // Ignorar fechas que no se pueden parsear
                    }
                }
                .groupBy { age ->
                    when {
                        age < 25 -> "18-24"
                        age in 25..34 -> "25-34"
                        age in 35..44 -> "35-44"
                        age in 45..54 -> "45-54"
                        else -> "55+"
                    }
                }
                .mapValues { it.value.size }
            
            // Ordenar por rango de edad
            val ageRangeOrder = listOf("18-24", "25-34", "35-44", "45-54", "55+")
            val ageDistribution = ageRangeOrder.map { range ->
                mapOf(
                    "label" to range,
                    "count" to (ageGroups[range] ?: 0)
                )
            }
            
            Result.success(ageDistribution)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

package com.proyect.ds6.data.repository

import com.proyect.ds6.model.* // Import all your data classes
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
//import io.github.jan.supabase.exceptions.SupabaseException
// import io.github.jan.supabase.rpc // If you use RPCs

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
}
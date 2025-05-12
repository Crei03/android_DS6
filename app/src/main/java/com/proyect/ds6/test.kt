package com.proyect.ds6

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable
import java.util.stream.IntStream.IntMapMultiConsumer

val supabase = createSupabaseClient(
    supabaseUrl = "https://vydouguzwrnxxezcwalv.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZ5ZG91Z3V6d3JueHhlemN3YWx2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI2NTgxOTEsImV4cCI6MjA1ODIzNDE5MX0.8dDVqfiMifNOlw0F0NLYvj5JZ3yNEQwY8dCbDTJ0-5I"
) {
    install(Postgrest)
    //install other modules
}


@Serializable
data class Client (
    val id: Int,
    val nombre: String,
    val apellido: String,
    val edad: Int,
)

suspend fun main(){
    val response = supabase
        .from("clientes")
        .select()
        .decodeSingle<Client>()
    println(response)
}
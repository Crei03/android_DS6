package com.proyect.ds6.db

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://srapsdaqmdhkxefxiatg.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNyYXBzZGFxbWRoa3hlZnhpYXRnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDYwNDA2MTYsImV4cCI6MjA2MTYxNjYxNn0.hMDctKSFT1EucNsIo9KvravBeEjnspRdRSxANwE8FiE"
) {
    install(Postgrest)
}
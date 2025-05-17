package com.proyect.ds6

import android.app.Application
import com.proyect.ds6.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DS6Application : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Inicializar Koin para la inyección de dependencias
        startKoin {
            androidLogger()
            androidContext(this@DS6Application)
            modules(appModule)
        }
    }
}

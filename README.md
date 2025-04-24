# App Android – Gestión de Empleados

Este repositorio contiene la aplicación móvil para Android destinada a la gestión de empleados de una empresa. Es una migración del sistema web existente (carpeta `/DS6`, PHP) a una app Android moderna, desarrollada en la carpeta `/app`.

## Características principales

- Listado de empleados (datos estáticos, sin conexión a API)
- Agregar, editar y eliminar empleados (solo interfaz, sin persistencia por ahora)
- Visualización de información personal, contacto, dirección y laboral
- Navegación fluida entre pantallas usando Jetpack Compose
- Componentes reutilizables y código modular

## Estructura del proyecto

```
/app
  └─ src/
      └─ main/
          ├─ java/com/proyect/ds6/
          │   ├─ model/         # Modelos de datos (Employee, etc.)
          │   ├─ ui/            # Pantallas y componentes de UI
          │   └─ MainActivity.kt
          └─ res/layout/        # Layouts XML para vistas legacy
```

## Requisitos

- Android Studio (recomendado: versión reciente)
- Dispositivo o emulador Android 7.0 (API 24) o superior

## Cómo ejecutar

1. Abre la carpeta `/app` en Android Studio.
2. Compila y ejecuta en un emulador o dispositivo físico.
3. Navega por la app: listado, agregar empleado, etc.

## Buenas prácticas aplicadas

- Separación de lógica y UI (`model/`, `ui/`)
- Componentes Compose reutilizables para cada sección del empleado
- Navegación declarativa con Navigation Compose
- Código limpio, legible y fácil de mantener

## Estado actual

- Los datos son estáticos y no requieren autenticación.
- No hay conexión a base de datos ni API.
- El foco está en la migración de la experiencia y estructura de la web a móvil.

---

Para dudas sobre buenas prácticas o estructura, revisa los archivos de instrucciones adicionales en la raíz del proyecto.

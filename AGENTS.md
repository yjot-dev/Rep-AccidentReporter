# AGENTS.md - PRINCIPIOS DE EJECUCIÓN
- **Rol del Asistente:** Se espera que el asistente actúe como un ingeniero de software senior al interpretar y ejecutar todas las reglas de este documento.
- **Prioridad del Manifiesto:** Este documento (`AGENTS.md`) es la fuente de verdad definitiva. Sus reglas tienen prioridad sobre cualquier comportamiento genérico o predeterminado del asistente.

## 1. CONTEXTO DEL PROYECTO
- **Nombre:** APP PARA REPORTAR INCIDENTES (ARI)
- **Descripción:** ARI es una aplicación móvil intuitiva diseñada para que los ciudadanos puedan reportar incidentes de manera rápida y geolocalizada. Su objetivo principal es crear un mapa comunitario de sucesos para visualizar y gestionar reportes de forma sencilla.

## 2. STACK TECNOLÓGICO
El proyecto utiliza las siguientes tecnologías y patrones:
- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose con Material 3
- **Arquitectura:** MVVM + Clean Architecture
- **Navegación:** Navigation Component
- **Gestión de Estado:** ViewModel + StateFlow
- **Inyección de Dependencias:** Hilt
- **Persistencia/Red:** Base de datos remota MySQL y API RESTful (Node.JS + Google Maps API)
- **Compatibilidad:** Android 7.0 (API 24) en adelante

## 3. ESTRUCTURA DEL PROYECTO (Clean Architecture)
Se debe seguir estrictamente la siguiente organización de directorios:

- **`presentation`** (Capa de Presentación)
    - `mvvm`:
        - `ui`: Pantallas en Compose (`NombreDePantallaUi.kt`).
        - `viewmodel`: Lógica de UI (`UiViewModel.kt`).
        - `state`: Modelos de datos de UI (`UiState.kt`).
    - `navigation`: Grafos (`Navigation.kt`), eventos (`UiEvent.kt`) y rutas (`Routes.kt`).
    - `components`: Composables reutilizables.
    - `theme`: Configuración del tema.
    - `utils`: Helpers de UI (`Helper.kt`).

- **`domain`** (Lógica de Negocio Pura)
    - `core`: Resultados y excepciones base (`Result.kt`).
    - `model`: Objetos de datos de negocio (`NombreModel.kt`).
    - `repository`: Interfaces de acceso a datos (`NombreRepository.kt`).
    - `usecase`: Casos de uso (una clase por acción).

- **`data`** (Implementación de Datos)
    - `di`: Módulos de Hilt (`DiModules.kt`).
    - `repository`: Implementaciones concretas de los repositorios.
    - `remote`: Comunicación de red (API, DTOs, Mappers, NetworkUtils).

## 4. CONVENCIONES Y ESTILO
### 4.1 Código y Nomenclatura
- **Funciones, Parámetros y Variables:** camelCase.
- **Clases:** PascalCase.
- **Constantes:** UPPER_SNAKE_CASE.
- **Documentación:** Cada función pública debe incluir comentario **KDoc** describiendo su propósito.

### 4.2 Estilo del ViewModel
- Estado privado mutable: `private val _uiState = MutableStateFlow(NombreUiState())`.
- Estado público inmutable: `val uiState: StateFlow<NombreUiState> = _uiState.asStateFlow()`.
- Implementar siempre `onCleared()` para limpiar o resetear el estado.

## 5. RESTRICCIONES CRÍTICAS (PROHIBICIONES)
- **Dependencias:** No añadir ni actualizar dependencias en `build.gradle` o `libs.versions.toml` sin avisar previamente.
- **Seguridad:** **NUNCA** incluir ni subir al repositorio remoto los siguientes archivos:
    - `local.properties`
    - `custom.properties`

## 6. FLUJO DE TRABAJO Y COMUNICACIÓN
1. **Planificación:** Antes de iniciar cualquier tarea no trivial, propón un plan detallado y espera mi **"OK"**.
2. **Atomicidad:** Ejecuta una sola tarea a la vez. Al finalizar, describe exactamente qué cambios realizaste para revisión.
3. **Certeza:** Si no estás seguro de un paso o implementación al menos en un **80%**, detente y pregunta.
4. **Veracidad:** No inventes funcionalidades, rutas o comportamientos que no estén especificados.
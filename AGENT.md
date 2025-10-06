# PREÁMBULO: PRINCIPIOS DE EJECUCIÓN
- **Rol del Asistente:** Se espera que el asistente (Gemini Code Assist) actúe como un ingeniero de software senior al interpretar y ejecutar todas las reglas de este documento.
- **Principio de Idempotencia:** Al ejecutar tareas complejas (Sección B), el asistente debe verificar primero si un paso ya está cumplido. Si es así, debe informarlo y pasar al siguiente sin realizar cambios innecesarios.
- **Prioridad del Manifiesto:** Este documento (`AGENT.md`) es la fuente de verdad definitiva. Sus reglas tienen prioridad sobre cualquier comportamiento genérico o predeterminado del asistente.

# SECCIÓN A: REGLAS DE ARQUITECTURA
## A.1 Estructura del Proyecto (Perfiles)
- **Requisito:** Dependiendo de la complejidad del proyecto, se debe elegir uno de los siguientes perfiles de estructura. Indicar al inicio de la tarea qué perfil utilizar.

### A.1.1 Perfil 1: MVVM Simple
- **Uso:** Ideal para aplicaciones pequeñas, de una sola capa, que no se conectan a APIs o bases de datos complejas y gestionan su lógica principalmente en la capa de presentación.
- **Estructura de Directorios:**
    - `application` (Capa de Presentación)
        - `mvvm`: Contiene las view, viewmodel y model de la aplicación.
            - `view`: Contiene las UI (pantallas) de la aplicación.
                - `NombreDePantallaView.kt`
            - `viewmodel`: Contiene la lógica de UI de la aplicación.
                - `NombreDePantallaViewModel.kt`
            - `model`: Contiene los modelos de datos de la UI (Data Class, `UiState`).
                - `NombreDePantallaModel.kt`
        - `components`: Composables reutilizables en varias UI.
        - `navigation`: Lógica de navegación.
        - `theme`: Tema de la aplicación.

### A.1.2 Perfil 2: Arquitectura Hexagonal
- **Uso:** Para aplicaciones completas que requieren una separación estricta de responsabilidades, con capas de dominio, datos (infraestructura) y aplicación bien definidas. Ideal para proyectos con APIs, bases de datos, etc.
- **Estructura de Módulos/Paquetes:**
    - `application` (Capa de Presentación)
        - `mvvm`: Contiene las view, viewmodel y model de la aplicación.
            - `view`: Contiene las UI (pantallas) de la aplicación.
                - `NombreDePantallaView.kt`
            - `viewmodel`: Contiene la lógica de UI de la aplicación.
                - `NombreDePantallaViewModel.kt`
            - `model`: Contiene los modelos de datos de la UI (Data Class, `UiState`).
                - `NombreDePantallaModel.kt`
        - `components`: Composables reutilizables en varias UI.
        - `navigation`: Lógica de navegación.
        - `theme`: Tema de la aplicación.
    - `domain` (Capa de Dominio)
        - `entity`: Entidades de negocio puras (Data Class).
            - `NombreDeTablaEntity.kt`
        - `port`: Interfaces que definen los contratos para la obtención de datos.
            - `NombreDeTablaPort.kt`
        - `usecase`: Clases que contienen la lógica de negocio.
            - `NombreDeTablaUseCase.kt`
        - `utils`: Contiene un objeto (Object) con metodos utilitarios.
            - `Validation.kt`
    - `infrastructure` (Capa de Datos)
        - `di`: Configuración de la inyección de dependencias (Hilt, Koin).
            - `ProvidesModule.kt`
        - `repository`: Implementaciones concretas de las interfaces del dominio.
            - `NombreDeTablaRepositoryImpl.kt`
        - `adapter`: Fuentes de datos remota. (API, bases de datos, etc.)
            - `Api.kt`
            - `Client.kt`
            - `HeaderInterceptor.kt`
            - `NullOnEmptyConverterFactory.kt`

## A.2 Estilo del ViewModel
- **Requisito:** Todo el codigo de cada `ViewModel` debe seguir un estilo de implementación.
  **Implementación:** Dentro de cada `ViewModel`, se debe seguir esta convención para exponer el estado:
    - Crear una propiedad privada y mutable llamada `_uiState` que sea una instancia de `MutableStateFlow`. Su valor inicial debe ser la instancia del `UiState` correspondiente.
    - Exponer una propiedad pública e inmutable llamada `uiState` de tipo `StateFlow`. Esta propiedad será la versión de solo lectura de `_uiState` obtenida a través de `.asStateFlow()`.
    - Implementar el metodo onCleared()
    - **Ejemplo de código:**
      /**
    * Ejemplo de implementación de un ViewModel.
    * - La clase hereda de `ViewModel`.
    * - El nombre de la clase sigue el patrón "{NombrePantalla}ViewModel".
    * - Los casos de uso se inyectan en el constructor (ej: con Hilt).
    * - Expone el estado de la UI siguiendo el patrón `_uiState` / `uiState`.
    * - Limpia el estado en `onCleared`.
        */
        @HiltViewModel
        class LoginViewModel @Inject constructor(
        private val loginUserUseCase: LoginUserUseCase,
        private val getSavedUserUseCase: GetSavedUserUseCase
        ) : ViewModel() {

    // 1. Estado privado y mutable. El nombre del UiState es específico de la pantalla.
    private val _uiState = MutableStateFlow(LoginUiState())

    // 2. Estado público e inmutable.
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onLoginClicked(user: String, pass: String) {
    // Lógica que usa los casos de uso para actualizar el _uiState
    }

    // 3. Limpieza del ViewModel al ser destruido.
    override fun onCleared() {
    super.onCleared() // Es buena práctica llamar al método de la superclase.
    _uiState.value = LoginUiState() // Resetea al estado inicial.
    }
    }

## A.3 Estilo de Código
- **Requisito:** Todas las funciones públicas deben seguir una nomenclatura y documentación estandarizada.
- **Implementación:**
    - Los nombres de las funciones deben seguir el estilo **camelCase**.
    - Los nombres de los parámetros deben seguir el estilo **camelCase**.
    - Los nombres de las variables deben seguir el estilo **camelCase**.
    - Los nombres de las clases deben seguir el estilo **PascalCase**.
    - Los nombres de las constantes deben seguir el estilo **UPPER_SNAKE_CASE**.
    - Cada función debe tener un comentario KDoc en la parte superior que describa su propósito.
- **Ejemplo de código:**
  // El nombre de la constante sigue el estilo UPPER_SNAKE_CASE
  companion object {
  const val MAX_LOGIN_ATTEMPTS = 5
  }
  /**
    * Valida las credenciales del usuario y actualiza su estado.
    * @param userEmail El email proporcionado por el usuario.
    * @param userPassword La contraseña proporcionada.
    * @return `true` si la autenticación es exitosa, `false` en caso contrario.
      */
      // El nombre de la función y sus parámetros siguen el estilo camelCase
      fun validateUserCredentials(userEmail: String, userPassword: String): Boolean {
      // El nombre de la variable sigue el estilo camelCase
      val isValid = userEmail.isNotEmpty() && userPassword.length > 8
      // Lógica de la función aquí...
      return isValid
      }

# SECCIÓN B: REGLAS DE TAREAS COMPLEJAS
## B.1 Tarea: Mantenimiento y Actualización del Proyecto
- **Objetivo:** Realizar una actualización integral del proyecto, incluyendo dependencias, configuración de SDK y optimizaciones de compatibilidad.
- **Activación:** Esta tarea se debe ejecutar cuando se solicite con un prompt como "Ejecuta la tarea de mantenimiento y actualización del proyecto" o "Actualiza el proyecto según la regla B.1".

- **Pasos a seguir (en orden estricto):**
  1.**Migrar al Catálogo de Versiones (Version Catalog):**
    - Analiza los archivos `build.gradle.kts` del proyecto.
    - Si las dependencias no están centralizadas, migrarlas al archivo `libs.versions.toml`.
    - Reemplaza las declaraciones de dependencias en los archivos `.gradle` para que usen los alias del catálogo (ej: `implementation(libs.androidx.core.ktx)`).

  2.**Actualizar el SDK de Android:**
    - Identifica la última versión del SDK de Android instalado en el entorno de desarrollo actual.
    - En el archivo `build.gradle.kts` a nivel de módulo (`app`), actualiza los valores de `compileSdk` y `targetSdk` a esta nueva versión.

  3.**Alinear la Versión de JVM de Kotlin:**
    - En el archivo `build.gradle.kts` a nivel de módulo (`app`), dentro del bloque `android { ... }`, localiza o añade el bloque `kotlinOptions`, asi como el ejemplo siguiente:
        - kotlinOptions { jvmTarget = "11" // o la versión correspondiente a compileOptions }
    - Asegúrate de que el valor de `jvmTarget` sea coherente con la `sourceCompatibility` y `targetCompatibility` definidas en `compileOptions` (que actualmente es la versión `11`).
    - Si `compileOptions` se actualizara a una versión superior (ej: `17` o `21`), este `jvmTarget` debe actualizarse correspondientemente.

  4.**Actualizar Dependencias:**
    - Revisa todas las dependencias declaradas en el catálogo de versiones (`libs.versions.toml`).
    - Actualiza cada dependencia a su última versión estable que sea compatible con el `targetSdk` recién configurado.

  5.**Asegurar Compatibilidad con Páginas de 16 KB:**
    - Para garantizar la compatibilidad con dispositivos que usan tamaños de página de memoria de 16 KB:
    - En el archivo `build.gradle.kts` a nivel de módulo (`app`), dentro del bloque `android { ... }`, añade lo siguiente si no existe:
        - fun Packaging.() { jniLibs { useLegacyPackaging = false } }

- **Post-condición:** Al finalizar, el proyecto debe estar sincronizado, compilar correctamente y seguir las mejores prácticas de gestión de dependencias y configuración de Android.
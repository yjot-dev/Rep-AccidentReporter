# PREÁMBULO: PRINCIPIOS DE EJECUCIÓN
- **Rol del Asistente:** Se espera que el asistente (Gemini Code Assist) actúe como un ingeniero de software senior al interpretar y ejecutar todas las reglas de este documento.
- **Principio de Idempotencia:** Al ejecutar tareas complejas (Sección B), el asistente debe verificar primero si un paso ya está cumplido. Si es así, debe informarlo y pasar al siguiente sin realizar cambios innecesarios.
- **Prioridad del Manifiesto:** Este documento (`AGENT.md`) es la fuente de verdad definitiva. Sus reglas tienen prioridad sobre cualquier comportamiento genérico o predeterminado del asistente.

# SECCIÓN A: REGLAS DE ARQUITECTURA
## A.1 Estructura del Proyecto (Perfiles)
- **Requisito:** La estructura del proyecto debe seguir un perfil de arquitectura y un paradigma de UI. Al inicio de la tarea, se debe indicar qué perfil y paradigma utilizar.

### A.1.1 Paradigma de UI
- **Requisito:** Se debe especificar si la interfaz de usuario se construirá con el sistema de Vistas tradicional (XML) o con Jetpack Compose.
- **Implementación:**
    - **`UI_COMPOSE`:** Para proyectos que usan Jetpack Compose.
    - **`UI_XML`:** Para proyectos que usan el sistema de Vistas basado en XML.

### A.1.2 Perfil de Estructura: MVVM Simple
- **Uso:** Ideal para aplicaciones pequeñas, de una sola capa, que no se conectan a APIs o bases de datos complejas y gestionan su lógica principalmente en la capa de presentación.
- **Estructura de Directorios:**
    - `presentation` (Capa de Presentación)
        - `mvvm`: Contiene las UI (pantallas), viewmodel y states de la aplicación.
            - `ui`: Contiene las UI de la aplicación.
                - **(Condicional) Si el paradigma es `UI_COMPOSE`:**
                    - `NombreDePantallaUi.kt`
                - **(Condicional) Si el paradigma es `UI_XML`:**
                    - `fragment`: Contiene los Fragments/Activities.
                        - `NombreDePantallaFragment.kt`
                    - `adapter`: Contiene los Adapters del RecyclerView.
                        - `NombreDePantallaAdapter.kt`
                    - **(Opcional) Si los ViewHolders son complejos y se extraen:**
                        - `viewholder`: Contiene los ViewHolders complejos.
                            - `NombreDePantallaViewHolder.kt`
            - `viewmodel`: Contiene la lógica de UI de la aplicación.
                - `UiViewModel.kt`
            - `state`: Contiene los modelos de datos de la UI.
                - `UiState.kt`
        - `navigation`: Contiene los grafos, canales de eventos y rutas de navegación.
            - `Navigation.kt`
            - `UiEvent.kt`
            - **(Condicional) Si el paradigma es `UI_COMPOSE`:**
                - `Routes.kt`
                - `Permission.kt`
        - **(Condicional) Si el paradigma es `UI_COMPOSE`:**
            - `components`: Contiene los composables reutilizables en varias UI.
            - `theme`: Contiene el tema de la aplicación.
        - **(Condicional) Si el paradigma es `UI_XML`:**
            - Se omiten los directorios `components` y `theme`.
            - Las vistas (layouts XML) residen en el directorio `res/layout`.
            - Los estilos y temas residen en el directorio `res/values`.
        - **(Opcional) Si se requiere metodos auxiliares:**
            - `utils`: Contiene los helpers de UI.
                - `Helper.kt`

### A.1.3 Perfil de Estructura: Clean Architecture
- **Uso:** Para aplicaciones completas que requieren una separación estricta de responsabilidades, con capas de dominio, datos (infraestructura) y aplicación bien definidas. Ideal para proyectos con APIs, bases de datos, etc.
- **Estructura de Directorios:**
    - `presentation` (Capa de Presentación)
        - `mvvm`: Contiene las UI (pantallas), viewmodel y states de la aplicación.
            - `ui`: Contiene las UI de la aplicación.
                - **(Condicional) Si el paradigma es `UI_COMPOSE`:**
                    - `NombreDePantallaUi.kt`
                - **(Condicional) Si el paradigma es `UI_XML`:**
                    - `fragment`: Contiene los Fragments/Activities.
                        - `NombreDePantallaFragment.kt`
                    - `adapter`: Contiene los Adapters del RecyclerView.
                        - `NombreDePantallaAdapter.kt`
                    - **(Opcional) Si los ViewHolders son complejos y se extraen:**
                        - `viewholder`: Contiene los ViewHolders complejos.
                            - `NombreDePantallaViewHolder.kt`
            - `viewmodel`: Contiene la lógica de UI de la aplicación.
                - `UiViewModel.kt`
            - `state`: Contiene los modelos de datos de la UI.
                - `UiState.kt`
        - `navigation`: Contiene los grafos, canales de eventos y rutas de navegación.
            - `Navigation.kt`
            - `UiEvent.kt`
            - **(Condicional) Si el paradigma es `UI_COMPOSE`:**
                - `Routes.kt`
                - `Permission.kt`
        - **(Condicional) Si el paradigma es `UI_COMPOSE`:**
            - `components`: Contiene los composables reutilizables en varias UI.
            - `theme`: Contiene el tema de la aplicación.
        - **(Condicional) Si el paradigma es `UI_XML`:**
            - Se omiten los directorios `components` y `theme`.
            - Las vistas (layouts XML) residen en el directorio `res/layout`.
            - Los estilos y temas residen en el directorio `res/values`.
        - **(Opcional) Si se requiere metodos auxiliares:**
            - `utils`: Contiene los helpers de UI.
                - `Helper.kt`
    - `domain` (Capa de lógica de negocio pura)
        - `core`: Contiene los resultados y excepciones base.
            - `Result.kt`
        - `model`: Contiene los objetos de datos de negocio
            - `NombreDeTablaModel.kt`
        - `repository`: Contiene las interfaces (contratos) que definen el acceso a datos.
            - `NombreDeTablaRepository.kt`
        - `usecase`: Contiene los casos de uso/Interactores (una clase por acción).
            - `NombreDeTablaUseCase.kt`
        - **(Opcional) Si se requiere metodos auxiliares:**
            - `utils`: Contiene los helpers de Domain.
                - `Helper.kt`
    - `data` (Capa de datos e implementación)
        - `di`: Contiene la configuración de la inyección de dependencias.
            - `DiModules.kt`
        - `repository`: Contiene las implementaciones concretas de las interfaces del dominio.
            - `NombreDeTablaRepositoryImpl.kt`
        - **(Condicional) Si se usa una base de datos local (ej: Room):**
            - `local`: Contiene las clases relacionadas con la base de datos local.
                - `dao`: Contiene los Data Access Objects (DAOs).
                    - `NombreDeTablaDao.kt`
                - `converter`: Contiene los conversores de tipos para la base de datos.
                    - `Converters.kt`
                - `database`: Contiene la clase principal que define la base de datos.
                    - `NombreDeProyectoDatabase.kt`
                - `entity`: Contiene las entidades para la base de datos
                    - `NombreDeTablaEntity.kt`
                - `mapper`: Contiene los conversores de entidades a modelos de dominio.
                    - `NombreDeTablaMapper.kt`
        - **(Condicional) Si se usa una API remota (ej: Retrofit):**
            - `remote`: Contiene las clases relacionadas con la comunicación de red.
                - `api`: Contiene los endpoints de la API.
                    - `NombreDeEndPointApi.kt`
                - `network`: Contiene la configuración de red con cliente HTTP e interceptores.
                    - `RetrofitBuilder.kt`
                    - `HeaderInterceptor.kt`
                - `dto`: Contiene los modelos de datos que coinciden exactamente con la respuesta JSON de la API.
                    - `NombreDeTablaDto.kt`
                - `mapper`: Contiene los conversores de DTOs a modelos de dominio.
                    - `NombreDeTablaMapper.kt`
                - `core`: Contiene las utilidades de red o conversores.
                    - `NetworkUtils.kt`
                    - `NullOnEmptyConverterFactory.kt`
        - **(Condicional) Si se usa Machine Learning local (ej: TensorFlow Lite, MLKit):**
            - `ml`: Contiene las implementaciones del procesamiento de inteligencia artificial.
                - `datasource`: Contiene las clases que gestionan el ciclo de vida del intérprete.
                    - `TfLiteDataSource.kt`
                - `dto`: Contiene los objetos de entrada/salida del modelo (Tensors/Recognitions).
                    - `RecognitionDto.kt`
                - `analyzer`: Contiene la lógica de pre-procesamiento y post-procesamiento.
                    - `ImageAnalyzer.kt`
                    - `TensorProcessor.kt`
                - `mapper`: Contiene los conversores de DTOs de ML a Modelos de Dominio.
                    - `MlResultMapper.kt`
        - **(Opcional) Si la aplicación requiere servicios en segundo plano:**
            - `service`: Contiene implementaciones para un servicio en Android.
                - `NombreService.kt`
        - **(Opcional) Si la aplicación requiere trabajadores en segundo plano:**
            - `worker`: Contiene implementaciones para un trabajador en Android.
                - `NombreWorker.kt`

## A.2 Estilo del ViewModel
- **Requisito:** Todo el codigo de cada `ViewModel` debe seguir un estilo de implementación.
  **Implementación:** Dentro de cada `ViewModel`, se debe seguir esta convención para exponer el estado:
    - Crear una propiedad privada y mutable llamada `_uiState` que sea una instancia de `MutableStateFlow`. Su valor inicial debe ser la instancia del `UiState` correspondiente.
    - Exponer una propiedad pública e inmutable llamada `uiState` de tipo `StateFlow`. Esta propiedad será la versión de solo lectura de `_uiState` obtenida a través de `.asStateFlow()`.
    - Implementar el metodo onCleared()
    - **Ejemplo de código:**
```kotlin
    /*
    * La clase hereda de `ViewModel`.
    * El nombre de la clase sigue el patrón "{NombrePantalla}ViewModel".
    * Los casos de uso se inyectan en el constructor (ej: con Hilt).
    * Expone el estado de la UI siguiendo el patrón `_uiState` / `uiState`.
    * Limpia el estado en `onCleared`.
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
            // Lógica de la función aquí...
        }
    
        // 3. Limpieza del ViewModel al ser destruido.
        override fun onCleared() {
            super.onCleared()
            _uiState.value = LoginUiState()
        }
    }
```

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
```kotlin
   /* Clase ejemplo de Login */
   class Login() {
       // El nombre de la constante sigue el estilo UPPER_SNAKE_CASE
       const val MAX_LOGIN_ATTEMPTS = 5
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
   }
```

# SECCIÓN B: REGLAS DE TAREAS COMPLEJAS
## B.1 Tarea: Mantenimiento y Actualización del Proyecto
- **Objetivo:** Realizar una actualización integral del proyecto, incluyendo dependencias, configuración de SDK y optimizaciones de compatibilidad y compilación.
- **Activación:** Esta tarea se debe ejecutar cuando se solicite con un prompt como "Ejecuta la tarea de mantenimiento y actualización del proyecto" o "Actualiza el proyecto según la regla B.1".

- **Pasos a seguir (en orden estricto):**
    1. **Migrar al Catálogo de Versiones (Version Catalog):**
        - Analiza los archivos `build.gradle.kts` del proyecto.
        - Si las dependencias no están centralizadas, migrarlas al archivo `libs.versions.toml`.
        - Reemplaza las declaraciones de dependencias en los archivos `.gradle` para que usen los alias del catálogo (ej: `implementation(libs.androidx.core.ktx)`).

    2. **Actualizar el SDK de Android:**
        - Identifica la última versión del SDK de Android disponible.
        - En el archivo `build.gradle.kts` a nivel de módulo (`app`), actualiza los valores de `compileSdk` y `targetSdk` a esta nueva versión.

    3. **Alinear la Versión de JVM de Kotlin:**
        - En el archivo `build.gradle.kts` a nivel de módulo (`app`), dentro del bloque `android { ... }`, localiza o añade el bloque `kotlinOptions`.
        - Asegúrate de que el valor de `jvmTarget` sea coherente con la `sourceCompatibility` y `targetCompatibility` definidas en `compileOptions` (usualmente "11", "17" o "21").

    4. **Actualizar Dependencias:**
        - Revisa todas las dependencias declaradas en `libs.versions.toml`.
        - Actualiza cada dependencia a su última versión estable compatible.

    5. **Asegurar Compatibilidad con Páginas de 16 KB:**
        - En el archivo `build.gradle.kts` a nivel de módulo (`app`), dentro del bloque `android { ... }`, añade lo siguiente si no existe:
          `packaging { jniLibs { useLegacyPackaging = false } }`

    6. **Habilitar Símbolos de Depuración para NDK:**
        - En el archivo `build.gradle.kts` a nivel de módulo (`app`), dentro de `android { buildTypes { release { ... } } }`, añade lo siguiente si no existe:
          `ndk { debugSymbolLevel = "FULL" }`

    7. **Habilitar Reducción de Código:**
        - En el archivo `build.gradle.kts` a nivel de módulo (`app`), dentro de `android { buildTypes { release { ... } } }`, añade lo siguiente si no existe:
          `isMinifyEnabled = true`

    8. **Habilitar Reducción de Recursos:**
        - En el archivo `build.gradle.kts` a nivel de módulo (`app`), dentro de `android { buildTypes { release { ... } } }`, añade lo siguiente si no existe:
          `isShrinkResources = true`

    9. **Excluir Metadatos de Licencia Duplicados:**
        - En el archivo `build.gradle.kts` a nivel de módulo (`app`), dentro de `android { packaging { ... } }`, añade lo siguiente si no existe:
          `resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" }`

    10. **Habilitar Advertencias de Deprecación:**
        - En el archivo `build.gradle.kts` a nivel de módulo (`app`), fuera del bloque `android` y antes de `dependencies`, añade lo siguiente si no existe:
          `tasks.withType<JavaCompile> { options.compilerArgs.add("-Xlint:deprecation") }`

    11. **Configurar Features de Compilación (UI_XML):**
        - **Condición:** Ejecutar solo si el paradigma de UI es `UI_XML` (según A.1.1).
        - En `android { ... }`, localiza o crea el bloque `buildFeatures`.
        - Asegúrate de que contenga `viewBinding = true` y `buildConfig = true`.

    12. **Configurar Features de Compilación (UI_COMPOSE):**
        - **Condición:** Ejecutar solo si el paradigma de UI es `UI_COMPOSE` (según A.1.1).
        - En `android { ... }`, localiza o crea el bloque `buildFeatures`.
        - Asegúrate de que contenga `compose = true` y `buildConfig = true`.

    13. **Habilitar el Cache de Configuración de Gradle:**
        - En el archivo `gradle.properties` a nivel de proyecto, añade la siguiente línea si no existe:
          `org.gradle.configuration-cache=true`

## B.2 Tarea: Configurar Firma de la Aplicación para Release
- **Objetivo:** Configurar de manera segura la firma para la variante de compilación `release`, utilizando un archivo `custom.properties` centralizado para no exponer información sensible en el control de versiones.
- **Activación:** Esta tarea se debe ejecutar cuando se solicite con un prompt como "Configura la firma para release según la regla B.2" o "Añade las credenciales de desarrollador al proyecto".

    - **Pasos a seguir (en orden estricto):**
        1.  **Verificar/Crear `keystore`:**
            -   Se asume que ya existe un archivo de almacén de claves (ej: `release.keystore`) en la raíz del proyecto. Si no es así, se debe notificar al usuario para que lo genere.

        2.  **Crear y Configurar `custom.properties`:**
            -   En la raíz del proyecto, crear un archivo llamado `custom.properties` si no existe.
            -   Añadir las siguientes claves a `custom.properties`, cuyos valores deben ser reemplazados por los del usuario:
                ```properties
                APP_STORE_FILE=D\:\\Ruta\\release.keystore
                APP_STORE_PASSWORD=Valor
                APP_KEY_ALIAS=Valor
                APP_KEY_PASSWORD=Valor
                ```
            -   Verificar que el nombre del archivo `custom.properties` está añadido al archivo `.gitignore` del proyecto.

        3.  **Cargar archivo `custom.properties`:**
            -   En `build.gradle.kts` a nivel de proyecto se debe cargar el archivo `custom.properties`, añade lo siguiente si no existe:
                ```kotlin
                // Carga el archivo custom.properties
                val customProperties = Properties()
                file("custom.properties").inputStream().use {
                    customProperties.load(it)
                }
                // Define las propiedades como variables globales del proyecto
                customProperties.forEach { (key, value) ->
                    extra[key.toString()] = value
                }
                ```

        4. **Configurar `signingConfigs`:**
            -   En `build.gradle.kts` a nivel de módulo (`app`), dentro de (`android { ... }`), añade lo siguiente si no existe:
                ```kotlin
                signingConfigs {
                    create("release") {
                        keyAlias = project.findProperty("APP_KEY_ALIAS") as? String
                        keyPassword = project.findProperty("APP_KEY_PASSWORD") as? String
                        storePassword = project.findProperty("APP_STORE_PASSWORD") as? String
                        storeFile = project.findProperty("APP_STORE_FILE")?.let { rootProject.file(it) }
                    }
                }
                ```
            -   En `build.gradle.kts` a nivel de módulo (`app`), dentro de (`android { buildTypes { release { ... } } }`), añade lo siguiente si no existe:
                `signingConfig = signingConfigs.getByName("release")`

- **Post-condición:** Al finalizar, el proyecto debe estar sincronizado, compilar correctamente y seguir las mejores prácticas de gestión de dependencias y configuración de Android.
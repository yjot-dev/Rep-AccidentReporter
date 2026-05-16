# APP PARA REPORTAR INCIDENTES (ARI)
ARI es una aplicación móvil intuitiva diseñada para que los ciudadanos puedan reportar incidentes de manera rápida y geolocalizada. El objetivo principal es crear un mapa comunitario de sucesos, permitiendo a los usuarios visualizar y gestionar reportes de forma sencilla.

# Características principales
- 🪟 Interfaz moderna con Jetpack Compose
- 🌐 Navegación con Navigation Component
- 📊 Integración con ViewModel + StateFlow
- 🎨 Patrón de diseño arquitectónico con MVVM + Clean Architecture
- 💉 Inyección de dependencias con Hilt
- 💽 Base de datos remota con MySQL
- 🧩 API RESTful con Node.JS y la API de Google Maps
- 📱 Compatible con Android 7.0 (API 24) en adelante

# Instalación
- Clona el repositorio: git clone https://github.com/yjot-dev/Rep-AccidentReporter.git
- Abre el proyecto en Android Studio (Giraffe o superior)
- Sincroniza dependencias con Gradle
- Conecta un dispositivo o emulador y ejecuta la app

# Tecnologías usadas
- Kotlin
- Jetpack Compose
- AndroidX (Navigation, Lifecycle, Core KTX)
- Material 3

# Uso
El flujo de uso de la aplicación está pensado para ser directo y eficiente, guiando al usuario a través de los siguientes pasos:

1. Configuración Inicial Sencilla: Al iniciar la aplicación por primera vez, el usuario tiene dos opciones claras:
   - Configurar Ubicación: Para nuevos usuarios, este es el punto de partida. Se les permite definir su país, provincia y ciudad. La aplicación utiliza esta información para obtener las coordenadas geográficas exactas, personalizando el mapa a su área de interés.
   - Configurar Token: Para usuarios recurrentes, esta función les permite reingresar un token personal único. Esto les devuelve el control sobre los incidentes que han reportado previamente, permitiéndoles editarlos o eliminarlos.
2. Visualización en el Mapa Interactivo: Una vez configurada la ubicación, el usuario accede a la vista principal: un mapa interactivo. En este mapa, se muestran con marcadores rojos todos los incidentes reportados por la comunidad. Al seleccionar un marcador, se puede obtener una vista previa con la información básica del incidente.
3. Gestión de Incidentes: Al hacer clic en la vista previa de un incidente, el sistema ofrece dos posibilidades:
   - Borrar el Reporte: Si el usuario es el "propietario" original del aviso, puede eliminarlo del mapa.
   - Ver/Editar Detalles: Esta opción lleva a una pantalla con toda la información del incidente. Para la mayoría de los usuarios, la información es de solo lectura. Sin embargo, para el propietario del reporte, esta pantalla se transforma en un formulario editable, permitiéndole actualizar la información según sea necesario.
4. Creación de un Nuevo Reporte: La característica central de ARI es la facilidad para añadir nuevos incidentes. El usuario simplemente debe pulsar en cualquier zona libre del mapa. Esto abre un formulario de "Agregar Ubicación", donde puede:
   - Seleccionar el tipo de incidente de una lista predefinida (ej: Tráfico, Accidentes, Problemas de servicios públicos).
   - Añadir una descripción detallada del suceso.

Al confirmar, el reporte se guarda, la aplicación registra la fecha automáticamente y el nuevo incidente aparece en el mapa para que toda la comunidad pueda verlo.
En resumen, ARI empodera a los usuarios para que sean participantes activos en el monitoreo de su comunidad, ofreciendo una herramienta fácil de usar para reportar, visualizar y gestionar incidentes locales de manera efectiva.

# Ver video Demo
[Ver en YouTube](https://youtu.be/zd-f3FNwjHA)

# Contribución
- Haz un fork del repositorio
- Crea una rama con tu feature: git checkout -b feature/nueva-funcionalidad
- Haz commit de tus cambios: git commit -m "Agrega nueva funcionalidad"
- Haz push a la rama: git push origin feature/nueva-funcionalidad
- Abre un Pull Request

# Licencia
Este proyecto está bajo la licencia GPL-3.0. Consulta el archivo LICENSE para más detalles.
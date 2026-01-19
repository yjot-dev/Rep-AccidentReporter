# APP PARA REPORTAR INCIDENTES (ARI)
Esta app le permite al usuario elegir una ubicación en el mapa para reportar algún incidente; luego de elegir una ubicación, se mostrará un formulario donde debe elegir el tipo de incidente y describir el incidente; luego la app obtiene la fecha actual del dispositivo y guarda el reporte en una BD.

# Características principales
- 🪟 Interfaz moderna con Jetpack Compose
- 🌐 Navegación con Navigation Component
- 📊 Integración con ViewModel + StateFlow
- 🎨 Patrón de diseño arquitectónico con MVVM + Hexagonal
- 🧩 Inyección de dependencias con Hilt
- 💽 Base de datos remota con MySQL, la API RESTful con Node y la API de Google Maps
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
- Al abrir la app, se muestra la vista *Inicio* para avanzar da click en el boton Continuar, luego se mostrara la vista *Mapa*
- La vista *Mapa* muestra la ubicacion actual pre-configurada de mi pais, hay puede cambiar las coordenadas para adaptaro a su ubicacion, si hay reportes se mostraran puntos de ubicacion
  de color rojo, en donde se podra hacer click para revisar su informacion, primero saldra una vista emergente mostrando el titulo del aviso y la fecha de publicacion, tambien ofrecera dos
  opciones, la primera es para ir a la vista *Editar Ubicacion* y la segunda es para borrar el aviso (solo permitido para el propietario del aviso).
- La vista *Editar Ubicacion* muestra la informacion completa del aviso en modo lectura para todos los usuario excepto para el propietario del aviso, al propietario se le permite actualizar
  dicha informacion.
- La vista *Agregar Ubicacion* aparece cuando se hace click en cualquier zona del mapa que no tenga un punto de ubicacion de color rojo, luego el usuario podra crear su propio aviso y ser
  el propietario del mismo, ademas al agregar el aviso usted puede elegir entre tres tipos de incidentes: 1. Trafico, 2. Accidentes y 3. Problemas en servicios publicos.
  
# Ver video Demo
[Ver en YouTube](https://youtu.be/a3F2HEVxkC0)

# Contribución
- Haz un fork del repositorio
- Crea una rama con tu feature: git checkout -b feature/nueva-funcionalidad
- Haz commit de tus cambios: git commit -m "Agrega nueva funcionalidad"
- Haz push a la rama: git push origin feature/nueva-funcionalidad
- Abre un Pull Request

# Licencia
Este proyecto está bajo la licencia GPL-3.0. Consulta el archivo LICENSE para más detalles.

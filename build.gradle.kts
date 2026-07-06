import java.util.Properties

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
}
// Carga el archivo custom.properties
val customProperties = Properties()
file("custom.properties").inputStream().use {
    customProperties.load(it)
}
// Define las propiedades como variables globales del proyecto
customProperties.forEach { (key, value) ->
    extra[key.toString()] = value
}
// Configuración para capturar errores de Pruebas Unitarias e Instrumentales en cualquier módulo
subprojects {
    tasks.withType<Test>().configureEach {
        testLogging {
            events("failed")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}
// Listener global para capturar fallos de compilación pesados
gradle.buildFinished {
    if (failure != null) {
        // Apunta directamente a la carpeta build del módulo principal (:app)
        val logDir = file("app/build/logs")
        if (!logDir.exists()) logDir.mkdirs()

        val errorFile = file("app/build/logs/build_error.txt")
        errorFile.writeText("""
            === FALLO DE COMPILACIÓN DETECTADO ===
            Fecha: ${java.util.Date()}
            Mensaje: ${failure?.message}
            Causa: ${failure?.cause}
        """.trimIndent())
    }
}
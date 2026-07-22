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
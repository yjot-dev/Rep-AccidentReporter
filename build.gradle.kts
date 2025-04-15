import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
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
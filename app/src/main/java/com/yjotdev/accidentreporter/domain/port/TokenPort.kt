package com.yjotdev.accidentreporter.domain.port

/**
 * Define el contrato para la gestión de un token de autenticación o de sesión.
 *
 * Esta interfaz pertenece a la capa de Dominio y actúa como un puerto en la Arquitectura Hexagonal.
 * Su propósito es abstraer el mecanismo de almacenamiento del token (por ejemplo, SharedPreferences,
 * DataStore, una variable en memoria, etc.), permitiendo que los casos de uso interactúen con
 * el token sin conocer los detalles de su implementación.
 */
interface TokenPort {

    fun createToken()

    fun getToken(): Int

    fun editToken(token: Int)
}

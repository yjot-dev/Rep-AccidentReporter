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

    /**
     * Crea o genera un nuevo token y lo persiste.
     *
     * La lógica específica de la generación (ej: un número aleatorio, un valor basado en la hora)
     * es responsabilidad de la implementación en la capa de infraestructura.
     */
    fun createToken()

    /**
     * Recupera el token almacenado actualmente.
     *
     * @return [Int] El valor numérico del token guardado. Si no existe un token, la
     * implementación debe decidir qué valor por defecto devolver (ej: 0, -1).
     */
    fun getToken(): Int
}

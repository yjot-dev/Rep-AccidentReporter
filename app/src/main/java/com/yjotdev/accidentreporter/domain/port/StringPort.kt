package com.yjotdev.accidentreporter.domain.port

/**
 * Define el contrato para la gestión de Strings.
 * Esta interfaz pertenece a la capa de Dominio y actúa como un puerto en
 * la Arquitectura Hexagonal.
 **/
interface StringPort {
    fun getString(resId: Int): String
    fun getString(resId: Int, vararg args: Any): String
}
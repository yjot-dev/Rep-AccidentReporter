package com.yjotdev.accidentreporter.domain.core
/**
 * Una clase genérica que encapsula un resultado exitoso con un valor data
 * o un error con una exception.
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
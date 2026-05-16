package com.yjotdev.accidentreporter.domain.core
/**
 * Una clase genérica que encapsula un resultado exitoso con un valor data
 * o un error con una exception.
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

/**
 * Función de extensión que permite transformar el valor contenido en un [Result.Success].
 * Si el resultado es un [Result.Error], se propaga la excepción original.
 *
 * @param transform Función lambda que define la conversión de tipo T a tipo R.
 * @return Un nuevo [Result] del tipo R.
 */
inline fun <T, R> Result<T>.mapSuccess(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(this.data))
        is Result.Error -> Result.Error(this.exception)
    }
}
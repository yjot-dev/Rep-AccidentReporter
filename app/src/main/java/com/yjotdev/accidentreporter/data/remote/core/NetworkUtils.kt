package com.yjotdev.accidentreporter.data.remote.core

import retrofit2.Response
import java.io.IOException
import com.yjotdev.accidentreporter.domain.core.Result

/**
 * Versión de safeApiCall para endpoints que DEVUELVEN un cuerpo de datos (body).
 * El tipo genérico T debe ser no nulo.
 */
suspend fun <T : Any> safeApiCallForBody(apiCall: suspend () -> Response<T>): Result<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.Success(body) // Camino feliz, el cuerpo no es nulo
            } else {
                // La API respondió 2xx pero sin cuerpo, lo cual es un error para este caso.
                Result.Error(Exception("API Error: Response body is null"))
            }
        } else {
            Result.Error(Exception("API Error: ${response.code()} ${response.message()}"))
        }
    } catch (e: IOException) {
        Result.Error(Exception("Network Error: ${e.message}", e))
    } catch (e: Exception) {
        Result.Error(Exception("Unknown Error: ${e.message}", e))
    }
}
/**
 * Versión de safeApiCall para endpoints que NO devuelven un cuerpo de datos (ej. DELETE, PUT).
 * No es genérica, siempre devuelve Result<Unit>.
 */
suspend fun safeApiCallForUnit(apiCall: suspend () -> Response<Unit>): Result<Unit> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            Result.Success(Unit) // La llamada fue exitosa.
        } else {
            Result.Error(Exception("API Error: ${response.code()} ${response.message()}"))
        }
    } catch (e: IOException) {
        Result.Error(Exception("Network Error: ${e.message}", e))
    } catch (e: Exception) {
        Result.Error(Exception("Unknown Error: ${e.message}", e))
    }
}
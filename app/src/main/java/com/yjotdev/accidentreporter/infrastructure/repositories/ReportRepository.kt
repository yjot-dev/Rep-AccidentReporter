package com.yjotdev.accidentreporter.infrastructure.repositories

import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.port.ReportPort
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.infrastructure.adapter.Api

@Singleton
class ReportRepository @Inject constructor(
    private val api: Api
) : ReportPort {
    /**
     * Versión de safeApiCall para endpoints que DEVUELVEN un cuerpo de datos (body).
     * El tipo genérico T debe ser no nulo.
     */
    private suspend fun <T : Any> safeApiCallForBody(apiCall: suspend () -> Response<T>): Result<T> {
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
    private suspend fun safeApiCallForUnit(apiCall: suspend () -> Response<Unit>): Result<Unit> {
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

    override suspend fun selectReports(): Result<List<ReportEntity>> {
        return safeApiCallForBody { api.getRetrofit().selectReports() }
    }

    override suspend fun insertReport(report: ReportEntity): Result<Unit> {
        return safeApiCallForUnit{ api.getRetrofit().insertReport(report) }
    }

    override suspend fun updateReport(id: Int, report: ReportEntity): Result<Unit> {
        return safeApiCallForUnit{ api.getRetrofit().updateReport(id, report) }
    }

    override suspend fun deleteReport(id: Int): Result<Unit> {
        return safeApiCallForUnit{ api.getRetrofit().deleteReport(id) }
    }
}
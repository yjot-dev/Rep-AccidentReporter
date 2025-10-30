package com.yjotdev.accidentreporter.infrastructure.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.GET
import retrofit2.http.Path
import com.yjotdev.accidentreporter.domain.entity.ReportEntity

/**
 * Interfaz de Retrofit para las operaciones de la API de reportes.
 * ESTA interfaz pertenece a la capa de Infraestructura y define los endpoints HTTP.
 */
interface ReportApi {
    @GET("reports/")
    suspend fun selectReports(): Response<List<ReportEntity>>

    @POST("reports/")
    suspend fun insertReport(@Body report:ReportEntity): Response<Unit>

    @PUT("reports/{id}")
    suspend fun updateReport(@Path("id") id:Int, @Body report:ReportEntity): Response<Unit>

    @DELETE("reports/{id}")
    suspend fun deleteReport(@Path("id") id:Int): Response<Unit>
}
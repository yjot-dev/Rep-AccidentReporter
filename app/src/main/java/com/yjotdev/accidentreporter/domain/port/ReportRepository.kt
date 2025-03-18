package com.yjotdev.accidentreporter.domain.port

import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReportRepository {
    @GET("reports/")
    suspend fun selectReports(): List<ReportEntity>

    @POST("reports/")
    suspend fun insertReport(@Body report:ReportEntity)

    @PUT("reports/{id}")
    suspend fun updateReport(@Path("id") id:Int, @Body report:ReportEntity)

    @DELETE("reports/{id}")
    suspend fun deleteReport(@Path("id") id:Int)
}
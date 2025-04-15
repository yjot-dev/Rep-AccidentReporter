package com.yjotdev.accidentreporter.infrastructure.repositories

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.port.ReportRepository
import com.yjotdev.accidentreporter.infrastructure.adapter.Api

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val api: Api
) : ReportRepository {

    /** Recupera una lista de reportes en tiempo real **/
    override fun getReportsFlow(): Flow<List<ReportEntity>> = flow {
        while(true){
            val reports = selectReports() // Llamada suspendida a Retrofit
            emit(reports) // Emite los datos
            delay(5000) // Intervalo de actualización
        }
    }

    /** Recupera una lista de reportes */
    override suspend fun selectReports(): List<ReportEntity> {
        return api.getRetrofit().selectReports()
    }

    /** Crea un nuevo reporte */
    override suspend fun insertReport(report: ReportEntity) {
        return api.getRetrofit().insertReport(report)
    }

    /** Actualiza un reporte existente */
    override suspend fun updateReport(id: Int, report: ReportEntity) {
        return api.getRetrofit().updateReport(id, report)
    }

    /** Elimina un reporte por su ID */
    override suspend fun deleteReport(id: Int) {
        return api.getRetrofit().deleteReport(id)
    }
}
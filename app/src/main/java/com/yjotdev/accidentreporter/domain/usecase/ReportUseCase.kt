package com.yjotdev.accidentreporter.domain.usecase

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.port.ReportRepository

@Singleton
class ReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    /** Obtener flujo de reportes mediante caso de uso **/
    operator fun invoke(): Flow<List<ReportEntity>> {
        return reportRepository.getReportsFlow()
    }

    /** Insertar reporte mediante caso de uso **/
    suspend operator fun invoke(report: ReportEntity) {
        return reportRepository.insertReport(report)
    }

    /** Actualizar reporte mediante caso de uso **/
    suspend operator fun invoke(id: Int, report: ReportEntity) {
        return reportRepository.updateReport(id, report)
    }

    /** Borrar reporte mediante caso de uso **/
    suspend operator fun invoke(id: Int) {
        return reportRepository.deleteReport(id)
    }
}
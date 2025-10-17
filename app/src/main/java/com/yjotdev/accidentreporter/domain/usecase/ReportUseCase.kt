package com.yjotdev.accidentreporter.domain.usecase

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.port.ReportPort

@Singleton
class ReportUseCase @Inject constructor(
    private val reportPort: ReportPort
) {
    /** Obtener reportes mediante caso de uso **/
    suspend operator fun invoke(): Result<List<ReportEntity>> {
        return reportPort.selectReports()
    }

    /** Insertar reporte mediante caso de uso **/
    suspend operator fun invoke(report: ReportEntity): Result<Unit> {
        return reportPort.insertReport(report)
    }

    /** Actualizar reporte mediante caso de uso **/
    suspend operator fun invoke(id: Int, report: ReportEntity): Result<Unit> {
        return reportPort.updateReport(id, report)
    }

    /** Borrar reporte mediante caso de uso **/
    suspend operator fun invoke(id: Int): Result<Unit> {
        return reportPort.deleteReport(id)
    }
}
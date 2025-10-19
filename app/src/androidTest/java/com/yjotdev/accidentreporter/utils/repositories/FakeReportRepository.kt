package com.yjotdev.accidentreporter.utils.repositories

import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.port.ReportPort
import com.yjotdev.accidentreporter.domain.core.Result

@Singleton
class FakeReportRepository @Inject constructor(): ReportPort {
    private val reportList = mutableListOf(
        ReportEntity(
            id = 2,
            latitude = -3.245448,
            longitude = -79.832331,
            date = "15/03/2025",
            type = "Accidentes",
            description = "Hubo un accidente en la calle 12",
            token = 1224567844
        )
    )

    override suspend fun selectReports(): Result<List<ReportEntity>> {
        return Result.Success(reportList)
    }

    override suspend fun insertReport(report: ReportEntity): Result<Unit> {
        reportList.add(report)
        return Result.Success(Unit)
    }

    override suspend fun updateReport(id: Int, report: ReportEntity): Result<Unit> {
        reportList[id] = report
        return Result.Success(Unit)
    }

    override suspend fun deleteReport(id: Int): Result<Unit> {
        reportList.removeAt(id)
        return Result.Success(Unit)
    }
}
package com.yjotdev.accidentreporter.utils.repositories

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.port.ReportRepository

@Singleton
class FakeReportRepositoryImpl @Inject constructor(): ReportRepository {
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

    override fun getReportsFlow(): Flow<List<ReportEntity>> {
        return flow {
            while(true){
                val reports = selectReports()
                emit(reports)
                delay(5000)
            }
        }
    }

    override suspend fun selectReports(): List<ReportEntity> {
        return reportList
    }

    override suspend fun insertReport(report: ReportEntity) {
        reportList.add(report)
    }

    override suspend fun updateReport(id: Int, report: ReportEntity) {
        reportList[id] = report
    }

    override suspend fun deleteReport(id: Int) {
        reportList.removeAt(id)
    }
}
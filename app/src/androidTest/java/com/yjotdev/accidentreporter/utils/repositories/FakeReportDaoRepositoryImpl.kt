package com.yjotdev.accidentreporter.utils.repositories

import javax.inject.Singleton
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import com.yjotdev.accidentreporter.domain.model.ReportModel
import com.yjotdev.accidentreporter.domain.repository.ReportDaoRepository

@Singleton
class FakeReportDaoRepositoryImpl @Inject constructor(): ReportDaoRepository {
    private val reportFlow = MutableStateFlow(listOf<ReportModel>())

    override fun getLocalReports(): Flow<List<ReportModel>> {
        return reportFlow
    }

    override suspend fun saveAllReports(reports: List<ReportModel>) {
        reportFlow.value = reports
    }

    override suspend fun insertLocalReport(report: ReportModel) {
        val reports = reportFlow.first().toMutableList()
        reports.add(report)
        reportFlow.value = reports
    }

    override suspend fun updateLocalReport(report: ReportModel) {
        val reports = reportFlow.first().toMutableList()
        val index = reports.indexOfFirst { item -> item.id == report.id }
        reports[index] = report
        reportFlow.value = reports
    }

    override suspend fun deleteLocalReport(report: ReportModel) {
        val reports = reportFlow.first().toMutableList()
        val index = reports.indexOfFirst { item -> item.id == report.id }
        reports.removeAt(index)
        reportFlow.value = reports
    }
}
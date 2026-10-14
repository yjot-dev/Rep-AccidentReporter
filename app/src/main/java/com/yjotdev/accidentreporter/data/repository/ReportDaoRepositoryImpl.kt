package com.yjotdev.accidentreporter.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.yjotdev.accidentreporter.domain.model.ReportModel
import com.yjotdev.accidentreporter.domain.repository.ReportDaoRepository
import com.yjotdev.accidentreporter.data.local.dao.ReportDao
import com.yjotdev.accidentreporter.data.local.mapper.toDomain
import com.yjotdev.accidentreporter.data.local.mapper.toBD

@Singleton
class ReportDaoRepositoryImpl @Inject constructor(
    private val reportDao: ReportDao,
) : ReportDaoRepository {
    override fun getLocalReports(): Flow<List<ReportModel>> {
        return reportDao.getLocalReports().map { res -> res.map { it.toDomain() } }
    }

    override suspend fun saveAllReports(reports: List<ReportModel>) {
        return reportDao.saveAllReports(reports.map { it.toBD() })
    }

    override suspend fun insertLocalReport(report: ReportModel) {
        return reportDao.insertLocalReport(report.toBD())
    }

    override suspend fun updateLocalReport(report: ReportModel) {
        return reportDao.updateLocalReport(report.toBD())
    }

    override suspend fun deleteLocalReport(report: ReportModel) {
        return reportDao.deleteLocalReport(report.toBD())
    }
}
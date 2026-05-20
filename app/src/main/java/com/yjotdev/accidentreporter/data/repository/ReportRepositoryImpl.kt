package com.yjotdev.accidentreporter.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.model.ReportModel
import com.yjotdev.accidentreporter.domain.repository.ReportRepository
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.core.mapSuccess
import com.yjotdev.accidentreporter.data.remote.core.safeApiCallForBody
import com.yjotdev.accidentreporter.data.remote.core.safeApiCallForUnit
import com.yjotdev.accidentreporter.data.remote.api.ReportApi
import com.yjotdev.accidentreporter.data.remote.mapper.toDomain
import com.yjotdev.accidentreporter.data.remote.mapper.toDto

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val reportApi: ReportApi,
) : ReportRepository {
    override suspend fun selectReports(): Result<List<ReportModel>> {
        return safeApiCallForBody { reportApi.selectReports() }
            .mapSuccess { result -> result.map { it.toDomain() } }
    }

    override suspend fun insertReport(report: ReportModel): Result<Unit> {
        return safeApiCallForUnit{ reportApi.insertReport(report.toDto()) }
    }

    override suspend fun updateReport(id: Int, report: ReportModel): Result<Unit> {
        return safeApiCallForUnit{ reportApi.updateReport(id, report.toDto()) }
    }

    override suspend fun deleteReport(id: Int): Result<Unit> {
        return safeApiCallForUnit{ reportApi.deleteReport(id) }
    }

    override suspend fun createToken(): Result<String> {
        return safeApiCallForBody { reportApi.createToken() }
            .mapSuccess { result -> result.token }
    }
}
package com.yjotdev.accidentreporter.infrastructure.repository

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.port.ReportPort
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.infrastructure.network.client.Api
import com.yjotdev.accidentreporter.infrastructure.network.core.safeApiCallForBody
import com.yjotdev.accidentreporter.infrastructure.network.core.safeApiCallForUnit

@Singleton
class ReportRepository @Inject constructor(
    private val api: Api
) : ReportPort {
    override suspend fun selectReports(): Result<List<ReportEntity>> {
        return safeApiCallForBody { api.getRetrofit().selectReports() }
    }

    override suspend fun insertReport(report: ReportEntity): Result<Unit> {
        return safeApiCallForUnit{ api.getRetrofit().insertReport(report) }
    }

    override suspend fun updateReport(id: Int, report: ReportEntity): Result<Unit> {
        return safeApiCallForUnit{ api.getRetrofit().updateReport(id, report) }
    }

    override suspend fun deleteReport(id: Int): Result<Unit> {
        return safeApiCallForUnit{ api.getRetrofit().deleteReport(id) }
    }
}
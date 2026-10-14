package com.yjotdev.accidentreporter.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.model.ReportModel
import com.yjotdev.accidentreporter.domain.repository.ReportApiRepository
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.core.mapSuccess
import com.yjotdev.accidentreporter.data.remote.core.safeApiCallForBody
import com.yjotdev.accidentreporter.data.remote.core.safeApiCallForUnit
import com.yjotdev.accidentreporter.data.remote.api.ReportApi
import com.yjotdev.accidentreporter.data.remote.mapper.toDomain
import com.yjotdev.accidentreporter.data.remote.mapper.toDto

@Singleton
class ReportApiRepositoryImpl @Inject constructor(
    private val reportApi: ReportApi,
) : ReportApiRepository {
    override suspend fun getRemoteReports(): Result<List<ReportModel>> {
        return safeApiCallForBody { reportApi.getRemoteReports() }
            .mapSuccess { result -> result.map { it.toDomain() } }
    }

    override suspend fun insertRemoteReport(report: ReportModel): Result<Unit> {
        return safeApiCallForUnit{ reportApi.insertRemoteReport(report.toDto()) }
    }

    override suspend fun updateRemoteReport(id: Int, report: ReportModel): Result<Unit> {
        return safeApiCallForUnit{ reportApi.updateRemoteReport(id, report.toDto()) }
    }

    override suspend fun deleteRemoteReport(id: Int): Result<Unit> {
        return safeApiCallForUnit{ reportApi.deleteRemoteReport(id) }
    }

    override suspend fun createToken(): Result<String> {
        return safeApiCallForBody { reportApi.createToken() }
            .mapSuccess { result -> result.token }
    }
}
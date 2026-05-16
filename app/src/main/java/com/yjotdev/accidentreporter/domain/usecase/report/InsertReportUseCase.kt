package com.yjotdev.accidentreporter.domain.usecase.report

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.repository.ReportRepository
import com.yjotdev.accidentreporter.domain.model.ReportModel

@Singleton
class InsertReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(report: ReportModel): Result<Unit> {
        return reportRepository.insertReport(report)
    }
}
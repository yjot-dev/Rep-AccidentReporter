package com.yjotdev.accidentreporter.domain.usecase.report

import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.repository.ReportApiRepository
import com.yjotdev.accidentreporter.domain.model.ReportModel

class InsertRemoteReportUseCase @Inject constructor(
    private val reportApiRepository: ReportApiRepository
) {
    suspend operator fun invoke(report: ReportModel): Result<Unit> {
        return reportApiRepository.insertRemoteReport(report)
    }
}
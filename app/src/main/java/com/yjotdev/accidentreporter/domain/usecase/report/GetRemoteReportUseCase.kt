package com.yjotdev.accidentreporter.domain.usecase.report

import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.model.ReportModel
import com.yjotdev.accidentreporter.domain.repository.ReportApiRepository

class GetRemoteReportUseCase @Inject constructor(
    private val reportApiRepository: ReportApiRepository
) {
    suspend operator fun invoke(): Result<List<ReportModel>> {
        return reportApiRepository.getRemoteReports()
    }
}
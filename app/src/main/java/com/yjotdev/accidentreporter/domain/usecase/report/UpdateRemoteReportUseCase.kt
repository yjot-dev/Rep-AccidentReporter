package com.yjotdev.accidentreporter.domain.usecase.report

import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.repository.ReportApiRepository
import com.yjotdev.accidentreporter.domain.model.ReportModel

class UpdateRemoteReportUseCase @Inject constructor(
    private val reportApiRepository: ReportApiRepository
) {
    suspend operator fun invoke(id: Int, report: ReportModel): Result<Unit> {
        return reportApiRepository.updateRemoteReport(id, report)
    }
}
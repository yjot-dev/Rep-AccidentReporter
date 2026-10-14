package com.yjotdev.accidentreporter.domain.usecase.report

import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.repository.ReportApiRepository

class DeleteRemoteReportUseCase @Inject constructor(
    private val reportApiRepository: ReportApiRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return reportApiRepository.deleteRemoteReport(id)
    }
}
package com.yjotdev.accidentreporter.domain.usecase.report

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.repository.ReportRepository

@Singleton
class DeleteReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return reportRepository.deleteReport(id)
    }
}
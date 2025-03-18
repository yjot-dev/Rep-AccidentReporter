package com.yjotdev.accidentreporter.domain.usecase

import com.yjotdev.accidentreporter.domain.port.ReportRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(id: Int) {
        return reportRepository.deleteReport(id)
    }
}
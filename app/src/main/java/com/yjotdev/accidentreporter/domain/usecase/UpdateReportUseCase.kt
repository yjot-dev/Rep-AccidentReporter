package com.yjotdev.accidentreporter.domain.usecase

import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.port.ReportRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(id: Int, report: ReportEntity) {
        return reportRepository.updateReport(id, report)
    }
}
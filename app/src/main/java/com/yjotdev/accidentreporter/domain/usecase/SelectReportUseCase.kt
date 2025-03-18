package com.yjotdev.accidentreporter.domain.usecase

import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.port.ReportRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(): List<ReportEntity> {
        return reportRepository.selectReports()
    }
}
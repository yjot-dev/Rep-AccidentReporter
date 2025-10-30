package com.yjotdev.accidentreporter.domain.usecase.report

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.port.ReportPort

@Singleton
class SelectReportUseCase @Inject constructor(
    private val reportPort: ReportPort
) {
    suspend operator fun invoke(): Result<List<ReportEntity>> {
        return reportPort.selectReports()
    }
}
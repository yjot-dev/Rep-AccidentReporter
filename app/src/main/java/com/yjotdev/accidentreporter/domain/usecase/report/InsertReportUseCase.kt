package com.yjotdev.accidentreporter.domain.usecase.report

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.port.ReportPort
import com.yjotdev.accidentreporter.domain.entity.ReportEntity

@Singleton
class InsertReportUseCase @Inject constructor(
    private val reportPort: ReportPort
) {
    suspend operator fun invoke(report: ReportEntity): Result<Unit> {
        return reportPort.insertReport(report)
    }
}
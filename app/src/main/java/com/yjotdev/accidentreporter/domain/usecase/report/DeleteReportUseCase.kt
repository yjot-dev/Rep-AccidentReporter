package com.yjotdev.accidentreporter.domain.usecase.report

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.port.ReportPort

@Singleton
class DeleteReportUseCase @Inject constructor(
    private val reportPort: ReportPort
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return reportPort.deleteReport(id)
    }
}
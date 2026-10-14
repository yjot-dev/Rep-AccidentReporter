package com.yjotdev.accidentreporter.domain.usecase.report

import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.repository.ReportDaoRepository
import com.yjotdev.accidentreporter.domain.model.ReportModel

class InsertLocalReportUseCase @Inject constructor(
    private val reportDaoRepository: ReportDaoRepository
) {
    suspend operator fun invoke(report: ReportModel) {
        return reportDaoRepository.insertLocalReport(report)
    }
}
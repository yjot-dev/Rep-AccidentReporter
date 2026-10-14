package com.yjotdev.accidentreporter.domain.usecase.report

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.model.ReportModel
import com.yjotdev.accidentreporter.domain.repository.ReportDaoRepository

class GetLocalReportUseCase @Inject constructor(
    private val reportDaoRepository: ReportDaoRepository
) {
    operator fun invoke(): Flow<List<ReportModel>> {
        return reportDaoRepository.getLocalReports()
    }
}
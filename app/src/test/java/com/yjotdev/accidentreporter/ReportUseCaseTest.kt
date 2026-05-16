package com.yjotdev.accidentreporter

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.model.ReportModel
import com.yjotdev.accidentreporter.domain.repository.ReportRepository
import com.yjotdev.accidentreporter.domain.usecase.report.DeleteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.InsertReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.SelectReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.UpdateReportUseCase

/**
 * Pruebas unitarias para los casos de uso CRUD de Reportes.
 */
class ReportUseCaseTest {

    private lateinit var reportRepository: ReportRepository

    private lateinit var selectReportUseCase: SelectReportUseCase
    private lateinit var insertReportUseCase: InsertReportUseCase
    private lateinit var updateReportUseCase: UpdateReportUseCase
    private lateinit var deleteReportUseCase: DeleteReportUseCase

    @Before
    fun setUp() {
        reportRepository = mockk()
        selectReportUseCase = SelectReportUseCase(reportRepository)
        insertReportUseCase = InsertReportUseCase(reportRepository)
        updateReportUseCase = UpdateReportUseCase(reportRepository)
        deleteReportUseCase = DeleteReportUseCase(reportRepository)
    }

    @Test
    fun whenSelectReportUseCaseIsInvokedSuccessfullyThenItReturnsListOfReports() = runTest {
        // Given
        val fakeReportList = listOf(ReportModel(id = 1, description = "Test", token = 123))
        coEvery { reportRepository.selectReports() } returns Result.Success(fakeReportList)

        // When
        val result = selectReportUseCase()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(fakeReportList, (result as Result.Success).data)
        coVerify(exactly = 1) { reportRepository.selectReports() }
    }

    @Test
    fun whenInsertReportUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val newReport = ReportModel(id = 0, description = "New Report", token = 456)
        coEvery { reportRepository.insertReport(newReport) } returns Result.Success(Unit)

        // When
        insertReportUseCase(newReport)

        // Then
        coVerify(exactly = 1) { reportRepository.insertReport(newReport) }
    }

    @Test
    fun whenUpdateReportUseCaseIsInvokedThenPortMethodIsCalledWithCorrectData() = runTest {
        // Given
        val reportId = 1
        val updatedReport = ReportModel(id = 1, description = "Updated Report", token = 789)
        coEvery { reportRepository.updateReport(reportId, updatedReport) } returns Result.Success(Unit)

        // When
        updateReportUseCase(reportId, updatedReport)

        // Then
        coVerify(exactly = 1) { reportRepository.updateReport(reportId, updatedReport) }
    }

    @Test
    fun whenDeleteReportUseCaseIsInvokedThenPortMethodIsCalledWithCorrectId() = runTest {
        // Given
        val reportId = 1
        coEvery { reportRepository.deleteReport(reportId) } returns Result.Success(Unit)

        // When
        deleteReportUseCase(reportId)

        // Then
        coVerify(exactly = 1) { reportRepository.deleteReport(reportId) }
    }
}

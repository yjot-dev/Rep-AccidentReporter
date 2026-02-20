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
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.port.ReportPort
import com.yjotdev.accidentreporter.domain.usecase.report.DeleteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.InsertReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.SelectReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.UpdateReportUseCase

/**
 * Pruebas unitarias para los casos de uso CRUD de Reportes.
 */
class ReportUseCaseTest {

    private lateinit var reportPort: ReportPort

    private lateinit var selectReportUseCase: SelectReportUseCase
    private lateinit var insertReportUseCase: InsertReportUseCase
    private lateinit var updateReportUseCase: UpdateReportUseCase
    private lateinit var deleteReportUseCase: DeleteReportUseCase

    @Before
    fun setUp() {
        reportPort = mockk()
        selectReportUseCase = SelectReportUseCase(reportPort)
        insertReportUseCase = InsertReportUseCase(reportPort)
        updateReportUseCase = UpdateReportUseCase(reportPort)
        deleteReportUseCase = DeleteReportUseCase(reportPort)
    }

    @Test
    fun whenSelectReportUseCaseIsInvokedSuccessfullyThenItReturnsListOfReports() = runTest {
        // Given
        val fakeReportList = listOf(ReportEntity(id = 1, description = "Test", token = 123))
        coEvery { reportPort.selectReports() } returns Result.Success(fakeReportList)

        // When
        val result = selectReportUseCase()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(fakeReportList, (result as Result.Success).data)
        coVerify(exactly = 1) { reportPort.selectReports() }
    }

    @Test
    fun whenInsertReportUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val newReport = ReportEntity(id = 0, description = "New Report", token = 456)
        coEvery { reportPort.insertReport(newReport) } returns Result.Success(Unit)

        // When
        insertReportUseCase(newReport)

        // Then
        coVerify(exactly = 1) { reportPort.insertReport(newReport) }
    }

    @Test
    fun whenUpdateReportUseCaseIsInvokedThenPortMethodIsCalledWithCorrectData() = runTest {
        // Given
        val reportId = 1
        val updatedReport = ReportEntity(id = 1, description = "Updated Report", token = 789)
        coEvery { reportPort.updateReport(reportId, updatedReport) } returns Result.Success(Unit)

        // When
        updateReportUseCase(reportId, updatedReport)

        // Then
        coVerify(exactly = 1) { reportPort.updateReport(reportId, updatedReport) }
    }

    @Test
    fun whenDeleteReportUseCaseIsInvokedThenPortMethodIsCalledWithCorrectId() = runTest {
        // Given
        val reportId = 1
        coEvery { reportPort.deleteReport(reportId) } returns Result.Success(Unit)

        // When
        deleteReportUseCase(reportId)

        // Then
        coVerify(exactly = 1) { reportPort.deleteReport(reportId) }
    }
}

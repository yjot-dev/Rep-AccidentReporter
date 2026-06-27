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
import com.yjotdev.accidentreporter.domain.usecase.report.CreateTokenUseCase
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
    private lateinit var createTokenUseCase: CreateTokenUseCase

    @Before
    fun setUp() {
        reportRepository = mockk()
        selectReportUseCase = SelectReportUseCase(reportRepository)
        insertReportUseCase = InsertReportUseCase(reportRepository)
        updateReportUseCase = UpdateReportUseCase(reportRepository)
        deleteReportUseCase = DeleteReportUseCase(reportRepository)
        createTokenUseCase = CreateTokenUseCase(reportRepository)
    }

    @Test
    fun whenSelectReportUseCaseIsInvokedSuccessfullyThenItReturnListOfReports() = runTest {
        // Given
        val fakeReportList = listOf(ReportModel(id = 1, description = "Test", token = "a7cf5ac786824acaccff4d533832f1f5"))
        coEvery { reportRepository.selectReports() } returns Result.Success(fakeReportList)

        // When
        val result = selectReportUseCase()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(fakeReportList, (result as Result.Success).data)
        coVerify(exactly = 1) { reportRepository.selectReports() }
    }

    @Test
    fun whenSelectReportUseCaseIsInvokedWithErrorThenReturnException() = runTest {
        // Given
        val exception = Exception("Error al obtener reporte")
        coEvery { reportRepository.selectReports() } returns Result.Error(exception)

        // When
        val result = selectReportUseCase()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify(exactly = 1) { reportRepository.selectReports() }
    }

    @Test
    fun whenInsertReportUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val newReport = ReportModel(id = 0, description = "New Report", token = "a7cf5ac786824acaccff4d533832f1f5")
        coEvery { reportRepository.insertReport(newReport) } returns Result.Success(Unit)

        // When
        val result = insertReportUseCase(newReport)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(Unit, (result as Result.Success).data)
        coVerify(exactly = 1) { reportRepository.insertReport(newReport) }
    }

    @Test
    fun whenInsertReportUseCaseIsInvokedWithErrorThenReturnException() = runTest {
        // Given
        val exception = Exception("Error al insertar reporte")
        val newReport = ReportModel(id = 0, description = "New Report", token = "a7cf5ac786824acaccff4d533832f1f5")
        coEvery { reportRepository.insertReport(newReport) } returns Result.Error(exception)

        // When
        val result = insertReportUseCase(newReport)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify(exactly = 1) { reportRepository.insertReport(newReport) }
    }

    @Test
    fun whenUpdateReportUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val reportId = 1
        val updatedReport = ReportModel(id = 1, description = "Updated Report", token = "a7cf5ac786824acaccff4d533832f1f5")
        coEvery { reportRepository.updateReport(reportId, updatedReport) } returns Result.Success(Unit)

        // When
        val result = updateReportUseCase(reportId, updatedReport)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(Unit, (result as Result.Success).data)
        coVerify(exactly = 1) { reportRepository.updateReport(reportId, updatedReport) }
    }

    @Test
    fun whenUpdateReportUseCaseIsInvokedWithErrorThenReturnException() = runTest {
        // Given
        val exception = Exception("Error al actualizar reporte")
        val reportId = 1
        val updatedReport = ReportModel(id = 1, description = "Updated Report", token = "a7cf5ac786824acaccff4d533832f1f5")
        coEvery { reportRepository.updateReport(reportId, updatedReport) } returns Result.Error(exception)

        // When
        val result = updateReportUseCase(reportId, updatedReport)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify(exactly = 1) { reportRepository.updateReport(reportId, updatedReport) }
    }

    @Test
    fun whenDeleteReportUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val reportId = 1
        coEvery { reportRepository.deleteReport(reportId) } returns Result.Success(Unit)

        // When
        val result = deleteReportUseCase(reportId)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(Unit, (result as Result.Success).data)
        coVerify(exactly = 1) { reportRepository.deleteReport(reportId) }
    }

    @Test
    fun whenDeleteReportUseCaseIsInvokedWithErrorThenReturnException() = runTest {
        // Given
        val exception = Exception("Error al eliminar reporte")
        val reportId = 1
        coEvery { reportRepository.deleteReport(reportId) } returns Result.Error(exception)

        // When
        val result = deleteReportUseCase(reportId)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify(exactly = 1) { reportRepository.deleteReport(reportId) }
    }

    @Test
    fun whenCreateTokenUseCaseIsInvokedSuccessfullyThenItReturnToken() = runTest {
        // Given
        val token = "a7cf5ac786824acaccff4d533832f1f5"
        coEvery { reportRepository.createToken() } returns Result.Success(token)

        // When
        val result = createTokenUseCase()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(token, (result as Result.Success).data)
        coVerify(exactly = 1) { reportRepository.createToken() }
    }

    @Test
    fun whenCreateTokenUseCaseIsInvokedWithErrorThenReturnException() = runTest {
        // Given
        val exception = Exception("Error al crear token")
        coEvery { reportRepository.createToken() } returns Result.Error(exception)

        // When
        val result = createTokenUseCase()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify(exactly = 1) { reportRepository.createToken() }
    }
}

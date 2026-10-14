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
import com.yjotdev.accidentreporter.domain.repository.ReportApiRepository
import com.yjotdev.accidentreporter.domain.usecase.report.CreateTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.DeleteRemoteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.InsertRemoteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.GetRemoteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.UpdateRemoteReportUseCase

/**
 * Pruebas unitarias para los casos de uso CRUD de Reportes.
 */
class ReportUseCaseTest {

    private lateinit var reportApiRepository: ReportApiRepository
    private lateinit var getRemoteReportUseCase: GetRemoteReportUseCase
    private lateinit var insertRemoteReportUseCase: InsertRemoteReportUseCase
    private lateinit var updateRemoteReportUseCase: UpdateRemoteReportUseCase
    private lateinit var deleteRemoteReportUseCase: DeleteRemoteReportUseCase
    private lateinit var createTokenUseCase: CreateTokenUseCase

    @Before
    fun setUp() {
        reportApiRepository = mockk()
        getRemoteReportUseCase = GetRemoteReportUseCase(reportApiRepository)
        insertRemoteReportUseCase = InsertRemoteReportUseCase(reportApiRepository)
        updateRemoteReportUseCase = UpdateRemoteReportUseCase(reportApiRepository)
        deleteRemoteReportUseCase = DeleteRemoteReportUseCase(reportApiRepository)
        createTokenUseCase = CreateTokenUseCase(reportApiRepository)
    }

    @Test
    fun whenSelectReportUseCaseIsInvokedSuccessfullyThenItReturnListOfReports() = runTest {
        // Given
        val fakeReportList = listOf(ReportModel(id = 1, description = "Test", token = "a7cf5ac786824acaccff4d533832f1f5"))
        coEvery { reportApiRepository.getRemoteReports() } returns Result.Success(fakeReportList)

        // When
        val result = getRemoteReportUseCase()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(fakeReportList, (result as Result.Success).data)
        coVerify(exactly = 1) { reportApiRepository.getRemoteReports() }
    }

    @Test
    fun whenSelectReportUseCaseIsInvokedWithErrorThenReturnException() = runTest {
        // Given
        val exception = Exception("Error al obtener reporte")
        coEvery { reportApiRepository.getRemoteReports() } returns Result.Error(exception)

        // When
        val result = getRemoteReportUseCase()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify(exactly = 1) { reportApiRepository.getRemoteReports() }
    }

    @Test
    fun whenInsertReportUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val newReport = ReportModel(id = 0, description = "New Report", token = "a7cf5ac786824acaccff4d533832f1f5")
        coEvery { reportApiRepository.insertRemoteReport(newReport) } returns Result.Success(Unit)

        // When
        val result = insertRemoteReportUseCase(newReport)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(Unit, (result as Result.Success).data)
        coVerify(exactly = 1) { reportApiRepository.insertRemoteReport(newReport) }
    }

    @Test
    fun whenInsertReportUseCaseIsInvokedWithErrorThenReturnException() = runTest {
        // Given
        val exception = Exception("Error al insertar reporte")
        val newReport = ReportModel(id = 0, description = "New Report", token = "a7cf5ac786824acaccff4d533832f1f5")
        coEvery { reportApiRepository.insertRemoteReport(newReport) } returns Result.Error(exception)

        // When
        val result = insertRemoteReportUseCase(newReport)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify(exactly = 1) { reportApiRepository.insertRemoteReport(newReport) }
    }

    @Test
    fun whenUpdateReportUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val reportId = 1
        val updatedReport = ReportModel(id = 1, description = "Updated Report", token = "a7cf5ac786824acaccff4d533832f1f5")
        coEvery { reportApiRepository.updateRemoteReport(reportId, updatedReport) } returns Result.Success(Unit)

        // When
        val result = updateRemoteReportUseCase(reportId, updatedReport)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(Unit, (result as Result.Success).data)
        coVerify(exactly = 1) { reportApiRepository.updateRemoteReport(reportId, updatedReport) }
    }

    @Test
    fun whenUpdateReportUseCaseIsInvokedWithErrorThenReturnException() = runTest {
        // Given
        val exception = Exception("Error al actualizar reporte")
        val reportId = 1
        val updatedReport = ReportModel(id = 1, description = "Updated Report", token = "a7cf5ac786824acaccff4d533832f1f5")
        coEvery { reportApiRepository.updateRemoteReport(reportId, updatedReport) } returns Result.Error(exception)

        // When
        val result = updateRemoteReportUseCase(reportId, updatedReport)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify(exactly = 1) { reportApiRepository.updateRemoteReport(reportId, updatedReport) }
    }

    @Test
    fun whenDeleteReportUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val reportId = 1
        coEvery { reportApiRepository.deleteRemoteReport(reportId) } returns Result.Success(Unit)

        // When
        val result = deleteRemoteReportUseCase(reportId)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(Unit, (result as Result.Success).data)
        coVerify(exactly = 1) { reportApiRepository.deleteRemoteReport(reportId) }
    }

    @Test
    fun whenDeleteReportUseCaseIsInvokedWithErrorThenReturnException() = runTest {
        // Given
        val exception = Exception("Error al eliminar reporte")
        val reportId = 1
        coEvery { reportApiRepository.deleteRemoteReport(reportId) } returns Result.Error(exception)

        // When
        val result = deleteRemoteReportUseCase(reportId)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify(exactly = 1) { reportApiRepository.deleteRemoteReport(reportId) }
    }

    @Test
    fun whenCreateTokenUseCaseIsInvokedSuccessfullyThenItReturnToken() = runTest {
        // Given
        val token = "a7cf5ac786824acaccff4d533832f1f5"
        coEvery { reportApiRepository.createToken() } returns Result.Success(token)

        // When
        val result = createTokenUseCase()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(token, (result as Result.Success).data)
        coVerify(exactly = 1) { reportApiRepository.createToken() }
    }

    @Test
    fun whenCreateTokenUseCaseIsInvokedWithErrorThenReturnException() = runTest {
        // Given
        val exception = Exception("Error al crear token")
        coEvery { reportApiRepository.createToken() } returns Result.Error(exception)

        // When
        val result = createTokenUseCase()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
        coVerify(exactly = 1) { reportApiRepository.createToken() }
    }
}

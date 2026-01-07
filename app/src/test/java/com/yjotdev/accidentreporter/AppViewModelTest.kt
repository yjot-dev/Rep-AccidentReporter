package com.yjotdev.accidentreporter

import app.cash.turbine.test
import com.google.android.gms.maps.model.LatLng
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.usecase.report.DeleteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.InsertReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.SelectReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.UpdateReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.CreateTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.EditTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.GetTokenUseCase
import com.yjotdev.accidentreporter.application.mvvm.viewmodel.AppViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {

    // 1. Mocks de los Casos de Uso
    private val selectReportUseCase: SelectReportUseCase = mockk()
    private val insertReportUseCase: InsertReportUseCase = mockk()
    private val updateReportUseCase: UpdateReportUseCase = mockk()
    private val deleteReportUseCase: DeleteReportUseCase = mockk()
    private val createTokenUseCase: CreateTokenUseCase = mockk(relaxed = true) // Relaxed para métodos void
    private val getTokenUseCase: GetTokenUseCase = mockk()
    private val editTokenUseCase: EditTokenUseCase = mockk(relaxed = true)

    private lateinit var viewModel: AppViewModel

    // 2. Dispatcher para Corrutinas
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Comportamiento por defecto para el init block del ViewModel
        every { getTokenUseCase() } returns 1234

        viewModel = AppViewModel(
            selectReportUseCase,
            insertReportUseCase,
            updateReportUseCase,
            deleteReportUseCase,
            createTokenUseCase,
            getTokenUseCase,
            editTokenUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initBlockLoadsTokenCorrectly() = runTest {
        // El init se ejecuta al instanciar el ViewModel en setUp()
        // Verificamos que se llamó al caso de uso
        verify(exactly = 1) { createTokenUseCase() }
        verify(exactly = 1) { getTokenUseCase() }

        // Verificamos que el estado inicial tenga el token cargado
        assertEquals("1234", viewModel.uiState.value.textToken)
    }

    @Test
    fun setTextDescriptionUpdatesState() = runTest {
        val newDescription = "Accidente leve en la esquina"
        viewModel.setTextDescription(newDescription)
        assertEquals(newDescription, viewModel.uiState.value.textDescription)
    }

    @Test
    fun setPosMarkerUpdatesState() = runTest {
        val newPos = LatLng(10.0, -20.0)
        viewModel.setPosMarker(newPos)
        assertEquals(newPos, viewModel.uiState.value.posMarker)
    }

    @Test
    fun getReportsSuccessUpdatesStateWithList() = runTest {
        // Given
        val mockReports = listOf(
            ReportEntity(id = 1, latitude = 0.0, longitude = 0.0, date = "2023", type = "Choque", description = "Desc", token = 1234)
        )
        coEvery { selectReportUseCase() } returns Result.Success(mockReports)

        viewModel.uiState.test {
            // Estado inicial (consumir el del init)
            awaitItem()

            // When
            viewModel.getReports()

            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertTrue(successState.wasFound)
            assertEquals(mockReports, successState.itemsMarker)
            // Verificamos que el contador aumentó
            assertEquals(loadingState.operationCompletedCount + 1, successState.operationCompletedCount)
        }
    }

    @Test
    fun getReportsErrorUpdatesStateWithErrorMessage() = runTest {
        // Given
        val errorMessage = "Error de conexión"
        coEvery { selectReportUseCase() } returns Result.Error(Exception(errorMessage))

        viewModel.uiState.test {
            awaitItem() // Inicial

            // When
            viewModel.getReports()

            // Then
            awaitItem() // Loading
            val errorState = awaitItem()

            assertFalse(errorState.isLoading)
            assertFalse(errorState.wasFound)
            assertNull(errorState.itemsMarker)
            assertEquals(errorMessage, errorState.error)
        }
    }

    @Test
    fun insertReportSuccessUpdatesWasInsertedFlag() = runTest {
        // Given
        // Simulamos datos necesarios en el estado
        viewModel.setTextDescription("Test Report")
        viewModel.setItemsComboBox(listOf("Type A"))
        viewModel.setIndexComboBox(0)

        coEvery { insertReportUseCase(any()) } returns Result.Success(Unit)

        viewModel.uiState.test {
            awaitItem() // Estado actual modificado

            // When
            viewModel.insertReport()

            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertTrue(successState.wasInserted)

            coVerify {
                insertReportUseCase(match {
                    it.description == "Test Report" && it.token == 1234 // 1234 viene del init
                })
            }
        }
    }

    @Test
    fun updateReportSuccessUpdatesWasUpdatedFlag() = runTest {
        // Given: Preparamos el estado simulando que hay reportes cargados y uno seleccionado
        val existingReport = ReportEntity(id = 5, latitude = 0.0, longitude = 0.0, date = "", type = "Old", description = "", token = 0)
        val reports = listOf(existingReport)

        // Mockeamos la carga inicial
        coEvery { selectReportUseCase() } returns Result.Success(reports)
        viewModel.getReports()
        testDispatcher.scheduler.advanceUntilIdle() // Esperamos que termine getReports

        // Configuramos edición
        viewModel.setIndexMarker(0) // Seleccionamos el primer reporte
        viewModel.setTextDescription("Updated Desc")
        viewModel.setItemsComboBox(listOf("New Type"))
        viewModel.setIndexComboBox(0)

        coEvery { updateReportUseCase(any(), any()) } returns Result.Success(Unit)

        viewModel.uiState.test {
            awaitItem() // Estado actual

            // When
            viewModel.updateReport()

            // Then
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val successState = awaitItem()
            assertTrue(successState.wasUpdated)

            coVerify {
                updateReportUseCase(eq(5), match { it.description == "Updated Desc" })
            }
        }
    }

    @Test
    fun deleteReportSuccessUpdatesWasDeletedFlag() = runTest {
        // Given: Reporte seleccionado
        val reportToDelete = ReportEntity(id = 10, latitude = 0.0, longitude = 0.0, date = "", type = "", description = "", token = 0)

        coEvery { selectReportUseCase() } returns Result.Success(listOf(reportToDelete))
        viewModel.getReports()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setIndexMarker(0)

        coEvery { deleteReportUseCase(any()) } returns Result.Success(Unit)

        viewModel.uiState.test {
            awaitItem() // Estado actual

            // When
            viewModel.deleteReport()

            // Then
            awaitItem() // Loading
            val successState = awaitItem()

            assertTrue(successState.wasDeleted)
            coVerify { deleteReportUseCase(10) }
        }
    }

    @Test
    fun enabledFormReturnsTrueOnlyWhenValid() = runTest {
        // Caso inicial: ComboBox index 0 y descripción vacía -> False
        viewModel.setIndexComboBox(0)
        viewModel.setTextDescription("")
        assertFalse(viewModel.enabledForm())

        // Caso: ComboBox > 0 pero descripción vacía -> False
        viewModel.setIndexComboBox(1)
        assertFalse(viewModel.enabledForm())

        // Caso: ComboBox > 0 y descripción llena -> True
        viewModel.setTextDescription("Some text")
        assertTrue(viewModel.enabledForm())
    }

    @Test
    fun verifyUserReturnsTrueIfTokenMatches() = runTest {
        // Given
        val myToken = 1234
        val otherToken = 9999
        val report = ReportEntity(id = 1, latitude = 0.0, longitude = 0.0, date = "", type = "", description = "", token = myToken)

        // Setup state
        coEvery { selectReportUseCase() } returns Result.Success(listOf(report))
        viewModel.getReports()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setIndexMarker(0)
        viewModel.setTextToken(myToken.toString()) // Token del usuario (cargado en init)

        // When/Then
        assertTrue(viewModel.verifyUser())

        // Change token to simulate mismatch
        viewModel.setTextToken(otherToken.toString())
        assertFalse(viewModel.verifyUser())
    }

    @Test
    fun clearFlagsResetsAllBooleans() = runTest {
        // Given
        // Aquí simulamos una operación exitosa previa
        insertReportSuccessUpdatesWasInsertedFlag()

        viewModel.uiState.test {
            awaitItem() // Estado actual modificado

            // When
            viewModel.clearFlags()

            // Then
            val state = awaitItem()
            assertFalse(state.wasFound)
            assertFalse(state.wasInserted)
            assertFalse(state.wasUpdated)
            assertFalse(state.wasDeleted)
        }
    }
}
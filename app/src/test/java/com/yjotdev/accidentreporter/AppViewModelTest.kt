package com.yjotdev.accidentreporter

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.entity.ReportEntity
import com.yjotdev.accidentreporter.domain.usecase.string.StringUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.DeleteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.InsertReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.SelectReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.UpdateReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.CreateTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.EditTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.GetTokenUseCase
import com.yjotdev.accidentreporter.application.mvvm.viewmodel.AppViewModel
import com.yjotdev.accidentreporter.application.navigation.UiEvent
import com.yjotdev.accidentreporter.application.navigation.ViewRoutes
import kotlinx.coroutines.test.advanceUntilIdle

/**
 * Pruebas unitarias para AppViewModel.
 * Se enfoca en validar la lógica de negocio, las actualizaciones de estado (UiState)
 * y la emisión de eventos de una sola vez (UiEvent).
 */
@ExperimentalCoroutinesApi
class AppViewModelTest {

    // Mocks para todos los casos de uso inyectados en el ViewModel.
    // Usamos @RelaxedMockK para evitar tener que definir un `every` para cada función.
    @RelaxedMockK
    private lateinit var getStringUseCase: StringUseCase
    @RelaxedMockK
    private lateinit var selectReportUseCase: SelectReportUseCase
    @RelaxedMockK
    private lateinit var insertReportUseCase: InsertReportUseCase
    @RelaxedMockK
    private lateinit var updateReportUseCase: UpdateReportUseCase
    @RelaxedMockK
    private lateinit var deleteReportUseCase: DeleteReportUseCase
    @RelaxedMockK
    private lateinit var createTokenUseCase: CreateTokenUseCase
    @RelaxedMockK
    private lateinit var getTokenUseCase: GetTokenUseCase
    @RelaxedMockK
    private lateinit var editTokenUseCase: EditTokenUseCase

    // La instancia del ViewModel que vamos a probar.
    private lateinit var viewModel: AppViewModel

    // Un TestDispatcher para controlar el hilo principal en las pruebas.
    private val testDispatcher = StandardTestDispatcher()

    /**
     * Configuración inicial para cada prueba.
     * Se ejecuta antes de cada test.
     */
    @Before
    fun setUp() {
        // Inicializa los mocks anotados en esta clase.
        MockKAnnotations.init(this)
        // Establece el dispatcher de prueba como el principal para controlar las corutinas.
        Dispatchers.setMain(testDispatcher)
        // Crea la instancia del ViewModel con los mocks.
        viewModel = AppViewModel(
            getString = getStringUseCase,
            selectReportUseCase = selectReportUseCase,
            insertReportUseCase = insertReportUseCase,
            updateReportUseCase = updateReportUseCase,
            deleteReportUseCase = deleteReportUseCase,
            createTokenUseCase = createTokenUseCase,
            getTokenUseCase = getTokenUseCase,
            editTokenUseCase = editTokenUseCase
        )
    }

    /**
     * Limpieza después de cada prueba.
     * Se ejecuta al finalizar cada test.
     */
    @After
    fun tearDown() {
        // Restablece el dispatcher principal a su estado original.
        Dispatchers.resetMain()
        // Limpia todos los mocks y sus configuraciones.
        unmockkAll()
    }

    @Test
    fun whenGetReportsIsSuccessfulThenUiStateIsUpdatedWithDataAndNavigationEventIsSent() = runTest {
        // Given: Preparamos el escenario
        val fakeReportList = listOf(ReportEntity(
            id = 1,
            latitude = -3.245274,
            longitude = -79.832028,
            date = "2023-09-04",
            type = "Accidentes",
            description = "Test Report",
            token = 123456
        ))
        coEvery { selectReportUseCase() } returns Result.Success(fakeReportList)

        // Then
        val job1 = launch {
            viewModel.eventChannel.test {
                // Verificamos que se envió el evento de navegación correcto
                assertEquals(UiEvent.Navigate(ViewRoutes.Map.name), awaitItem())
            }
        }
        val job2 = launch {
            viewModel.uiState.test {
                val loadingState = awaitItem() // Estado de carga de datos
                assertTrue(loadingState.isLoading)

                val successState = awaitItem() // Estado final con los datos
                assertFalse(successState.isLoading)
                assertEquals(fakeReportList, successState.itemsMarker)
            }
        }

        // When: Ejecutamos la acción a probar
        viewModel.getReports()

        // Ejecutamos las corrutinas
        advanceUntilIdle()

        // Esperamos a que se completen las corrutinas
        job1.cancel()
        job2.cancel()

        // Verificamos que el caso de uso fue llamado una vez
        coVerify(exactly = 1) { selectReportUseCase() }
    }

    @Test
    fun whenGetReportsFailsThenUiStateIsUpdatedAndLogEventIsSent() = runTest {
        // Given
        val errorMessage = "Network Error"
        coEvery { selectReportUseCase() } returns Result.Error(Exception(errorMessage))

        // Then
        val job1 = launch {
            viewModel.eventChannel.test {
                assertEquals(UiEvent.ShowLog(errorMessage), awaitItem())
            }
        }
        val job2 = launch {
            viewModel.uiState.test {
                val loadingState = awaitItem() // Estado de carga de datos
                assertTrue(loadingState.isLoading)

                val errorState = awaitItem() // Estado final con el error
                assertFalse(errorState.isLoading)
                assertNull(errorState.itemsMarker)
            }
        }

        // When
        viewModel.getReports()

        // Ejecutamos las corrutinas
        advanceUntilIdle()

        // Esperamos a que se completen las corrutinas
        job1.cancel()
        job2.cancel()

        coVerify(exactly = 1) { selectReportUseCase() }
    }

    @Test
    fun whenInsertReportIsSuccessfulThenToastEventIsSent() = runTest {
        // Given
        val successMessage = "Report inserted"
        coEvery { insertReportUseCase(any()) } returns Result.Success(Unit)
        every { getStringUseCase(R.string.toast_insert_true) } returns successMessage

        viewModel.setItemsComboBox(
            listOf(
                "Seleccione un tipo de incidente",
                "Accidentes",
                "Trafico",
                "Problemas en servicios publicos"
            )
        )
        viewModel.setIndexComboBox(1)
        viewModel.setTextDescription("Una descripción válida")

        // Then
        val job1 = launch {
            viewModel.eventChannel.test {
                assertEquals(UiEvent.ShowToast(successMessage), awaitItem())
            }
        }
        val job2 = launch {
            viewModel.uiState.test {
                val loadingState = awaitItem() // Estado de insertar datos
                assertTrue(loadingState.isLoading)

                val successState = awaitItem() // Estado final de inserción exitosa
                assertFalse(successState.isLoading)
            }
        }

        // When
        viewModel.insertReport()

        // Ejecutamos las corrutinas
        advanceUntilIdle()

        // Esperamos a que se completen las corrutinas
        job1.cancel()
        job2.cancel()

        coVerify(exactly = 1) { insertReportUseCase(any()) }
    }

    @Test
    fun whenDeleteReportFailsThenToastAndLogEventsAreSent() = runTest {
        // Given
        val errorMessage = "Deletion failed"
        val toastMessage = "Error deleting report"
        // Simulamos que ya hay un reporte cargado en el estado
        val reports = listOf(
            ReportEntity(
                id = 1,
                latitude = -3.245274,
                longitude = -79.832028,
                date = "2023-09-04",
                type = "Accidentes",
                description = "Test Report 1",
                token = 123456),
            ReportEntity(
                id = 2,
                latitude = -3.456789,
                longitude = -79.986745,
                date = "2023-10-14",
                type = "Trafico",
                description = "Test Report 2",
                token = 456567)
        )
        viewModel.setItemsMarker(reports)
        viewModel.setIndexMarker(0)

        coEvery { deleteReportUseCase(reports[0].id) } returns Result.Error(Exception(errorMessage))
        every { getStringUseCase(R.string.toast_delete_false) } returns toastMessage

        // Then
        val job1 = launch {
            viewModel.eventChannel.test {
                assertEquals(UiEvent.ShowToast(toastMessage), awaitItem())
                assertEquals(UiEvent.ShowLog(errorMessage), awaitItem())
            }
        }
        val job2 = launch {
            viewModel.uiState.test {
                val loadingState = awaitItem() // Estado de eliminar datos
                assertTrue(loadingState.isLoading)

                val successState = awaitItem() // Estado final con el error
                assertFalse(successState.isLoading)
            }
        }

        // When
        viewModel.deleteReport()

        // Ejecutamos las corrutinas
        advanceUntilIdle()

        // Esperamos a que se completen las corrutinas
        job1.cancel()
        job2.cancel()

        coVerify(exactly = 1) { deleteReportUseCase(reports[0].id) }
    }

    @Test
    fun whenSetTextDescriptionIsCalledThenUiStateUpdatesCorrectly() = runTest {
        // Given
        val newDescription = "A new description for the report."

        // When
        viewModel.setTextDescription(newDescription)

        // Then
        viewModel.uiState.test {
            val updatedState = awaitItem()
            assertEquals(newDescription, updatedState.textDescription)
        }
    }

    @Test
    fun whenEnabledFormIsCalledItReturnsTrueIfDescriptionAndComboboxAreValid() {
        // Given
        viewModel.setTextDescription("Valid description")
        viewModel.setIndexComboBox(1) // 0 es la opción por defecto, 1 es una opción válida

        // When & Then
        assertTrue(viewModel.enabledForm())
    }

    @Test
    fun whenEnabledFormIsCalledItReturnsFalseIfDescriptionIsBlank() {
        // Given
        viewModel.setTextDescription("  ") // Descripción en blanco
        viewModel.setIndexComboBox(1)

        // When & Then
        assertFalse(viewModel.enabledForm())
    }

    @Test
    fun whenEnabledFormIsCalledItReturnsFalseIfComboboxIsNotSelected() {
        // Given
        viewModel.setTextDescription("Valid description")
        viewModel.setIndexComboBox(0) // Opción por defecto (no válida)

        // When & Then
        assertFalse(viewModel.enabledForm())
    }

    @Test
    fun whenVerifyUserReturnsTrueIfTokenMatchesTheSelectedMarkerSToken() {
        // Given
        val userToken = "123456"
        val reports = listOf(
            ReportEntity(
                id = 1,
                latitude = -3.245274,
                longitude = -79.832028,
                date = "2023-09-04",
                type = "Accidentes",
                description = "Test Report 1",
                token = 123456),
            ReportEntity(
                id = 2,
                latitude = -3.456789,
                longitude = -79.986745,
                date = "2023-10-14",
                type = "Trafico",
                description = "Test Report 2",
                token = 456567)
        )
        viewModel.setTextToken(userToken)
        viewModel.setItemsMarker(reports)
        viewModel.setIndexMarker(0) // Selecciona el primer reporte

        // When & Then
        assertTrue(viewModel.verifyUser())
    }

    @Test
    fun whenVerifyUserReturnsFalseIfTokenDoesNotMatch() {
        // Given
        val userToken = "999999"
        val reports = listOf(
            ReportEntity(
                id = 1,
                latitude = -3.245274,
                longitude = -79.832028,
                date = "2023-09-04",
                type = "Accidentes",
                description = "Test Report 1",
                token = 123456),
            ReportEntity(
                id = 2,
                latitude = -3.456789,
                longitude = -79.986745,
                date = "2023-10-14",
                type = "Trafico",
                description = "Test Report 2",
                token = 456567)
        )
        viewModel.setTextToken(userToken)
        viewModel.setItemsMarker(reports)
        viewModel.setIndexMarker(0)

        // When & Then
        assertFalse(viewModel.verifyUser())
    }
}
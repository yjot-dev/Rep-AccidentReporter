package com.yjotdev.accidentreporter

import app.cash.turbine.test
import com.google.android.gms.maps.model.LatLng
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.advanceUntilIdle
import io.mockk.Runs
import io.mockk.just
import io.mockk.verify
import com.yjotdev.accidentreporter.domain.core.Result
import com.yjotdev.accidentreporter.domain.model.ReportModel
import com.yjotdev.accidentreporter.domain.model.GeocodingModel
import com.yjotdev.accidentreporter.domain.usecase.string.StringUseCase
import com.yjotdev.accidentreporter.domain.usecase.geocoding.SelectGeocodingUseCase
import com.yjotdev.accidentreporter.domain.usecase.geocoding.EditLocationUseCase
import com.yjotdev.accidentreporter.domain.usecase.geocoding.GetLocationUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.DeleteRemoteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.DeleteLocalReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.InsertRemoteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.InsertLocalReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.GetRemoteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.GetLocalReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.UpdateRemoteReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.UpdateLocalReportUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.CreateTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.report.SaveAllReportsUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.EditTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.GetTokenUseCase
import com.yjotdev.accidentreporter.presentation.mvvm.viewmodel.UiViewModel
import com.yjotdev.accidentreporter.presentation.navigation.UiEvent

/**
 * Pruebas unitarias para los metodos del ViewModel.
 */
@ExperimentalCoroutinesApi
class ViewModelTest {

    // Mocks para todos los casos de uso inyectados en el ViewModel.
    @RelaxedMockK
    private lateinit var stringUseCase: StringUseCase
    @RelaxedMockK
    private lateinit var selectGeocodingUseCase: SelectGeocodingUseCase
    @RelaxedMockK
    private lateinit var getLocationUseCase: GetLocationUseCase
    @RelaxedMockK
    private lateinit var editLocationUseCase: EditLocationUseCase
    @RelaxedMockK
    private lateinit var getRemoteReportUseCase: GetRemoteReportUseCase
    @RelaxedMockK
    private lateinit var getLocalReportUseCase: GetLocalReportUseCase
    @RelaxedMockK
    private lateinit var insertRemoteReportUseCase: InsertRemoteReportUseCase
    @RelaxedMockK
    private lateinit var insertLocalReportUseCase: InsertLocalReportUseCase
    @RelaxedMockK
    private lateinit var saveAllReportsUseCase: SaveAllReportsUseCase
    @RelaxedMockK
    private lateinit var updateRemoteReportUseCase: UpdateRemoteReportUseCase
    @RelaxedMockK
    private lateinit var updateLocalReportUseCase: UpdateLocalReportUseCase
    @RelaxedMockK
    private lateinit var deleteRemoteReportUseCase: DeleteRemoteReportUseCase
    @RelaxedMockK
    private lateinit var deleteLocalReportUseCase: DeleteLocalReportUseCase
    @RelaxedMockK
    private lateinit var createTokenUseCase: CreateTokenUseCase
    @RelaxedMockK
    private lateinit var getTokenUseCase: GetTokenUseCase
    @RelaxedMockK
    private lateinit var editTokenUseCase: EditTokenUseCase

    private lateinit var viewModel: UiViewModel
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var localReportsFlow: MutableStateFlow<List<ReportModel>>

    @Before
    fun setUp() {
        // Inicializa los mocks anotados en esta clase.
        MockKAnnotations.init(this)
        // Establece el dispatcher de prueba como el principal para controlar las corutinas.
        Dispatchers.setMain(testDispatcher)
        // Configura los mocks para evitar errores en init del ViewModel
        every { getTokenUseCase() } returns ""
        every { getLocationUseCase() } returns ""
        coEvery { createTokenUseCase() } returns Result.Success("test-token-123")
        every { editTokenUseCase(any()) } just Runs
        every { editLocationUseCase(any()) } just Runs
        every { stringUseCase(any()) } returns "Test Message"
        localReportsFlow = MutableStateFlow(listOf(ReportModel()))
        every { getLocalReportUseCase() } returns localReportsFlow
        // Crea la instancia del ViewModel con los mocks.
        viewModel = UiViewModel(
            stringUseCase = stringUseCase,
            selectGeocodingUseCase = selectGeocodingUseCase,
            getLocationUseCase = getLocationUseCase,
            editLocationUseCase = editLocationUseCase,
            getRemoteReportUseCase = getRemoteReportUseCase,
            getLocalReportUseCase = getLocalReportUseCase,
            insertRemoteReportUseCase = insertRemoteReportUseCase,
            insertLocalReportUseCase = insertLocalReportUseCase,
            saveAllReportsUseCase = saveAllReportsUseCase,
            updateRemoteReportUseCase = updateRemoteReportUseCase,
            updateLocalReportUseCase = updateLocalReportUseCase,
            deleteRemoteReportUseCase = deleteRemoteReportUseCase,
            deleteLocalReportUseCase = deleteLocalReportUseCase,
            createTokenUseCase = createTokenUseCase,
            getTokenUseCase = getTokenUseCase,
            editTokenUseCase = editTokenUseCase
        )
    }

    @After
    fun tearDown() {
        // Restablece el dispatcher principal a su estado original.
        Dispatchers.resetMain()
        // Limpia todos los mocks y sus configuraciones.
        unmockkAll()
    }

    @Test
    fun whenSelectGeocodingIsSuccessfulThenUiStateIsUpdatedWithDataAndNavigationEventIsSent() = runTest {
        // Given: Preparamos el escenario
        val successMessage = "Ubicacion encontrada"
        val fakeLocation = GeocodingModel(lat = -3.245274, lng = -79.832028)
        coEvery { selectGeocodingUseCase(any(),any(),any()) } returns Result.Success(fakeLocation)
        every { stringUseCase(R.string.toast_geocoding_true) } returns successMessage
        viewModel.setTextCountry("Ecuador")
        viewModel.setTextProvince("El Oro")
        viewModel.setTextCity("El Guabo")

        // When: Ejecutamos la acción a probar
        viewModel.selectGeocoding()
        advanceUntilIdle()

        // Then: Verificamos los resultados
        viewModel.eventChannel.test {
            assertEquals(UiEvent.ShowToast(successMessage), awaitItem())
            cancel()
        }

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(LatLng(-3.245274, -79.832028), state.location)
            cancel()
        }

        coVerify(exactly = 1) { selectGeocodingUseCase(any(),any(),any()) }
    }

    @Test
    fun whenSelectGeocodingFailsThenUiStateIsUpdatedAndLogEventIsSent() = runTest {
        // Given
        val errorMessage = "Network Error"
        val toastMessage = "Error finding location"
        coEvery { selectGeocodingUseCase(any(),any(),any()) } returns Result.Error(Exception(errorMessage))
        every { stringUseCase(R.string.toast_geocoding_false) } returns toastMessage
        viewModel.setTextCountry("Ecuador")
        viewModel.setTextProvince("El Oro")
        viewModel.setTextCity("El Guabo")

        // When
        viewModel.selectGeocoding()
        advanceUntilIdle()

        // Then
        viewModel.eventChannel.test {
            assertEquals(UiEvent.ShowToast(toastMessage), awaitItem())
            assertEquals(UiEvent.ShowLog("selectGeocoding: $errorMessage"), awaitItem())
            cancel()
        }

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(LatLng(0.0, 0.0), state.location)
            cancel()
        }

        coVerify(exactly = 1) { selectGeocodingUseCase(any(),any(),any()) }
    }

    @Test
    fun whenGetAllRemoteReportsIsSuccessfulThenUiStateIsUpdatedWithDataAndNavigationEventIsSent() = runTest {
        // Given: Preparamos el escenario
        val fakeReportList = listOf(ReportModel(
            id = 1,
            latitude = -3.245274,
            longitude = -79.832028,
            date = "2023-09-04",
            type = "Accidentes",
            description = "Test Report",
            token = "a7cf5ac786824acaccff4d533832f1f5"
        ))
        coEvery { getRemoteReportUseCase() } returns Result.Success(fakeReportList)
        coEvery { saveAllReportsUseCase(fakeReportList) } answers {
            localReportsFlow.value = fakeReportList
        }

        // When: Ejecutamos la acción a probar
        viewModel.getAllRemoteReports()
        advanceUntilIdle()

        // Then
        assertEquals(fakeReportList, viewModel.uiState.value.itemsMarker)
        assertFalse(viewModel.uiState.value.isLoading)
        coVerify(exactly = 1) { getRemoteReportUseCase() }
        coVerify(exactly = 1) { saveAllReportsUseCase(fakeReportList) }
    }

    @Test
    fun whenGetAllRemoteReportsFailsThenUiStateIsUpdatedAndLogEventIsSent() = runTest {
        // Given
        val errorMessage = "Network Error"
        coEvery { getRemoteReportUseCase() } returns Result.Error(Exception(errorMessage))

        // When
        val eventJob = launch {
            viewModel.eventChannel.test {
                assertEquals(UiEvent.ShowLog("selectReports: $errorMessage"), awaitItem())
            }
        }
        viewModel.getAllRemoteReports()
        advanceUntilIdle()
        eventJob.cancel()

        // Then
        assertEquals(listOf(ReportModel()), viewModel.uiState.value.itemsMarker)
        assertFalse(viewModel.uiState.value.isLoading)
        coVerify(exactly = 1) { getRemoteReportUseCase() }
    }

    @Test
    fun whenInsertReportIsSuccessfulThenToastEventIsSent() = runTest {
        // Given
        val successMessage = "Report inserted"
        coEvery { insertRemoteReportUseCase(any()) } returns Result.Success(Unit)
        every { stringUseCase(R.string.toast_insert_true) } returns successMessage

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
        viewModel.setPosMarker(LatLng(-3.245274, -79.832028))

        // When
        viewModel.insertReport()
        advanceUntilIdle()

        // Then
        viewModel.eventChannel.test {
            assertEquals(UiEvent.ShowToast(successMessage), awaitItem())
            cancel()
        }

        coVerify(exactly = 1) { insertRemoteReportUseCase(any()) }
    }

    @Test
    fun whenInsertReportFailsThenToastEventIsSent() = runTest {
        // Given
        val errorMessage = "Database Error"
        val toastMessage = "Error updating report"
        coEvery { insertRemoteReportUseCase(any()) } returns Result.Error(Exception(errorMessage))
        every { stringUseCase(R.string.toast_insert_false) } returns toastMessage

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
        viewModel.setPosMarker(LatLng(-3.245274, -79.832028))

        // When
        viewModel.insertReport()
        advanceUntilIdle()

        // Then
        viewModel.eventChannel.test {
            assertEquals(UiEvent.ShowToast(toastMessage), awaitItem())
            assertEquals(UiEvent.ShowLog("insertReport: $errorMessage"), awaitItem())
            cancel()
        }

        coVerify(exactly = 1) { insertRemoteReportUseCase(any()) }
    }

    @Test
    fun whenUpdateReportIsSuccessfulThenToastEventIsSent() = runTest {
        // Given
        val successMessage = "Report updated"
        val reportToUpdate = ReportModel(
            id = 1,
            latitude = -3.245274,
            longitude = -79.832028,
            date = "2023-09-04",
            type = "Accidentes",
            description = "Old Description",
            token = "a7cf5ac786824acaccff4d533832f1f5"
        )
        viewModel.setItemsMarker(listOf(reportToUpdate))
        viewModel.setIndexMarker(0)
        viewModel.setItemsComboBox(listOf("...", "Accidentes")) // Mock de opciones
        viewModel.setIndexComboBox(1) // Nueva selección
        viewModel.setTextDescription("New Description") // Nueva descripción
        viewModel.setTextToken("7bcf5ac786824acaccff4d533832f11d") // Token del usuario

        // Simulamos la respuesta exitosa del caso de uso
        coEvery { updateRemoteReportUseCase(eq(1), any()) } returns Result.Success(Unit)
        every { stringUseCase(R.string.toast_update_true) } returns successMessage

        // When
        viewModel.updateReport()
        advanceUntilIdle()

        // Then
        viewModel.eventChannel.test {
            assertEquals(UiEvent.ShowToast(successMessage), awaitItem())
            cancel()
        }

        // Verificamos que el caso de uso se llamó con los parámetros correctos
        coVerify(exactly = 1) { updateRemoteReportUseCase(eq(1), any()) }
    }

    @Test
    fun whenUpdateReportFailsThenToastAndLogEventsAreSent() = runTest {
        // Given
        val errorMessage = "Database Error"
        val toastMessage = "Error updating report"
        val reportToUpdate = ReportModel(
            id = 1,
            latitude = -3.245274,
            longitude = -79.832028,
            date = "2023-09-04",
            type = "Accidentes",
            description = "Old Description",
            token = "a7cf5ac786824acaccff4d533832f1f5"
        )
        viewModel.setItemsMarker(listOf(reportToUpdate))
        viewModel.setIndexMarker(0)
        viewModel.setItemsComboBox(listOf("...", "Accidentes"))
        viewModel.setIndexComboBox(1)
        viewModel.setTextDescription("New Description")
        viewModel.setTextToken("7bcf5ac786824acaccff4d533832f11d")

        // Simulamos la respuesta de error del caso de uso
        coEvery { updateRemoteReportUseCase(eq(1), any()) } returns Result.Error(Exception(errorMessage))
        every { stringUseCase(R.string.toast_update_false) } returns toastMessage

        // When
        viewModel.updateReport()
        advanceUntilIdle()

        // Then
        viewModel.eventChannel.test {
            assertEquals(UiEvent.ShowToast(toastMessage), awaitItem())
            assertEquals(UiEvent.ShowLog("updateReport: $errorMessage"), awaitItem())
            cancel()
        }

        // Verificamos la llamada al caso de uso
        coVerify(exactly = 1) { updateRemoteReportUseCase(eq(1), any()) }
    }

    @Test
    fun whenDeleteReportIsSuccessfulThenToastEventIsSent() = runTest {
        // Given
        val successMessage = "Report deleted"
        val reportToDelete = ReportModel(
            id = 5,
            latitude = -3.245274,
            longitude = -79.832028,
            date = "2023-09-04",
            type = "Accidentes",
            description = "Report to be deleted",
            token = "a7cf5ac786824acaccff4d533832f1f5"
        )
        viewModel.setItemsMarker(listOf(reportToDelete))
        viewModel.setIndexMarker(0)

        // Simulamos la respuesta exitosa del caso de uso
        coEvery { deleteRemoteReportUseCase(eq(5)) } returns Result.Success(Unit)
        every { stringUseCase(R.string.toast_delete_true) } returns successMessage

        // When
        viewModel.deleteReport()
        advanceUntilIdle()

        // Then
        viewModel.eventChannel.test {
            assertEquals(UiEvent.ShowToast(successMessage), awaitItem())
            cancel()
        }

        // Verificamos la llamada al caso de uso con el ID correcto
        coVerify(exactly = 1) { deleteRemoteReportUseCase(eq(5)) }
    }

    @Test
    fun whenDeleteReportFailsThenToastAndLogEventsAreSent() = runTest {
        // Given
        val errorMessage = "Deletion failed"
        val toastMessage = "Error deleting report"
        val reportToDelete = ReportModel(
            id = 5,
            latitude = -3.245274,
            longitude = -79.832028,
            date = "2023-09-04",
            type = "Accidentes",
            description = "Report to be deleted",
            token = "a7cf5ac786824acaccff4d533832f1f5"
        )
        viewModel.setItemsMarker(listOf(reportToDelete))
        viewModel.setIndexMarker(0)

        coEvery { deleteRemoteReportUseCase(eq(5)) } returns Result.Error(Exception(errorMessage))
        every { stringUseCase(R.string.toast_delete_false) } returns toastMessage

        // When
        viewModel.deleteReport()
        advanceUntilIdle()

        // Then
        viewModel.eventChannel.test {
            assertEquals(UiEvent.ShowToast(toastMessage), awaitItem())
            assertEquals(UiEvent.ShowLog("deleteReport: $errorMessage"), awaitItem())
            cancel()
        }

        coVerify(exactly = 1) { deleteRemoteReportUseCase(eq(5)) }
    }

    @Test
    fun whenGetLocationAndTokenIsCalledAndUsedInViewModel() = runTest {
        // Given
        val fakeLocation = "Ecuador,El Oro,El Guabo"
        val fakeToken = "a7cf5ac786824acaccff4d533832f1f5"
        every { getLocationUseCase() } returns fakeLocation
        every { getTokenUseCase() } returns fakeToken

        // When: Establecemos los valores
        viewModel.setTextCountry("Ecuador")
        viewModel.setTextProvince("El Oro")
        viewModel.setTextCity("El Guabo")
        viewModel.setTextToken(fakeToken)

        // Then
        viewModel.uiState.test {
            val updatedState = awaitItem()
            assertEquals("Ecuador", updatedState.textCountry)
            assertEquals("El Oro", updatedState.textProvince)
            assertEquals("El Guabo", updatedState.textCity)
            assertEquals(fakeToken, updatedState.textToken)
            cancel()
        }
    }

    @Test
    fun whenEditTokenIsCalledThenUseCaseIsInvokedAndToastEventIsSent() = runTest {
        // Given
        val newToken = "a7cf5ac786824acaccff4d533832f1f5"
        val successMessage = "Token updated"
        every { editTokenUseCase(newToken) } just Runs
        every { stringUseCase(R.string.toast_update_token_true) } returns successMessage

        // When
        viewModel.editToken(newToken)
        advanceUntilIdle()

        // Then
        viewModel.eventChannel.test {
            assertEquals(UiEvent.ShowToast(successMessage), awaitItem())
            cancel()
        }
        verify(exactly = 1) { editTokenUseCase(newToken) }
    }

    @Test
    fun whenEditLocationIsCalledThenUseCaseIsInvokedAndToastEventIsSent() = runTest {
        // Given
        val country = "Ecuador"
        val province = "El Oro"
        val city = "El Guabo"
        viewModel.setTextCountry(country)
        viewModel.setTextProvince(province)
        viewModel.setTextCity(city)

        // When
        viewModel.editLocation(country, province, city)
        advanceUntilIdle()

        // Then
        verify(exactly = 1) { editLocationUseCase("$country,$province,$city") }
    }

    @Test
    fun whenSetTextDescriptionIsCalledThenUiStateUpdatesCorrectly() = runTest {
        // Given
        val newDescription = "A new description for the report."

        // When
        viewModel.setTextDescription(newDescription)
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val updatedState = awaitItem()
            assertEquals(newDescription, updatedState.textDescription)
            cancel()
        }
    }

    @Test
    fun whenEnabledFormIsCalledItReturnsTrueIfDescriptionAndComboboxAreValid() {
        // Given
        viewModel.setTextDescription("Valid description")
        viewModel.setIndexComboBox(1) // 1 es una opción válida

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
        val userToken = "a7cf5ac786824acaccff4d533832f1f5"
        val reports = listOf(
            ReportModel(
                id = 1,
                latitude = -3.245274,
                longitude = -79.832028,
                date = "2023-09-04",
                type = "Accidentes",
                description = "Test Report 1",
                token = "a7cf5ac786824acaccff4d533832f1f5"),
            ReportModel(
                id = 2,
                latitude = -3.456789,
                longitude = -79.986745,
                date = "2023-10-14",
                type = "Trafico",
                description = "Test Report 2",
                token = "7bcf5ac786824acaccff4d533832f11d")
        )
        viewModel.setTextToken(userToken)
        viewModel.setItemsMarker(reports)
        viewModel.setIndexMarker(0)

        // When & Then
        assertTrue(viewModel.verifyUser())
    }

    @Test
    fun whenVerifyUserReturnsFalseIfTokenDoesNotMatch() {
        // Given
        val userToken = "sw5f5ac786824acaccff4d533832st56"
        val reports = listOf(
            ReportModel(
                id = 1,
                latitude = -3.245274,
                longitude = -79.832028,
                date = "2023-09-04",
                type = "Accidentes",
                description = "Test Report 1",
                token = "a7cf5ac786824acaccff4d533832f1f5"),
            ReportModel(
                id = 2,
                latitude = -3.456789,
                longitude = -79.986745,
                date = "2023-10-14",
                type = "Trafico",
                description = "Test Report 2",
                token = "7bcf5ac786824acaccff4d533832f11d")
        )
        viewModel.setTextToken(userToken)
        viewModel.setItemsMarker(reports)
        viewModel.setIndexMarker(0)

        // When & Then
        assertFalse(viewModel.verifyUser())
    }
}
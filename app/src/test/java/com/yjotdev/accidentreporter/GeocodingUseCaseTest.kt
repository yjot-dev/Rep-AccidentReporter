package com.yjotdev.accidentreporter

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import com.yjotdev.accidentreporter.domain.usecase.geocoding.EditLocationUseCase
import com.yjotdev.accidentreporter.domain.usecase.geocoding.GetLocationUseCase
import com.yjotdev.accidentreporter.domain.usecase.geocoding.SelectGeocodingUseCase
import com.yjotdev.accidentreporter.domain.repository.GeocodingRepository
import com.yjotdev.accidentreporter.domain.model.GeocodingModel
import com.yjotdev.accidentreporter.domain.core.Result

/**
 * Pruebas unitarias para los casos de uso relacionados con Geocoding y la ubicación local.
 */
@ExperimentalCoroutinesApi
class GeocodingUseCaseTest {

    private lateinit var geocodingRepository: GeocodingRepository
    private lateinit var selectGeocodingUseCase: SelectGeocodingUseCase
    private lateinit var getLocationUseCase: GetLocationUseCase
    private lateinit var editLocationUseCase: EditLocationUseCase

    @Before
    fun setUp() {
        // Inicializamos el mock antes de cada test
        geocodingRepository = mockk()
        // Creamos las instancias de los casos de uso con el mock
        selectGeocodingUseCase = SelectGeocodingUseCase(geocodingRepository)
        getLocationUseCase = GetLocationUseCase(geocodingRepository)
        editLocationUseCase = EditLocationUseCase(geocodingRepository)
    }

    @Test
    fun whenSelectGeocodingUseCaseIsInvokedSuccessfullyThenItReturnGeocoding() = runTest {
        // Given: Configuramos el mock para que devuelva un resultado exitoso
        val fakeResponse = GeocodingModel(-38.5678, -69.5633)
        coEvery { geocodingRepository.selectGeocoding("Ecuador", "El Oro", "El Guabo") } returns Result.Success(fakeResponse)

        // When: Invocamos el caso de uso
        val result = selectGeocodingUseCase("Ecuador", "El Oro", "El Guabo")

        // Then: Verificamos que el resultado es el esperado
        assertEquals(fakeResponse, (result as Result.Success).data)
        coVerify(exactly = 1) { geocodingRepository.selectGeocoding("Ecuador", "El Oro", "El Guabo") }
    }

    @Test
    fun whenSelectGeocodingUseCaseIsInvokedWithErrorThenReturnException() = runTest {
        // Given: Configuramos el mock para que devuelva un resultado fallido
        val fakeResponse = Exception("Error al obtener el geocoding")
        coEvery { geocodingRepository.selectGeocoding("Ecuador", "El Oro", "El Guabo") } returns Result.Error(fakeResponse)

        // When: Invocamos el caso de uso
        val result = selectGeocodingUseCase("Ecuador", "El Oro", "El Guabo")

        // Then: Verificamos que el resultado es el esperado
        assertEquals(fakeResponse, (result as Result.Error).exception)
        coVerify(exactly = 1) { geocodingRepository.selectGeocoding("Ecuador", "El Oro", "El Guabo") }
    }

    @Test
    fun whenGetLocationUseCaseIsInvokedThenReturnLocation() {
        // Given
        val expectedLocation = "Ecuador,El Oro,El Guabo"
        every { geocodingRepository.getLocation() } returns expectedLocation

        // When
        val actualLocation = getLocationUseCase()

        // Then
        assertEquals(expectedLocation, actualLocation)
        verify(exactly = 1) { geocodingRepository.getLocation() }
    }

    @Test
    fun whenEditLocationUseCaseIsInvokedThenPortMethodIsCalled() {
        // Given
        val newLocation = "Ecuador,Pichincha,Quito"
        every { geocodingRepository.editLocation(newLocation) } returns Unit

        // When
        val result = editLocationUseCase(newLocation)

        // Then
        assertEquals(Unit, result)
        verify(exactly = 1) { geocodingRepository.editLocation(newLocation) }
    }
}
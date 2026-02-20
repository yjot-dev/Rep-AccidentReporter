package com.yjotdev.accidentreporter

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import com.yjotdev.accidentreporter.domain.usecase.geocoding.EditLocationUseCase
import com.yjotdev.accidentreporter.domain.usecase.geocoding.GetLocationUseCase
import com.yjotdev.accidentreporter.domain.usecase.geocoding.SelectGeocodingUseCase
import com.yjotdev.accidentreporter.domain.port.GeocodingPort
import com.yjotdev.accidentreporter.domain.entity.GeocodingEntity
import com.yjotdev.accidentreporter.domain.core.Result

/**
 * Pruebas unitarias para los casos de uso relacionados con Geocoding y la ubicación local.
 */
@ExperimentalCoroutinesApi
class GeocodingUseCaseTest {

    private lateinit var geocodingPort: GeocodingPort

    private lateinit var selectGeocodingUseCase: SelectGeocodingUseCase
    private lateinit var getLocationUseCase: GetLocationUseCase
    private lateinit var editLocationUseCase: EditLocationUseCase

    @Before
    fun setUp() {
        // Inicializamos el mock antes de cada test
        geocodingPort = mockk()
        // Creamos las instancias de los casos de uso con el mock
        selectGeocodingUseCase = SelectGeocodingUseCase(geocodingPort)
        getLocationUseCase = GetLocationUseCase(geocodingPort)
        editLocationUseCase = EditLocationUseCase(geocodingPort)
    }

    @Test
    fun whenSelectGeocodingUseCaseIsInvokedThenPortMethodIsCalledAndReturnsData() = runTest {
        // Given: Configuramos el mock para que devuelva un resultado exitoso
        val fakeResponse: GeocodingEntity = mockk()
        coEvery { geocodingPort.selectGeocoding(any(), any(), any()) } returns Result.Success(fakeResponse)

        // When: Invocamos el caso de uso
        val result = selectGeocodingUseCase("Ecuador", "El Oro", "El Guabo")

        // Then: Verificamos que el resultado es el esperado y que se llamó al puerto
        assertEquals(fakeResponse, (result as Result.Success).data)
        coVerify(exactly = 1) { geocodingPort.selectGeocoding("Ecuador", "El Oro", "El Guabo") }
    }

    @Test
    fun whenGetLocationUseCaseIsInvokedThenItReturnsLocationFromPort() {
        // Given
        val expectedLocation = "Ecuador,El Oro,El Guabo"
        every { geocodingPort.getLocation() } returns expectedLocation

        // When
        val actualLocation = getLocationUseCase()

        // Then
        assertEquals(expectedLocation, actualLocation)
        verify(exactly = 1) { geocodingPort.getLocation() }
    }

    @Test
    fun whenEditLocationUseCaseIsInvokedThenPortMethodIsCalledWithCorrectData() {
        // Given
        val newLocation = "Ecuador,Pichincha,Quito"
        every { geocodingPort.editLocation(newLocation) } just runs

        // When
        editLocationUseCase(newLocation)

        // Then
        verify(exactly = 1) { geocodingPort.editLocation(newLocation) }
    }
}
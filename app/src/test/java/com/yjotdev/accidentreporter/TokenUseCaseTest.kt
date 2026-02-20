package com.yjotdev.accidentreporter

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.yjotdev.accidentreporter.domain.port.TokenPort
import com.yjotdev.accidentreporter.domain.usecase.token.CreateTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.EditTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.GetTokenUseCase

/**
 * Pruebas unitarias para los casos de uso relacionados con el Token.
 */
class TokenUseCaseTest {

    private lateinit var tokenPort: TokenPort

    private lateinit var createTokenUseCase: CreateTokenUseCase
    private lateinit var getTokenUseCase: GetTokenUseCase
    private lateinit var editTokenUseCase: EditTokenUseCase

    @Before
    fun setUp() {
        tokenPort = mockk()
        createTokenUseCase = CreateTokenUseCase(tokenPort)
        getTokenUseCase = GetTokenUseCase(tokenPort)
        editTokenUseCase = EditTokenUseCase(tokenPort)
    }

    @Test
    fun whenCreateTokenUseCaseIsInvokedThenPortCreateTokenIsCalled() {
        // Given
        every { tokenPort.createToken() } just runs

        // When
        createTokenUseCase()

        // Then
        verify(exactly = 1) { tokenPort.createToken() }
    }

    @Test
    fun whenGetTokenUseCaseIsInvokedThenItReturnsTokenFromPort() {
        // Given
        val expectedToken = 123456
        every { tokenPort.getToken() } returns expectedToken

        // When
        val actualToken = getTokenUseCase()

        // Then
        assertEquals(expectedToken, actualToken)
        verify(exactly = 1) { tokenPort.getToken() }
    }

    @Test
    fun whenEditTokenUseCaseIsInvokedThenPortEditTokenIsCalledWithCorrectToken() {
        // Given
        val newToken = 654321
        every { tokenPort.editToken(newToken) } just runs

        // When
        editTokenUseCase(newToken)

        // Then
        verify(exactly = 1) { tokenPort.editToken(newToken) }
    }
}

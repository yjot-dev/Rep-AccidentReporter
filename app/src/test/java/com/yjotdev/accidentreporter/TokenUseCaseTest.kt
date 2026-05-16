package com.yjotdev.accidentreporter

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.yjotdev.accidentreporter.domain.repository.TokenRepository
import com.yjotdev.accidentreporter.domain.usecase.token.CreateTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.EditTokenUseCase
import com.yjotdev.accidentreporter.domain.usecase.token.GetTokenUseCase

/**
 * Pruebas unitarias para los casos de uso relacionados con el Token.
 */
class TokenUseCaseTest {

    private lateinit var tokenRepository: TokenRepository

    private lateinit var createTokenUseCase: CreateTokenUseCase
    private lateinit var getTokenUseCase: GetTokenUseCase
    private lateinit var editTokenUseCase: EditTokenUseCase

    @Before
    fun setUp() {
        tokenRepository = mockk()
        createTokenUseCase = CreateTokenUseCase(tokenRepository)
        getTokenUseCase = GetTokenUseCase(tokenRepository)
        editTokenUseCase = EditTokenUseCase(tokenRepository)
    }

    @Test
    fun whenCreateTokenUseCaseIsInvokedThenPortCreateTokenIsCalled() {
        // Given
        every { tokenRepository.createToken() } just runs

        // When
        createTokenUseCase()

        // Then
        verify(exactly = 1) { tokenRepository.createToken() }
    }

    @Test
    fun whenGetTokenUseCaseIsInvokedThenItReturnsTokenFromPort() {
        // Given
        val expectedToken = 123456
        every { tokenRepository.getToken() } returns expectedToken

        // When
        val actualToken = getTokenUseCase()

        // Then
        assertEquals(expectedToken, actualToken)
        verify(exactly = 1) { tokenRepository.getToken() }
    }

    @Test
    fun whenEditTokenUseCaseIsInvokedThenPortEditTokenIsCalledWithCorrectToken() {
        // Given
        val newToken = 654321
        every { tokenRepository.editToken(newToken) } just runs

        // When
        editTokenUseCase(newToken)

        // Then
        verify(exactly = 1) { tokenRepository.editToken(newToken) }
    }
}

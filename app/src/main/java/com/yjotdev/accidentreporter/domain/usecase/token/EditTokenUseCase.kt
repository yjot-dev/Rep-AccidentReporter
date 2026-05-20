package com.yjotdev.accidentreporter.domain.usecase.token

import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.repository.TokenRepository

class EditTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    operator fun invoke(token: String) {
        return tokenRepository.editToken(token)
    }
}
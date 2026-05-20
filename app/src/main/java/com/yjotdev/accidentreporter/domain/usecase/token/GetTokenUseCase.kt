package com.yjotdev.accidentreporter.domain.usecase.token

import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.repository.TokenRepository

class GetTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    operator fun invoke(): String {
        return tokenRepository.getToken()
    }
}
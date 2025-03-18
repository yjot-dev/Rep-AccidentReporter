package com.yjotdev.accidentreporter.domain.usecase

import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.port.TokenRepository

@Singleton
class CreateTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    operator fun invoke(){
        tokenRepository.createToken()
    }
}
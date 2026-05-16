package com.yjotdev.accidentreporter.domain.usecase.token

import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.repository.TokenRepository

@Singleton
class CreateTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    operator fun invoke(){
        tokenRepository.createToken()
    }
}
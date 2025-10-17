package com.yjotdev.accidentreporter.domain.usecase

import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.port.TokenPort

@Singleton
class CreateTokenUseCase @Inject constructor(
    private val tokenPort: TokenPort
) {
    operator fun invoke(){
        tokenPort.createToken()
    }
}
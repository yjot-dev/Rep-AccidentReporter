package com.yjotdev.accidentreporter.domain.usecase.token

import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.port.TokenPort

@Singleton
class EditTokenUseCase @Inject constructor(
    private val tokenPort: TokenPort
) {
    operator fun invoke(token: Int) {
        return tokenPort.editToken(token)
    }
}
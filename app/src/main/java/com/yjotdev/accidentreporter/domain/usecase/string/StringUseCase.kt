package com.yjotdev.accidentreporter.domain.usecase.string

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.port.StringPort

@Singleton
class StringUseCase @Inject constructor(
    private val stringPort: StringPort
) {
    operator fun invoke(resId: Int): String {
        return stringPort.getString(resId)
    }

    operator fun invoke(resId: Int, vararg args: Any): String {
        return stringPort.getString(resId, *args)
    }
}
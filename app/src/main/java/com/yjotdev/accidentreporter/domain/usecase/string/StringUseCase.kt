package com.yjotdev.accidentreporter.domain.usecase.string

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.repository.StringRepository

@Singleton
class StringUseCase @Inject constructor(
    private val stringRepository: StringRepository
) {
    operator fun invoke(resId: Int): String {
        return stringRepository.getString(resId)
    }

    operator fun invoke(resId: Int, vararg args: Any): String {
        return stringRepository.getString(resId, *args)
    }
}
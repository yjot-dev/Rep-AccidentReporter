package com.yjotdev.accidentreporter.utils.repositories

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.repository.StringRepository

@Singleton
class FakeStringRepositoryImpl @Inject constructor(): StringRepository {
    override fun getString(resId: Int): String {
        return "Id del recurso: $resId"
    }

    override fun getString(resId: Int, vararg args: Any): String {
        return "Id del recurso: $resId y argumentos: $args"
    }
}
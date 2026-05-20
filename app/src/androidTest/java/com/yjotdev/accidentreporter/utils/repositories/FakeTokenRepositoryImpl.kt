package com.yjotdev.accidentreporter.utils.repositories

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.repository.TokenRepository

@Singleton
class FakeTokenRepositoryImpl @Inject constructor(): TokenRepository {
    private val tokenStorage = mutableMapOf<String, String>()

    override fun getToken(): String {
        return tokenStorage["token"] ?: ""
    }

    override fun editToken(token: String) {
        tokenStorage["token"] = token
    }
}
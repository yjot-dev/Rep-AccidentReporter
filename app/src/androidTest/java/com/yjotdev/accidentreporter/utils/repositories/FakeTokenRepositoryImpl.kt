package com.yjotdev.accidentreporter.utils.repositories

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.port.TokenRepository

@Singleton
class FakeTokenRepositoryImpl @Inject constructor(): TokenRepository {
    private val tokenStorage = mutableMapOf<String, Int>()

    override fun createToken(){
        if (getToken() == 0) {
            val tokenRandom = 1000000000
            tokenStorage["token"] = tokenRandom
        }
    }

    override fun getToken(): Int {
        return tokenStorage["token"] ?: 0
    }
}
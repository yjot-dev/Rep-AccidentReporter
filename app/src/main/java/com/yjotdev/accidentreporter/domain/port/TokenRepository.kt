package com.yjotdev.accidentreporter.domain.port

interface TokenRepository {

    fun createToken()

    fun getToken(): Int
}
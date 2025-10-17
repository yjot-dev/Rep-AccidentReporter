package com.yjotdev.accidentreporter.domain.port

interface TokenPort {

    fun createToken()

    fun getToken(): Int
}
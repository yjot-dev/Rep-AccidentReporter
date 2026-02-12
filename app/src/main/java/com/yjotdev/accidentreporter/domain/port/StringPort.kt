package com.yjotdev.accidentreporter.domain.port

interface StringPort {
    fun getString(resId: Int): String
    fun getString(resId: Int, vararg args: Any): String
}
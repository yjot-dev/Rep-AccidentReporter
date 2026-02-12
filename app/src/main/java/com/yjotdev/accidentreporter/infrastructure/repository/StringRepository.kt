package com.yjotdev.accidentreporter.infrastructure.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.port.StringPort

@Singleton
class StringRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : StringPort {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg args: Any): String {
        return context.getString(resId, *args)
    }
}
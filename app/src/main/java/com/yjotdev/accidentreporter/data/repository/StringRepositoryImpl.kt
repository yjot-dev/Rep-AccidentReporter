package com.yjotdev.accidentreporter.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.accidentreporter.domain.repository.StringRepository

@Singleton
class StringRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : StringRepository {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg args: Any): String {
        return context.getString(resId, *args)
    }
}
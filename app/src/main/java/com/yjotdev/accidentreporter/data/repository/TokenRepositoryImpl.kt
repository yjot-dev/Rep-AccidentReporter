package com.yjotdev.accidentreporter.data.repository

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.repository.TokenRepository

@Singleton
class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : TokenRepository {
    private val sharedPreferences = context.getSharedPreferences("save_token", Context.MODE_PRIVATE)

    override fun getToken(): String {
        return sharedPreferences.getString("token", "") ?: ""
    }

    override fun editToken(token: String) {
        sharedPreferences.edit { putString("token", token) }
    }
}
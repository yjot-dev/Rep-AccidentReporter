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

    override fun createToken() {
        if (getToken() == 0) {
            val tokenRandom = (100000000..999999999).random()
            sharedPreferences.edit { putInt("token", tokenRandom) }
        }
    }

    override fun getToken(): Int {
        return sharedPreferences.getInt("token", 0)
    }

    override fun editToken(token: Int) {
        sharedPreferences.edit { putInt("token", token) }
    }
}
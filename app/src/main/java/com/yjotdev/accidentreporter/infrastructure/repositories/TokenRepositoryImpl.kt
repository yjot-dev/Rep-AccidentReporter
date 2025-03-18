package com.yjotdev.accidentreporter.infrastructure.repositories

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.port.TokenRepository

@Singleton
class TokenRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenRepository {
    private val sharedPreferences = context.getSharedPreferences("save_token", Context.MODE_PRIVATE)

    /** Crea un token **/
    override fun createToken() {
        if (getToken() == 0) {
            val tokenRandom = (1000000000..9999999999).random().toInt()
            sharedPreferences.edit().putInt("token", tokenRandom).apply()
        }
    }

    /** Obtiene el token **/
    override fun getToken(): Int {
        return sharedPreferences.getInt("token", 0)
    }
}
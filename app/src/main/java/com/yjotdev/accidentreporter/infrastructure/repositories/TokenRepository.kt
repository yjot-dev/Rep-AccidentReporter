package com.yjotdev.accidentreporter.infrastructure.repositories

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.accidentreporter.domain.port.TokenPort
import androidx.core.content.edit

@Singleton
class TokenRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenPort {
    private val sharedPreferences = context.getSharedPreferences("save_token", Context.MODE_PRIVATE)

    /** Crea un token **/
    override fun createToken() {
        if (getToken() == 0) {
            val tokenRandom = (1000000000..9999999999).random().toInt()
            sharedPreferences.edit { putInt("token", tokenRandom) }
        }
    }

    /** Obtiene el token **/
    override fun getToken(): Int {
        return sharedPreferences.getInt("token", 0)
    }
}
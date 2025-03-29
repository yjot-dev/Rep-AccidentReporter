package com.yjotdev.accidentreporter.infrastructure.adapter

import android.content.Context
import android.util.Log
import com.yjotdev.accidentreporter.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object Client {
    private val loggingInterceptor = HttpLoggingInterceptor{ msm ->
        Log.d("OkHttp", msm)
    }.apply { level = HttpLoggingInterceptor.Level.BODY }

    /** Cliente para app en producción **/
    fun getSafeClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(loggingInterceptor)
        .build()

    /** Cliente para app en desarrollo **/
    fun getUnsafeClient(context: Context): OkHttpClient {
        return try {
            // Leer el certificado desde res/raw
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val inputStream: InputStream = context.resources.openRawResource(R.raw.mycert)
            val certificate = certificateFactory.generateCertificate(inputStream) as X509Certificate
            inputStream.close()
            // Cargar el KeyStore con certificados confiables (si tienes un certificado personalizado, cámbialo aquí)
            val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
                load(null, null) // Inicia un KeyStore vacío
                setCertificateEntry("my_certificate", certificate)
            }
            // Inicializar TrustManagerFactory con el KeyStore
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                init(keyStore)
            }
            // Obtener el TrustManager
            val trustManagers = trustManagerFactory.trustManagers
            val trustManager = trustManagers[0] as X509TrustManager
            // Crear un SSLContext con el TrustManager
            val sslContext = SSLContext.getInstance("TLS").apply {
                init(null, arrayOf(trustManager), null)
            }
            // Construye el cliente OkHttp
            OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory, trustManager)
                .hostnameVerifier { _, _ -> true }
                .addInterceptor(HeaderInterceptor())
                .addInterceptor(loggingInterceptor)
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
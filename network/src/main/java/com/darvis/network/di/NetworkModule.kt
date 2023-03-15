package com.darvis.network.di

import android.util.Log
import com.darvis.network.helpers.TrustAllX509TrustManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import javax.inject.Singleton
import javax.net.ssl.SSLContext

private const val TIME_OUT = 15000L //15 seconds

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    private val TAG = NetworkModule::class.simpleName

    @Provides
    @Singleton
    fun provideKtorHttpClient() = HttpClient(Android) {
        //How we want to log our apis
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d(TAG, message)
                }
            }
        }

        //How we want to serialize our data, could be via Moshi , Gson, KotlinxSerializer etc.
        install(ContentNegotiation) {
            json(json = provideSerializer())
        }


        install(ResponseObserver) {
            onResponse { httpResponse ->
                when (httpResponse.status.value) {
                    in 300..399 -> throw RedirectResponseException(
                        response = httpResponse, cachedResponseText = httpResponse.toString()
                    )
                    in 400..499 -> throw ClientRequestException(
                        response = httpResponse, cachedResponseText = httpResponse.toString()
                    )
                    in 500..599 -> throw ServerResponseException(
                        response = httpResponse, cachedResponseText = httpResponse.toString()
                    )
                }
            }
        }

        install(DefaultRequest) {
            accept(ContentType.Application.Json)// Sends a accept header of to application/json
        }

        install(UserAgent) {
            agent = "mobile"
        }

        install(HttpTimeout) {
            connectTimeoutMillis = TIME_OUT
            requestTimeoutMillis = TIME_OUT
            socketTimeoutMillis = TIME_OUT
        }

        //Bypass https certificate validation
        engine {
            sslManager = { httpsURLConnection ->
                httpsURLConnection.sslSocketFactory = SSLContext.getInstance("TLS").apply {
                    this.init(null, arrayOf(TrustAllX509TrustManager()), SecureRandom())
                }.socketFactory
                httpsURLConnection.setHostnameVerifier { _, _ -> true }
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideSerializer() = Json {
        isLenient = true
        prettyPrint = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }

}
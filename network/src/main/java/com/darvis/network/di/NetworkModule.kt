package com.darvis.network.di

import android.content.Context
import android.util.Log
import com.darvis.network.helpers.TrustAllX509TrustManager
import com.darvis.network.remote.Request
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import java.security.SecureRandom
import javax.inject.Singleton
import javax.net.ssl.SSLContext

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    const val Baseurl = "172.16.20.161"
    private const val TIME_OUT = 15000L //15 seconds
    private val TAG = NetworkModule::class.simpleName

    @Singleton
    @Provides
    fun provideKtorHttpClient(jsonSerializer: KotlinxSerializer) = HttpClient(Android) {
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
        install(JsonFeature) {
            serializer = jsonSerializer
        }

/*        //TODO: Configure and load tokens from local storage
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens("accessToken", "refreshToken")
                }
            }
        }*/

        install(ResponseObserver) {
            onResponse { httpResponse ->
                Log.d(TAG, httpResponse.toString())
                onResponse {
                    if (it.status == HttpStatusCode.Unauthorized) {
                        //navigateToLogin()
                    }
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
                httpsURLConnection.setHostnameVerifier { hostname, session -> true }
            }
        }
    }

    @Singleton
    @Provides
    fun provideSerializer() = KotlinxSerializer(kotlinx.serialization.json.Json {
        isLenient = true
        prettyPrint = true
        ignoreUnknownKeys = true
    })


    @Singleton
    @Provides
    fun provideApiService(httpClient: HttpClient, @ApplicationContext context: Context) =
        Request()
}
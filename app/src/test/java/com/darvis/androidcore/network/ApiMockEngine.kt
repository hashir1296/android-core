package com.darvis.androidcore.network


import com.darvis.androidcore.LoginMockResponse
import com.darvis.androidcore.PostMockResponse
import com.darvis.network.di.NetworkModule
import com.darvis.network.models.LoginResponse
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.JsonConfiguration

private const val TIME_OUT = 15000L

class ApiMockEngine {

    fun get() = client

    private val responseHeaders =
        headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))

    private val client = HttpClient(MockEngine) {
        HttpResponseValidator {
            validateResponse { response: HttpResponse ->
                val statusCode = response.status.value
                when (statusCode) {
                    in 300..399 -> throw RedirectResponseException(
                        response = response, cachedResponseText = response.toString()
                    )
                    in 400..499 -> throw ClientRequestException(
                        response = response, cachedResponseText = response.toString()
                    )
                    in 500..599 -> throw ServerResponseException(
                        response = response, cachedResponseText = response.toString()
                    )
                }
                if (statusCode >= 600) {
                    throw ResponseException(
                        response = response, cachedResponseText = response.toString()
                    )
                }
            }
        }
        install(UserAgent) {
            agent = "mobile"
        }

        //How we want to serialize our data, could be via Moshi , Gson, KotlinxSerializer etc.
        install(ContentNegotiation) {
            json(json = NetworkModule.provideSerializer())
        }

        install(DefaultRequest) {
            accept(ContentType.Application.Json)// Sends a accept header of to application/json
        }

        engine {
            addHandler { request ->
                when (request.url.encodedPath) {
                    "/posts" -> {
                        respond(PostMockResponse(), HttpStatusCode.OK, responseHeaders)
                    }
                    "/users" -> {
                        respond(UsersMockResponse(), HttpStatusCode.OK, responseHeaders)
                    }
                    "/token" ->{
                        respond(LoginMockResponse(), HttpStatusCode.OK, responseHeaders)
                    }
                    else -> {
                        error("Unhandled ${request.url.encodedPath}")
                    }
                }
            }
        }


        install(HttpTimeout) {
            connectTimeoutMillis = TIME_OUT
            requestTimeoutMillis = TIME_OUT
            socketTimeoutMillis = TIME_OUT
        }

    }
}
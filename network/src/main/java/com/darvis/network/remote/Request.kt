package com.darvis.network.remote

import android.content.Context
import android.util.Log
import com.darvis.network.R
import com.darvis.network.models.ErrorModel
import com.darvis.network.models.NetworkResult
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.socket.client.IO
import io.socket.client.SocketIOException
import io.socket.emitter.Emitter
import kotlinx.serialization.json.Json
import javax.inject.Inject

private const val TAG = "Network Request"

class Request {
    private lateinit var serviceUrl: Url
    private lateinit var method: HttpMethod
    private var queryParams: HashMap<String, String>? = null
    private var additionalHeaders: HashMap<String, String>? = null
    private var contentType: ContentType = ContentType(
        contentType = ContentType.Application.Json.contentType,
        ContentType.Application.Json.contentSubtype
    )
    private var formUrlEncodedParams: HashMap<String, String>? = null
    private var requestBody: Any? = null

    @Inject
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var context: Context

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun httpMethod(
        httpMethod: HttpMethod,
    ) = apply {
        method = httpMethod
    }

    fun url(
        host: String, port: Int, protocol: URLProtocol = URLProtocol.HTTP, endPoint: String
    ) = apply {
        serviceUrl = URLBuilder(
            protocol = protocol, host = host, port = port, pathSegments = listOf(endPoint)
        ).build()
    }

    fun queryParams(paramsMap: HashMap<String, String>) = apply {
        if (paramsMap.isNotEmpty()) {
            queryParams = paramsMap
        }
    }

    fun contentType(type: ContentType) = apply {
        contentType = type
    }

    fun additionalHeaders(additionalHeadersMap: HashMap<String, String>) = apply {
        if (additionalHeadersMap.isNotEmpty()) {
            additionalHeaders = additionalHeadersMap
        }
    }

    fun requestBody(body: Any?) = apply {
        this@Request.requestBody = body
    }

    fun formUrlEncodedParams(paramsMap: HashMap<String, String>) = apply {
        if (paramsMap.isNotEmpty()) {
            formUrlEncodedParams = paramsMap
        }
    }

    suspend fun send(): NetworkResult<HttpResponse, ErrorModel> {
        return try {
            val apiResponse = httpClient.request {
                //Set method
                method = this@Request.method
                //Set url and add query params if any
                url(
                    scheme = serviceUrl.protocol.name,
                    port = serviceUrl.port,
                    host = serviceUrl.host,
                    path = serviceUrl.encodedPath,
                ) {
                    queryParams?.let {
                        if (it.isNotEmpty()) {
                            it.forEach { entry ->
                                parameters.append(entry.key, entry.value)
                            }
                        }
                    }
                }
                additionalHeaders?.let {
                    if (it.isNotEmpty()) {
                        it.entries.forEach { entry ->
                            headers.append(entry.key, entry.value)
                        }
                    }
                }

                //Set token
                headers.append(
                    "Authorization", SessionManager.provideAccessTokenWithBearer()
                )

                contentType.let { type ->
                    headers.append(type.contentType, type.contentSubtype)
                }

                requestBody?.let {
                    setBody(it)
                }

                formUrlEncodedParams?.let {
                    if (it.isNotEmpty()) {
                        setBody(FormDataContent(Parameters.build {
                            it.forEach { entry ->
                                append(entry.key, entry.value)
                            }
                        }))
                    }
                }
            }
            NetworkResult.Success(apiResponse.body())
        } catch (ex: RedirectResponseException) {
            //3xx exceptions
            val errorModel = json.decodeFromString(
                ErrorModel.serializer(), ex.response.body()
            )
            NetworkResult.Error(
                message = ex.response.status.description,
                code = ex.response.status.value,
                errorBody = errorModel
            )
        } catch (ex: ClientRequestException) {
            //4xx exceptions
            val errorModel = json.decodeFromString(
                ErrorModel.serializer(), ex.response.body()
            )
            NetworkResult.Error(
                message = ex.response.status.description,
                code = ex.response.status.value,
                errorBody = errorModel
            )
        } catch (ex: ServerResponseException) {
            //5xx exceptions
            val errorModel = json.decodeFromString(
                ErrorModel.serializer(), ex.response.body()
            )
            NetworkResult.Error(
                message = ex.response.status.description,
                code = ex.response.status.value,
                errorBody = errorModel
            )
        } catch (ex: Exception) {
            NetworkResult.Error(
                message = ex.message ?: context.getString(R.string.something_went_wrong),
                code = -1,
                errorBody = null
            )
        }
    }

    object Socket {
        private var mSocket: io.socket.client.Socket? = null
        fun initSocket(
            host: String, protocol: URLProtocol = URLProtocol.HTTP, port: Int
        ) = apply {
            val url = URLBuilder(
                protocol = protocol, host = host, port = port
            ).build().toURI()
            try {
                mSocket = IO.socket(url)
                Log.d(TAG, "Socket initialized successfully")
            } catch (ex: Exception) {
                Log.d(TAG, "Socket init exception: ${ex.localizedMessage}")
                ex.printStackTrace()
            }
        }

        private fun isSocketConnect(): Boolean = mSocket?.connected()
            ?: throw SocketIOException("Socket is null. Please initialize socket. Call initSocket() function first")


        fun connectSocket(
            emitterListener: Emitter.Listener, vararg events: String
        ) = apply {
            try {

                /*Mandatory events so we can have a better idea what state socket is in*/
                mSocket?.on(
                    io.socket.client.Socket.EVENT_CONNECT, emitterListener
                )
                mSocket?.on(
                    io.socket.client.Socket.EVENT_CONNECT_ERROR, emitterListener
                )
                mSocket?.on(
                    io.socket.client.Socket.EVENT_DISCONNECT, emitterListener
                )

                /*User events*/
                for (event in events) {
                    mSocket?.on(event, emitterListener)
                }

                if (isSocketConnect()) mSocket?.connect()
            } catch (ex: Exception) {
                Log.d(TAG, "Socket connect exception: ${ex.localizedMessage}")
                ex.printStackTrace()
            }
        }

        fun disconnectSocket() {
            mSocket?.disconnect()
            mSocket?.off()
        }

    }

}

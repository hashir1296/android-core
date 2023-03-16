@file:Suppress("unused")

package com.darvis.network.remote

import android.util.Log
import com.darvis.network.models.ErrorModel
import com.darvis.network.models.NetworkResult
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.pipeline.*
import io.socket.client.IO
import io.socket.client.SocketIOException
import io.socket.emitter.Emitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

private const val TAG = "Network Request"

class Request private constructor(
    val httpClient: HttpClient, val json: Json
) {

    lateinit var serviceUrl: Url
        private set
    lateinit var method: HttpMethod
        private set

    var queryParams: HashMap<String, String?>? = null
        private set
    var headers: HashMap<String, String>? = null
        private set
    var contentType: ContentType = ContentType(
        contentType = ContentType.Application.Json.contentType,
        ContentType.Application.Json.contentSubtype
    )
        private set
    var formUrlEncodedParams: HashMap<String, String>? = null
        private set
    var requestBody: Any? = null
        private set

    private fun clearRequest() = apply {
        this.queryParams = null
        this.headers = null
        this.formUrlEncodedParams = null
        this.requestBody = null
    }

    fun setHttpMethod(
        httpMethod: HttpMethod,
    ) = apply {
        method = httpMethod
    }

    fun setUrl(
        host: String, port: Int, protocol: URLProtocol = URLProtocol.HTTP, endPoint: String
    ) = apply {
        serviceUrl = URLBuilder(
            protocol = protocol, host = host, port = port, pathSegments = listOf(endPoint)
        ).build()
    }

    fun setQueryParams(paramsMap: HashMap<String, String?>) = apply {
        if (paramsMap.isNotEmpty()) {
            queryParams = paramsMap
        }
    }

    fun setContentType(type: ContentType) = apply {
        contentType = type
    }

    fun setHeaders(additionalHeadersMap: HashMap<String, String>) = apply {
        if (additionalHeadersMap.isNotEmpty()) {
            headers = additionalHeadersMap
        }
    }

    fun setRequestBody(body: Any?) = apply {
        requestBody = body
    }

    fun setFormUrlEncodedParams(paramsMap: HashMap<String, String>) = apply {
        if (paramsMap.isNotEmpty()) {
            formUrlEncodedParams = paramsMap
        }
    }

    class Builder {
        private lateinit var httpClient: HttpClient
        private lateinit var jsonSerializer: Json

        fun client(httpClient: HttpClient) = apply {
            this.httpClient = httpClient
        }

        fun serializer(json: Json) = apply {
            this.jsonSerializer = json
        }

        fun addRequestInterceptor(
            phase: PipelinePhase, block: PipelineInterceptor<Any, HttpRequestBuilder>
        ) = apply {
            httpClient.sendPipeline.intercept(phase = phase, block = block)
        }

        fun addResponseInterceptor(
            phase: PipelinePhase, block: PipelineInterceptor<HttpResponse, Unit>
        ) = apply {
            httpClient.receivePipeline.intercept(phase = phase, block = block)
        }

        fun build() = Request(
            httpClient = httpClient, json = jsonSerializer
        )

    }

    suspend fun send(): NetworkResult<HttpResponse, ErrorModel> {
        return try {
            if (::method.isInitialized.not() || ::serviceUrl.isInitialized.not()) throw UninitializedPropertyAccessException(
                "Make sure method and service url are initialized properly"
            )

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
                                //Only append entry if value is not null
                                entry.value?.let { value ->
                                    parameters.append(entry.key, value)
                                }
                            }
                        }
                    }
                }
                this@Request.headers?.let {
                    if (it.isNotEmpty()) {
                        it.entries.forEach { entry ->
                            headers.append(entry.key, entry.value)
                        }
                    }
                }

                contentType.let { type ->
                    headers.append(type.contentType, type.contentSubtype)
                }

                contentType(contentType)

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
            clearRequest()
            NetworkResult.Success(apiResponse.body())
        } catch (ex: RedirectResponseException) {
            //3xx exceptions
            clearRequest()
            val errorModel = json.decodeFromString(
                ErrorModel.serializer(), ex.response.body()
            )
            NetworkResult.Error(
                message = errorModel.message ?: ex.response.status.description,
                code = errorModel.code ?: ex.response.status.value,
                errorBody = errorModel
            )
        } catch (ex: ClientRequestException) {
            //4xx exceptions
            clearRequest()
            val errorModel = json.decodeFromString(
                ErrorModel.serializer(), ex.response.body()
            )
            NetworkResult.Error(
                message = errorModel.message ?: ex.response.status.description,
                code = errorModel.code ?: ex.response.status.value,
                errorBody = errorModel
            )
        } catch (ex: ServerResponseException) {
            //5xx exceptions
            clearRequest()
            val errorModel = json.decodeFromString(
                ErrorModel.serializer(), ex.response.body()
            )
            NetworkResult.Error(
                message = errorModel.message ?: ex.response.status.description,
                code = errorModel.code ?: ex.response.status.value,
                errorBody = errorModel
            )
        } catch (ex: Exception) {
            clearRequest()
            NetworkResult.Error(
                message = ex.message ?: "Something went wrong", code = -1, errorBody = null
            )
        }
    }


    suspend inline fun <reified T> HttpResponse.receiveAs(): T {
        return withContext(Dispatchers.IO) {
            try {
                this@receiveAs.body<T>()
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    throw ex
                }
            }
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

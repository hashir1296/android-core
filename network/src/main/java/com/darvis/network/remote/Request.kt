package com.darvis.network.remote

import android.content.Context
import com.darvis.network.R
import com.darvis.network.di.NetworkModule
import com.darvis.network.models.NetworkResult
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Inject

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

    fun httpMethod(
        httpMethod: HttpMethod,
    ) = apply {
        method = httpMethod
    }

    fun url(
        host: String = NetworkModule.Baseurl,
        port: Int,
        protocol: URLProtocol = URLProtocol.HTTP,
        endPoint: String
    ) = apply {
        serviceUrl = URLBuilder(
            protocol = protocol, host = host, port = port, encodedPath = endPoint
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

    suspend fun send(): NetworkResult<HttpResponse> {
        return try {
            NetworkResult.Success(httpClient.request {
                //Set method
                method = this@Request.method
                //Set url and add query params if any
                url(
                    scheme = serviceUrl.protocol.name,
                    port = serviceUrl.port,
                    host = serviceUrl.host,
                    path = serviceUrl.encodedPath
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
                headers.append("Authorization", SessionManager.provideAccessTokenWithBearer())

                contentType.let { type ->
                    headers.append(type.contentType, type.contentSubtype)
                }

                requestBody?.let {
                    body = it
                }

                formUrlEncodedParams?.let {
                    if (it.isNotEmpty()) {
                        body = FormDataContent(Parameters.build {
                            it.forEach { entry ->
                                append(entry.key, entry.value)
                            }
                        })
                    }
                }
            })
        } catch (ex: RedirectResponseException) {
            //3xx exceptions
            NetworkResult.Error(
                message = ex.response.status.description, code = ex.response.status.value
            )
        } catch (ex: ClientRequestException) {
            //4xx exceptions
            NetworkResult.Error(
                message = ex.response.status.description, code = ex.response.status.value
            )
        } catch (ex: ServerResponseException) {
            //5xx exceptions
            NetworkResult.Error(
                message = ex.response.status.description, code = ex.response.status.value
            )
        } catch (ex: Exception) {
            NetworkResult.Error(
                message = ex.message ?: context.getString(R.string.something_went_wrong), code = -1
            )
        }
    }
}

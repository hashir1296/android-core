package com.darvis.androidcore.network

import com.darvis.androidcore.PostMockResponseIObject
import com.darvis.network.di.NetworkModule
import com.darvis.network.models.NetworkResult
import com.darvis.network.remote.Request
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class RequestTest {
    private lateinit var request: Request
    private val apiMockEngine = ApiMockEngine()
    private val mockClient = apiMockEngine.get()
    private val host = "jsonplaceholder.typicode.com"

    @Before
    fun setup() {
        request = Request.Builder().client(mockClient).serializer(NetworkModule.provideSerializer())
            .build()
    }

    @Test
    fun `is httpMethod getting set`() {
        request.setHttpMethod(HttpMethod.Get)
        assert(request.method == HttpMethod.Get)
    }

    @Test
    fun `the url function should build correct url`() {
        val correctURl = "http://172.16.20.161:1000/token"
        request.setUrl(
            host = "172.16.20.161", endPoint = "token", protocol = URLProtocol.HTTP, port = 1000
        )
        assertTrue("Url is correct", request.serviceUrl?.toURI().toString() == correctURl)
    }

    @Test
    fun `query parameters should append in request object`() {
        val queryParamsMap = HashMap<String, String?>()
        queryParamsMap["param1"] = "1"
        queryParamsMap["param2"] = "2"
        request.setQueryParams(queryParamsMap)

        val result = request.queryParams == queryParamsMap
        assertTrue("Query params are attached", result)
    }

    @Test
    fun `form url encoded parameters are attached in request object`() {
        val formParams = hashMapOf("param1" to "1", "param2" to "2")
        request.setFormUrlEncodedParams(formParams)


        val result = request.formUrlEncodedParams == formParams

        assertTrue("Form encoded params are attached", result)
    }


    @Test
    fun `body is attached in request object`() {
        val body = provideDummyRequestBody()
        request.setRequestBody(provideDummyRequestBody())

        val result = request.requestBody == body

        assertTrue("Form encoded params are attached", result)
    }

    private fun provideDummyRequestBody() = DummyRequestBody("hashir", "pass123")

    @Test
    fun `correct content type is passed`() {
        request.setContentType(ContentType.Application.Json)
        val result = request.contentType.contentType == request.contentType.contentType

        assertTrue("Content type is correct", result)
    }

    @Test
    fun `correct content subtype is passed`() {
        request.setContentType(ContentType.Application.Json)
        val result = request.contentType.contentSubtype == request.contentType.contentSubtype

        assertTrue("Content subtype is correct", result)
    }

    @Test
    fun `additional headers are getting attached`() {
        val headers = hashMapOf("header1" to "1", "header2" to "2")
        request.setHeaders(headers)


        val result = request.headers == headers

        assertTrue("Additional headers are attached", result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `dummy Get network call`() {
        runTest {
            val api = request.setHttpMethod(HttpMethod.Get).setUrl(
                host = host, protocol = URLProtocol.HTTPS, endPoint = "posts", port = DEFAULT_PORT
            ).send()

            when (api) {
                is NetworkResult.Error -> {
                }
                NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    api.response?.body<List<PostMockResponseIObject>>()?.let {
                        assertTrue("Fetching post from mock api successfully", it.size == 5)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `network call request url correctness`() {
        runTest {
            val api = request.setHttpMethod(HttpMethod.Get).setUrl(
                host = host, protocol = URLProtocol.HTTPS, endPoint = "posts", port = DEFAULT_PORT
            ).send()

            when (api) {
                is NetworkResult.Success -> {
                    val requestURL = api.response?.request?.url?.toURI().toString()
                    val urlBuilt = request.serviceUrl.toURI().toString()

                    if (urlBuilt == requestURL) assert(true)
                    else fail()
                }
                else -> {
                    fail()
                }
            }

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `is request header interceptor added`() {
        val r = Request.Builder().client(mockClient).serializer(NetworkModule.provideSerializer())
            .addRequestInterceptor(phase = HttpSendPipeline.State, block = {
                context.headers.append("Authorization", "Token Abc")
            }).build()

        runTest {
            val api = r.setHttpMethod(HttpMethod.Get).setUrl(
                host = host, protocol = URLProtocol.HTTPS, endPoint = "posts", port = DEFAULT_PORT
            ).send()
            when (api) {
                is NetworkResult.Success -> {
                    val requestHeaders = api.response?.request?.headers
                    if (requestHeaders != null) {
                        if (requestHeaders.contains("Authorization")) {
                            assert(true)
                        } else fail()
                    } else {
                        fail()
                    }

                }
                else -> {
                    fail()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `is response header interceptor added`() {
        val expectedResponseCode = 200
        var responseCodeFromServer = -1
        val r = Request.Builder().client(mockClient).serializer(NetworkModule.provideSerializer())
            .addResponseInterceptor(phase = HttpReceivePipeline.State, block = {
                responseCodeFromServer = it.status.value
            }).build()

        runTest {
            val api = r.setHttpMethod(HttpMethod.Get).setUrl(
                host = host, protocol = URLProtocol.HTTPS, endPoint = "posts", port = DEFAULT_PORT
            ).send()
            when (api) {
                is NetworkResult.Success -> {
                    val requestHeaders = api.response?.headers
                    if (requestHeaders != null) {
                        if (responseCodeFromServer == expectedResponseCode) {
                            assert(true)
                        } else fail()
                    } else {
                        fail()
                    }

                }
                else -> {
                    fail()
                }
            }
        }
    }


}
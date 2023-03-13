package com.darvis.androidcore.network

import com.darvis.network.remote.Request
import io.ktor.http.*
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test


class RequestTest {
    private lateinit var request: Request

    @Before
    fun setup() {
        request = Request()
    }

    @Test
    fun `is httpMethod getting set`() {
        request.httpMethod(HttpMethod.Get)
        assert(request.method == HttpMethod.Get)
    }

    @Test
    fun `the url function should build correct url`() {
        val correctURl = "http://172.16.20.161:1000/token"
        request.url(
            host = "172.16.20.161", endPoint = "token", protocol = URLProtocol.HTTP, port = 1000
        )
        assertTrue("Url is correct", request.serviceUrl?.toURI().toString() == correctURl)
    }

    @Test
    fun `query parameters should appended in request object`() {
//        val correctURl = "http://172.16.20.161:1000/token?param1=1&param2=2"
        val queryParams = hashMapOf("param1" to "1", "param2" to "2")
        request.queryParams(queryParams)


        val result = request.queryParams == queryParams

        assertTrue("Query params are attached", result)
    }

    @Test
    fun `form url encoded parameters are attached in request object`() {
        val formParams = hashMapOf("param1" to "1", "param2" to "2")
        request.formUrlEncodedParams(formParams)


        val result = request.formUrlEncodedParams == formParams

        assertTrue("Form encoded params are attached", result)
    }


    @Test
    fun `body is attached in request object`() {
        val body = provideDummyRequestBody()
        request.requestBody(provideDummyRequestBody())

        val result = request.requestBody == body

        assertTrue("Form encoded params are attached", result)
    }

    private fun provideDummyRequestBody() = DummyRequestBody("hashir", "pass123")

    @Test
    fun `correct content type is passed`() {
        request.contentType(ContentType.Application.Json)
        val result = request.contentType.contentType == request.contentType.contentType

        assertTrue("Content type is correct",result)
    }

    @Test
    fun `correct content subtype is passed`() {
        request.contentType(ContentType.Application.Json)
        val result = request.contentType.contentSubtype == request.contentType.contentSubtype

        assertTrue("Content subtype is correct",result)
    }

    @Test
    fun `additional headers are getting attached`() {
        val headers = hashMapOf("header1" to "1", "header2" to "2")
        request.additionalHeaders(headers)


        val result = request.additionalHeaders == headers

        assertTrue("Additional headers are attached", result)
    }

}
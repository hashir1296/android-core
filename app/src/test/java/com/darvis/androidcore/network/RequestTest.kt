package com.darvis.androidcore.network

import com.darvis.network.remote.Request
import io.ktor.http.*
import org.junit.Before
import org.junit.Test


class RequestTest {
    private var request: Request = Request()

    @Before
    fun setup() {

    }

    @Test
    fun `url with empty host`() {
        val url =
            Url(URLBuilder(host = "", pathSegments = emptyList(), protocol = URLProtocol.HTTP))
        assert(url.)
    }

    @Test
    fun queryParams() {
    }

    @Test
    fun contentType() {
    }

    @Test
    fun additionalHeaders() {
    }

    @Test
    fun requestBody() {
    }

    @Test
    fun formUrlEncodedParams() {
    }

    @Test
    fun formMultiPartData() {
    }

    @Test
    fun sendAuthHeader() {
    }

    @Test
    fun send() {
    }

    @Test
    fun `aSas aS`() {
    }
}
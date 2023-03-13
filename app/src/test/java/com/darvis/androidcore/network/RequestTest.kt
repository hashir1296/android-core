package com.darvis.androidcore.network

import com.darvis.network.remote.Request
import io.ktor.http.*
import org.junit.Before
import org.junit.Test


class RequestTest {
    private lateinit var request: Request

    @Before
    fun setup() {
        request = Request()
    }

    /*Test if httpMethod() function is working as expected*/
    @Test
    fun httpMethod() {
        request.httpMethod(HttpMethod.Get)
        assert(request.method == HttpMethod.Get)
    }
}
package com.darvis.androidcore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.darvis.network.remote.Request
import io.ktor.http.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    /*private fun loginUserNetworkRequest() {
        CoroutineScope(Dispatchers.IO).launch {
            Request().GetRequest().setUrl()
            val api = Request().httpMethod(HttpMethod.Post).url(
                host = "172.16.20.161", protocol = URLProtocol.HTTP, endPoint = "token", port = 8090
            ).formUrlEncodedParams(
                hashMapOf(
                    "username" to "darvis", "password" to "password123"
                )
            ).contentType(ContentType.Application.FormUrlEncoded).sendAuthHeader(false).send()

            withContext(Dispatchers.Main) {
                when (api) {
                    is NetworkResult.Error -> {

                    }
                    NetworkResult.Loading -> {
                    }

                    is NetworkResult.Success -> {
                        api.response?.body<HttpResponse>()?.let {}
                    }
                }
            }
        }
    }*/
}
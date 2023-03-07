package com.darvis.androidcore

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.darvis.network.models.LoginResponse
import com.darvis.network.models.NetworkResult
import com.darvis.network.remote.Request
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loginUserNetworkRequest()
    }

    private fun loginUserNetworkRequest() {
        CoroutineScope(Dispatchers.IO).launch {
            val api = Request().httpMethod(HttpMethod.Post).url(
                host = "172.16.20.161", protocol = URLProtocol.HTTP, endPoint = "token", port = 8090
            ).contentType(ContentType.Application.FormUrlEncoded).formUrlEncodedParams(
                paramsMap = hashMapOf(
                    "username" to "darvis", "password" to "password123"
                )
            ).sendAuthHeader(false).send()

            withContext(Dispatchers.Main) {
                when (api) {
                    is NetworkResult.Error -> {

                    }
                    NetworkResult.Loading -> {
                    }

                    is NetworkResult.Success -> {
                        api.response?.body<LoginResponse>()?.let {
                            Log.d("Token", it.data?.accessToken ?: "")
                        }
                    }
                }
            }
        }
    }

}
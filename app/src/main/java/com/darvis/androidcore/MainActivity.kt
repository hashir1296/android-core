package com.darvis.androidcore

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.darvis.network.di.NetworkModule
import com.darvis.network.models.LoginResponse
import com.darvis.network.models.NetworkResult
import com.darvis.network.remote.Request
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //loginCall()
    }

    private fun loginCall() {
        val request = Request.Builder().serializer(NetworkModule.provideSerializer())
            .client(NetworkModule.provideKtorHttpClient())
            .addRequestInterceptor(HttpSendPipeline.State) {
                context.headers.append("Custom-header", "Wow")
            }.addResponseInterceptor(HttpResponsePipeline.Receive) {
                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(
                        this@MainActivity, context.response.status.description, Toast.LENGTH_SHORT
                    ).show()
                }, 100)
            }.build()

        CoroutineScope(Dispatchers.IO).launch {
            val api = request.setHttpMethod(HttpMethod.Post)
                .setUrl(host = "172.16.20.161", endPoint = "token", port = 8090).setContentType(
                    ContentType.Application.FormUrlEncoded
                ).setFormUrlEncodedParams(
                    hashMapOf(
                        "username" to "demo1", "password" to "Pass123?"
                    )
                ).send()

            withContext(Dispatchers.Main) {
                when (api) {
                    is NetworkResult.Error -> {
                        //Toast.makeText(this@MainActivity, api.message, Toast.LENGTH_SHORT).show()
                    }
                    NetworkResult.Loading -> {}
                    is NetworkResult.Success -> {
                        val body = api.response?.body<LoginResponse>()
                        Log.d("Api", body?.message ?: "")
                    }
                }
            }
        }
    }

}
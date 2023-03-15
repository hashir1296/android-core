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

    lateinit var request: Request
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //loginUserNetworkRequest()
    }

    private fun loginUserNetworkRequest() {
        CoroutineScope(Dispatchers.IO).launch {
        }
    }

}
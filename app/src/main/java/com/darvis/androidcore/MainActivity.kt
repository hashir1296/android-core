package com.darvis.androidcore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.darvis.network.models.LoginResponse
import com.darvis.network.models.NetworkResult
import com.darvis.network.remote.Request
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //loginUserNetworkRequest()
    }


}
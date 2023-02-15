package com.darvis.androidcore

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.darvis.network.helpers.EndPoints
import com.darvis.network.helpers.Ports
import com.darvis.network.models.NetworkResult
import com.darvis.network.remote.Request
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.http.*
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var request: Request

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
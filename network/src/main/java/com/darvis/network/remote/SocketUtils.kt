package com.darvis.network.remote

import android.util.Log
import io.ktor.http.*
import io.socket.client.IO
import io.socket.client.SocketIOException
import io.socket.emitter.Emitter

private const val TAG = "Network Socket"

object SocketUtils {
    var mSocket: io.socket.client.Socket? = null
        private set

    fun initSocket(
        host: String, protocol: URLProtocol = URLProtocol.HTTP, port: Int
    ) = apply {
        val url = URLBuilder(
            protocol = protocol, host = host, port = port
        ).build().toURI()
        try {
            mSocket = IO.socket(url)
            Log.d(TAG, "Socket initialized successfully")
        } catch (ex: Exception) {
            Log.d(TAG, "Socket init exception: ${ex.localizedMessage}")
            ex.printStackTrace()
        }
    }

    fun isSocketConnect(): Boolean = mSocket?.connected()
        ?: throw SocketIOException("Socket is null. Please initialize socket. Call initSocket() function first")


    fun connectSocket(
        emitterListener: Emitter.Listener, vararg events: String
    ) = apply {
        try {

            /*Mandatory events so we can have a better idea what state socket is in*/
            mSocket?.on(
                io.socket.client.Socket.EVENT_CONNECT, emitterListener
            )
            mSocket?.on(
                io.socket.client.Socket.EVENT_CONNECT_ERROR, emitterListener
            )
            mSocket?.on(
                io.socket.client.Socket.EVENT_DISCONNECT, emitterListener
            )

            /*User events*/
            for (event in events) {
                mSocket?.on(event, emitterListener)
            }

            if (isSocketConnect()) mSocket?.connect()
        } catch (ex: Exception) {
            Log.d(TAG, "Socket connect exception: ${ex.localizedMessage}")
            ex.printStackTrace()
        }
    }

    fun listenToEvents(vararg events: String, emitterListener: Emitter.Listener) {
        for (event in events) {
            mSocket?.on(event, emitterListener)
        }
    }

    fun disconnectSocket() {
        mSocket?.disconnect()
        mSocket?.off()
    }
}
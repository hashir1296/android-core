package com.darvis.network.models

@kotlinx.serialization.Serializable
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T?) : NetworkResult<T>()
    data class Error<T>(val message: String?, val code: Int? = null, val errorBody: T? = null) :
        NetworkResult<T>()
    class Loading<T>() : NetworkResult<T>()
}
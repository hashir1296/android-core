package com.darvis.network.models

@kotlinx.serialization.Serializable
sealed class NetworkResult<out T, out E> {
    data class Success<T>(val data: T?) : NetworkResult<T, Nothing>()
    data class Error<E>(val code: Int, val message: String? = null, val errorBody: E?) :
        NetworkResult<Nothing, E>()

    object Loading : NetworkResult<Nothing, Nothing>()
}
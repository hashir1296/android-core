package com.darvis.network.remote

object SessionManager {

    private var accessToken = ""
    private var refreshToken = ""

    fun resetTokens() = apply {
        this.accessToken = ""
        this.refreshToken = ""
    }

    fun setNewTokens(accessToken: String, refreshToken: String) = apply {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    fun provideAccessTokenWithBearer(): String = "Bearer $accessToken"

    fun provideAccessTokenWithoutBearer(): String = accessToken

    fun provideRefreshToken(): String = refreshToken

    fun isUserLoggedIn() = accessToken.isNotEmpty()

}
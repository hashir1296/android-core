package com.darvis.network.remote

object SessionManager {

    private var accessToken = ""
    private var refreshToken = ""

    fun resetTokens() {
        accessToken = ""
        refreshToken = ""
    }

    fun setNewTokens(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    fun provideAccessTokenWithBearer(): String {
        return "Bearer $accessToken"
    }

    fun provideAccessTokenWithoutBearer(): String {
        return accessToken
    }

    fun provideRefreshToken(): String {
        return refreshToken
    }

    fun isUserLoggedIn() = accessToken.isNotEmpty()

}
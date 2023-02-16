package com.darvis.data.helpers

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

    object DataStore {
        val BASE_URL = stringPreferencesKey("BASE_URL")
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")

    }
}
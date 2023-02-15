package com.darvis.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class DataStoreManager(val context: Context) {

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "APP_DATASTORE")
    private val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(name = "LANGUAGE_DATA_STORE")
    private val Context.urlDataStore: DataStore<Preferences> by preferencesDataStore(name = "URL_DATASTORE")


}
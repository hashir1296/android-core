package com.darvis.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStorePreferenceAPI {

    suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T,dataStore: DataStore<Preferences>): Flow<T>
    suspend fun <T> savePreference(
        key: Preferences.Key<T>, value: T, dataStore: DataStore<Preferences>
    )

    suspend fun <T> removePreference(key: Preferences.Key<T>, dataStore: DataStore<Preferences>)
    suspend fun clearAllPreference(dataStore: DataStore<Preferences>)
}
package com.darvis.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 *
 * User can store very secure types of data's like KEY etc..
 * This is Generic class created for all types of the DataTypes
 */
class DataStoreManager : DataStorePreferenceAPI {

    override suspend fun <T> getPreference(
        key: Preferences.Key<T>, defaultValue: T, dataStore: DataStore<Preferences>
    ): Flow<T> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val result = preferences[key] ?: defaultValue
            result
        }
    }

    override suspend fun <T> savePreference(
        key: Preferences.Key<T>, value: T, dataStore: DataStore<Preferences>
    ) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }


    override suspend fun <T> removePreference(
        key: Preferences.Key<T>, dataStore: DataStore<Preferences>
    ) {
        dataStore.edit {
            it.remove(key)
        }
    }

    override suspend fun clearAllPreference(dataStore: DataStore<Preferences>) {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
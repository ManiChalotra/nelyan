package com.nelyan_live.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoragePreference(context: Context) {

    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(name = "app_preferences")
    private val dataStore2: DataStore<Preferences> = applicationContext.createDataStore(name = "FCM")

    suspend fun <T> save(saveValue: T, KEY: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            if(preferences.contains(KEY)) preferences.remove(KEY)

            preferences[KEY] = saveValue
        }
    }
    suspend fun <T> saveFCM(saveValue: T, KEY: Preferences.Key<T>) {
        dataStore2.edit { preferences ->
            if(preferences.contains(KEY)) preferences.remove(KEY)

            preferences[KEY] = saveValue
        }
    }

    fun <T> emitStoredValue(value: Preferences.Key<T>): Flow<T> {
        return dataStore.data.catch {

            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[value] ?: null as T
        }
    }
    fun <T> emitStoredFCMValue(value: Preferences.Key<T>): Flow<T> {
        return dataStore2.data.catch {

            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[value] ?: null as T
        }
    }

  suspend  fun deleteDataBase(){
        dataStore.edit {
            it.clear()
        }
    }
}

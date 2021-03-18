package com.meherr.mehar.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoragePreference(context: Context) {

    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences>

    init {
        dataStore = applicationContext.createDataStore(
            name = "app_preferences"
        )
    }


    suspend fun <T> save(saveValue: T, KEY: Preferences.Key<T>) {
        dataStore.edit { preferences ->
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

  suspend  fun deleteDataBase(){
        dataStore.edit {
            it.clear()
        }
    }
}

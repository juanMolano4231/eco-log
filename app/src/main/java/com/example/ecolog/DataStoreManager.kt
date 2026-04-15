package com.example.ecolog

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ecolog_prefs")

class DataStoreManager(private val context: Context) {
    private val gson = Gson()
    private val ACTIVITIES_KEY = stringPreferencesKey("activities")

    val activitiesFlow: Flow<List<ActivityLog>> = context.dataStore.data.map { prefs ->
        val json = prefs[ACTIVITIES_KEY] ?: "[]"
        val type = object : TypeToken<List<ActivityLog>>() {}.type
        gson.fromJson(json, type) ?: emptyList()
    }

    suspend fun saveActivity(activity: ActivityLog) {
        context.dataStore.edit { prefs ->
            val currentJson = prefs[ACTIVITIES_KEY] ?: "[]"
            val type = object : TypeToken<MutableList<ActivityLog>>() {}.type
            val list: MutableList<ActivityLog> = gson.fromJson(currentJson, type) ?: mutableListOf()
            list.add(activity)
            val newJson = gson.toJson(list)
            prefs[ACTIVITIES_KEY] = newJson
        }
    }

    suspend fun deleteActivity(activity: ActivityLog) {
        context.dataStore.edit { prefs ->
            val currentJson = prefs[ACTIVITIES_KEY] ?: "[]"
            val type = object : TypeToken<MutableList<ActivityLog>>() {}.type
            val list: MutableList<ActivityLog> = gson.fromJson(currentJson, type) ?: mutableListOf()

            list.removeAll { it.timestamp == activity.timestamp }

            prefs[ACTIVITIES_KEY] = gson.toJson(list)
        }
    }
}


data class ActivityLog(
    val name: String,
    val category: String,
    val impact: Double,
    val timestamp: Long = System.currentTimeMillis()
)
package com.azizgraphics.clcltr.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppPreferences(private val context: Context) {
    companion object {
        val KEY_USE_FEET = booleanPreferencesKey("use_feet")
        val KEY_DEFAULT_PRICE = doublePreferencesKey("default_price")
        val KEY_THEME_MODE = intPreferencesKey("theme_mode")
        val KEY_CURRENCY = stringPreferencesKey("currency")
    }

    val useFeet: Flow<Boolean> = context.dataStore.data.map { it[KEY_USE_FEET] ?: true }
    val defaultPrice: Flow<Double> = context.dataStore.data.map { it[KEY_DEFAULT_PRICE] ?: 50.0 }
    val themeMode: Flow<Int> = context.dataStore.data.map { it[KEY_THEME_MODE] ?: 0 }
    val currency: Flow<String> = context.dataStore.data.map { it[KEY_CURRENCY] ?: "PKR" }

    suspend fun setUseFeet(value: Boolean) {
        context.dataStore.edit { it[KEY_USE_FEET] = value }
    }

    suspend fun setDefaultPrice(value: Double) {
        context.dataStore.edit { it[KEY_DEFAULT_PRICE] = value }
    }

    suspend fun setThemeMode(value: Int) {
        context.dataStore.edit { it[KEY_THEME_MODE] = value }
    }

    suspend fun setCurrency(value: String) {
        context.dataStore.edit { it[KEY_CURRENCY] = value }
    }
}

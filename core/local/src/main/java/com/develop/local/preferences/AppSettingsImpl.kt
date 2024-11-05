package com.develop.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.develop.common.constants.AppLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppSettingsImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
):AppSettings {

    private val darkModeKey = booleanPreferencesKey("$PREFS_TAG_KEY$IS_DARK_MODE")
    private val appLocaleKey = stringPreferencesKey("$PREFS_TAG_KEY$APP_LOCALE")


    override val isDarkMode: Flow<Boolean?> = context.dataStore.data.map { preferences ->
        preferences[darkModeKey]
    }

    override val currentLocale: Flow<AppLanguage?> = context.dataStore.data.map { preferences ->
        preferences[appLocaleKey]?.let { lng ->
            try {
                AppLanguage.valueOf(lng)
            }catch (e:Exception){
                null
            }
        }
    }

    override suspend fun changeDarkMode(isEnabled: Boolean?) {
        context.dataStore.edit { preferences ->
            if (isEnabled == null){
                preferences.remove(darkModeKey)
            }else{
                preferences[darkModeKey] = isEnabled
            }
        }
    }

    override suspend fun setLocale(lng: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[appLocaleKey] = lng.name
        }
    }

    private companion object {
        private const val PREFS_TAG_KEY = "AppPreferences"
        private const val IS_DARK_MODE = "prefsBoolean"
        private const val APP_LOCALE = "app_locale"
    }
}

interface AppSettings{

    val isDarkMode: Flow<Boolean?>
    val currentLocale: Flow<AppLanguage?>

    suspend fun changeDarkMode(isEnabled: Boolean?)
    suspend fun setLocale(lng: AppLanguage)
}
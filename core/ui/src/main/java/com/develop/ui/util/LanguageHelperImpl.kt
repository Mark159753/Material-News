package com.develop.ui.util

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.text.intl.Locale
import androidx.core.os.LocaleListCompat
import com.develop.common.LanguageHelper
import com.develop.common.constants.AppLanguage
import com.develop.local.preferences.AppSettingsImpl
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class LanguageHelperImpl @Inject constructor(
    private val appSettings: AppSettingsImpl,
    @ApplicationContext
    private val appContext: Context
):LanguageHelper {

    override suspend fun changeLanguage(locale: AppLanguage) {
        val tage = Locale(locale.name.lowercase()).toLanguageTag()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            appContext.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(tage)
        } else {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(tage)
            )
        }
        appSettings.setLocale(locale)
    }

    override suspend fun saveInitLng(context: Context){
        val savedLng = appSettings.currentLocale.firstOrNull()
        if (savedLng == null){
            val systemLng = context.getCurrentLng()
            try {
                appSettings.setLocale(AppLanguage.valueOf(systemLng.uppercase()))
            }catch (e:Exception){
                appSettings.setLocale(AppLanguage.EN)
            }
        }
    }
}


fun Context.getCurrentLng():String{
    return resources.configuration.locales.get(0).language
}
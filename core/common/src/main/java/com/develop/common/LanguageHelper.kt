package com.develop.common

import android.content.Context
import com.develop.common.constants.AppLanguage

interface LanguageHelper {

    suspend fun changeLanguage(locale: AppLanguage)

    suspend fun saveInitLng(context: Context)
}
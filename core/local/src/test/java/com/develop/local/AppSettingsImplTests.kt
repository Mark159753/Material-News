package com.develop.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.develop.common.constants.AppLanguage
import com.develop.local.preferences.AppSettingsImpl
import com.develop.local.preferences.dataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class AppSettingsImplTests {

    private val context:Context by lazy { RuntimeEnvironment.getApplication() }

    private val appSettings:AppSettingsImpl by lazy {
        AppSettingsImpl(context)
    }

    @Test
    fun `is dark them null`() = runTest {
        val isDark = appSettings.isDarkMode.firstOrNull()
        Assert.assertNull(isDark)
    }

    @Test
    fun `is current locale null`() = runTest {
        val isDark = appSettings.currentLocale.firstOrNull()
        Assert.assertNull(isDark)
    }

    @Test
    fun `set is dark true and check`() = runTest {
        appSettings.changeDarkMode(true)
        val isDark = appSettings.isDarkMode.firstOrNull()
        Assert.assertNotNull(isDark)
        Assert.assertTrue(isDark!!)
    }

    @Test
    fun `set current locale to EN and check`() = runTest {
        val locale = AppLanguage.EN
        appSettings.setLocale(locale)
        val lng = appSettings.currentLocale.firstOrNull()
        Assert.assertNotNull(lng)
        Assert.assertEquals(locale, lng)
    }

    @After
    fun clanUp(){
        runBlocking {
            context.dataStore.edit { it.clear() }
        }
    }

}
import java.util.Properties

plugins {
    alias(libs.plugins.android.lib)
}

val apikeyPropertiesFile = rootProject.file("local.properties")
val apikeyProperties = Properties().apply {
    load(apikeyPropertiesFile.inputStream())
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", (apikeyProperties["BASE_URL"] as? String) ?: System.getenv("BASE_URL"))
        buildConfigField("String", "API_KEY", (apikeyProperties["API_KEY"] as? String) ?: System.getenv("API_KEY"))
    }

    buildFeatures.buildConfig = true
}

dependencies {

    //Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.common.tests)
}
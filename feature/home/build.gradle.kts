plugins {
    alias(libs.plugins.android.lib)
    alias(libs.plugins.compose.lib)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:local"))
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))

    //Paging 3
    implementation(libs.paging.compose)

    //Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.common.tests)
}
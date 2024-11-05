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
    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))

    //Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.common.tests)
}
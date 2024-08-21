plugins {
    alias(libs.plugins.android.lib)
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

    implementation(libs.paging.runtime)

    //Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.common.tests)
}
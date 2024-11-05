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
    implementation(project(":core:local"))
    implementation(project(":core:network"))
    implementation(project(":core:data"))

    implementation(libs.paging.runtime)

    //Protobuf
    implementation(libs.datastore)
    implementation(libs.protobuf.kotlin.lite)

    //Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.common.tests)
}
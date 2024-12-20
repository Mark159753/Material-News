plugins {
    alias(libs.plugins.android.lib)
    alias(libs.plugins.compose.lib)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
//    composeCompiler{
//        reportsDestination = layout.buildDirectory.dir("compose_compiler")
//        metricsDestination = layout.buildDirectory.dir("compose_compiler")
//    }
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:local"))
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))

    //Paging 3
    implementation(libs.paging.compose)
    testImplementation(libs.paging.common.testing)

    //Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.common.tests)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
}
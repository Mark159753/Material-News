import com.google.protobuf.gradle.id

plugins {
    alias(libs.plugins.android.lib)
    alias(libs.plugins.protobuf)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {

    //Room
    ksp(libs.room.compiler)
    api(libs.bundles.room)
    testImplementation(libs.room.testing)

    implementation(project(":core:common"))

    //DataStore
    implementation(libs.datastore.preferences)
    //Protobuf
    implementation(libs.datastore)
    implementation(libs.protobuf.kotlin.lite)

    //Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.common.tests)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                id("java") {
                    option("lite")
                }
                id("kotlin") {
                    option("lite")
                }
            }
        }
    }
}
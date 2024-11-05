package com.develop.materialnews

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        val moduleName = path.split(":").drop(2).joinToString(".")
        namespace = if(moduleName.isNotEmpty()) "com.develop.$moduleName" else "com.develop.materialnews"

        compileSdk = libs.findVersion("android.compileSdk").get().requiredVersion.toInt()

        //For Robolectric
        testOptions.unitTests.isIncludeAndroidResources = true

        defaultConfig {
            minSdk = libs.findVersion("android.minSdk").get().requiredVersion.toInt()
            vectorDrawables {
                useSupportLibrary = true
            }
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            isCoreLibraryDesugaringEnabled = true
        }
    }

    configureKotlin<KotlinAndroidProjectExtension>()

    dependencies {
        add("ksp", libs.findLibrary("hilt.android.compiler").get())
        add("ksp", libs.findLibrary("androidx.hilt.compiler").get())
        add("implementation", libs.findLibrary("hilt.android").get())
        add("implementation", libs.findLibrary("hilt.navigation.compose").get())
        add("implementation", libs.findLibrary("androidx.appcompat").get())
        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())

        add("testImplementation", libs.findLibrary("mockito.core").get())
        add("testImplementation", libs.findLibrary("mockito.kotlin").get())
        add("testImplementation", libs.findLibrary("mockk").get())
        add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
        add("testImplementation", libs.findLibrary("robolectric").get())
        add("testImplementation", libs.findLibrary("androidx.test.core").get())
        add("androidTestImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
        add("androidTestImplementation", libs.findLibrary("dexmaker.mockito").get())
    }
}

internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    configureKotlin<KotlinJvmProjectExtension>()
}

/**
 * Configure base Kotlin options
 */
private inline fun <reified T : KotlinTopLevelExtension> Project.configureKotlin() = configure<T> {
    // Treat all Kotlin warnings as errors (disabled by default)
    // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
    val warningsAsErrors: String? by project
    when (this) {
        is KotlinAndroidProjectExtension -> compilerOptions
        is KotlinJvmProjectExtension -> compilerOptions
        else -> TODO("Unsupported project extension $this ${T::class}")
    }.apply {
        jvmTarget = JvmTarget.JVM_17
        allWarningsAsErrors = warningsAsErrors.toBoolean()
        freeCompilerArgs.add(
            // Enable experimental coroutines APIs, including Flow
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        )
    }
}
package com.develop.materialnews

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.findVersion("kotlin.compiler.extension").get().requiredVersion
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", libs.findLibrary("androidx.core.ktx").get())
            add("implementation", libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
            add("implementation", libs.findLibrary("androidx.activity.compose").get())
            add("implementation", libs.findLibrary("androidx.ui").get())
            add("implementation", libs.findLibrary("androidx.ui.graphics").get())
            add("implementation", libs.findLibrary("androidx.ui.tooling.preview").get())
            add("implementation", libs.findLibrary("androidx.material3").get())
            add("implementation", libs.findLibrary("lifecycle.runtime.compose").get())
            add("implementation", libs.findLibrary("androidx.material").get())
            add("implementation", platform(bom))

            //Navigation
            add("implementation", libs.findLibrary("navigation").get())
            //Motion animations
            add("implementation", libs.findLibrary("material.motion.compose.core").get())

            add("androidTestImplementation", platform(bom))
            add("androidTestImplementation", libs.findLibrary("androidx.ui.test.junit4").get())
            add("debugImplementation", libs.findLibrary("androidx.ui.tooling").get())
            add("debugImplementation", libs.findLibrary("androidx.ui.test.manifest").get())
        }
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }
        fun Provider<*>.relativeToRootProject(dir: String) = flatMap {
            rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
        }.map { it.dir(dir) }

        project.providers.gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
            .relativeToRootProject("compose-metrics")
            .let(metricsDestination::set)

        project.providers.gradleProperty("enableComposeCompilerReports").onlyIfTrue()
            .relativeToRootProject("compose-reports")
            .let(reportsDestination::set)

        stabilityConfigurationFile = rootProject.layout.projectDirectory.file("compose_compiler_config.conf")

        enableStrongSkippingMode = true
    }
}
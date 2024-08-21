import com.android.build.api.dsl.ApplicationExtension
import com.develop.materialnews.configureAndroidCompose
import com.develop.materialnews.configureKotlinAndroid
import com.develop.materialnews.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidApplicationComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.findPlugin("android.application").get().get().pluginId)
            apply(plugin = libs.findPlugin("jetbrains.kotlin.android").get().get().pluginId)
            apply(plugin = libs.findPlugin("kotlin.compose").get().get().pluginId)
            apply(plugin = libs.findPlugin("ksp").get().get().pluginId)
            apply(plugin = libs.findPlugin("hilt").get().get().pluginId)

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk =  libs.findVersion("android.targetSdk").get().requiredVersion.toInt()
                configureAndroidCompose(this)
            }
        }
    }

}
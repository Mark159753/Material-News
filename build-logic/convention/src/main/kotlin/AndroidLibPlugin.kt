import com.android.build.api.dsl.LibraryExtension
import com.develop.materialnews.configureKotlinAndroid
import com.develop.materialnews.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidLibPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.findPlugin("android.library").get().get().pluginId)
            apply(plugin = libs.findPlugin("jetbrains.kotlin.android").get().get().pluginId)
            apply(plugin = libs.findPlugin("ksp").get().get().pluginId)
            apply(plugin = libs.findPlugin("hilt").get().get().pluginId)

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }
        }
    }
}
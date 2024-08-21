import com.android.build.api.dsl.LibraryExtension
import com.develop.materialnews.configureAndroidCompose
import com.develop.materialnews.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class ComposeLibPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.findPlugin("kotlin.compose").get().get().pluginId)

            extensions.configure<LibraryExtension> {
                configureAndroidCompose(this)
            }
        }
    }
}
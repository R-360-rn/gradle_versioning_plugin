package main.kotlin.gradle.plugins.versioningclient

import org.gradle.api.Plugin
import org.gradle.api.Project
import javax.inject.Inject
import org.gradle.api.plugins.BasePlugin

class VersioningClientPlugin @Inject constructor(private val project: Project) : Plugin<Project> {

    override fun apply(project: Project) {
        val generateVersionFile = project.tasks.register("generateVersionFile", GenerateVersionFileTask::class.java)

        project.afterEvaluate {
            // Attach the task to the build phase
            project.tasks.getByName(BasePlugin.BUILD_GROUP).dependsOn(generateVersionFile)
        }
    }
}

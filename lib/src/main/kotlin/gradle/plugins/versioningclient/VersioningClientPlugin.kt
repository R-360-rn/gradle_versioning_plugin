package main.kotlin.gradle.plugins.versioningclient

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import javax.inject.Inject
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.file.FileCollection
import main.kotlin.gradle.plugins.versioningclient.GenerateVersionFileTask
import main.kotlin.gradle.plugins.versioningclient.GenerateCurrentTimestampTask
import main.kotlin.gradle.plugins.versioningclient.GenerateGitCommitHashTask
import main.kotlin.gradle.plugins.versioningclient.GenerateCurrentVersionTask

class VersioningClientPlugin @Inject constructor(private val project: Project) : Plugin<Project> {
    override fun apply(project: Project) {

        val versionFileDir = project.layout.buildDirectory.get().dir("version")
        val versionFilePartsDir =  versionFileDir.dir("version_parts")

        val generateGitCommitHashTask: TaskProvider<GenerateGitCommitHashTask> = project.tasks.register("generateGitCommitHash", GenerateGitCommitHashTask::class.java) {
            val gitComitHashFileProvider = project.provider { versionFilePartsDir.file("gitCommitHash.txt") }
            it.setGitCommitHashFile(gitComitHashFileProvider)
        }

        val generateCurrentTimestampTask: TaskProvider<GenerateCurrentTimestampTask> = project.tasks.register("generateCurrentTimestamp", GenerateCurrentTimestampTask::class.java) {
            val currentTimestampFileProvider = project.provider { versionFilePartsDir.file("timestamp.txt") }
            it.setTimestampFile(currentTimestampFileProvider)
        }

        val generateCurrentVersionTask: TaskProvider<GenerateCurrentVersionTask> = project.tasks.register("generateCurrentVersion", GenerateCurrentVersionTask::class.java) {
            val projectVersionFileProvider = project.provider { versionFilePartsDir.file("projectVersion.txt") }
            it.setProjectVersionFile(projectVersionFileProvider)
        }

        val generateVersionFile: TaskProvider<GenerateVersionFileTask> = project.tasks.register("generateVersionFile", GenerateVersionFileTask::class.java) {
            // You can configure the output file path directly within the task
            val versionFileProvider = project.provider { versionFileDir.file("version.txt") }
            it.setVersionFile(versionFileProvider)
            val specificFilesCollection: FileCollection = project.files(
                                                            generateGitCommitHashTask.get().getGitCommitHashFile(),
                                                            generateCurrentTimestampTask.get().getTimestampFile(),
                                                            generateCurrentVersionTask.get().getProjectVersionFile()
                                                        )

            it.setInputFiles(specificFilesCollection)
        }

        // Attach the task to the build phase
        project.tasks.getByName(BasePlugin.BUILD_GROUP).dependsOn(generateVersionFile)
    }
}
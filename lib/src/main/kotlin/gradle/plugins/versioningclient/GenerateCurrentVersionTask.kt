package main.kotlin.gradle.plugins.versioningclient

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import org.gradle.api.provider.Provider
import org.gradle.api.file.RegularFile


open class GenerateCurrentVersionTask : DefaultTask() {
    @Internal
    val propertyName = "currentVersion"

    @OutputFile
     private var projectVersionFile: RegularFileProperty = project.objects.fileProperty()

    @Input
    val projectVersion: String? = project.findProperty(propertyName)?.toString()

    @TaskAction
    fun generateCurrentVersion() {
        logger.quiet("Generating file {}", projectVersion)
        projectVersionFile.get().asFile.writeText("current_version: $projectVersion")
        logger.quiet("Completed generating file {}", projectVersion)
    }

    fun getProjectVersionFile(): RegularFileProperty {
        return projectVersionFile
    }

    fun setProjectVersionFile(file: Provider<RegularFile>) {
        projectVersionFile.convention(file)
    }
}

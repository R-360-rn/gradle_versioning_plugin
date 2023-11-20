package main.kotlin.gradle.plugins.versioningclient

import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.Project
import java.io.File
import java.nio.file.Files
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import org.gradle.api.tasks.util.PatternFilterable
import org.gradle.api.file.FileCollection
import javax.inject.Inject

open class GenerateVersionFileTask @Inject constructor(private val project: Project) : DefaultTask() {

    @OutputFile
    private var versionFile: RegularFileProperty = project.objects.fileProperty()

    @InputFiles
    private var inputFiles = project.objects.fileCollection()

    init {
        group = "Versioning"
        description = "Generate version.txt"
    }

    @TaskAction
    public fun generateVersionFile() {
        val versionFileString = versionFile.get().toString()
        logger.quiet("Generating Uber file {}", versionFileString)
        val values = inputFiles.files.map { it.readText() }
        val concatenatedContent = values.joinToString(separator = "\n")
        versionFile.get().asFile.writeText(concatenatedContent)
        logger.quiet("Completed generating Uber file {}", versionFileString)
    }

    public fun setVersionFile(filePathProvider: Provider<RegularFile>) {
        versionFile.convention(filePathProvider)
    }

    public fun getVersionFile(): File {
        return versionFile.get().asFile
    }

    public fun setInputFiles(fileCollection: FileCollection) {
        inputFiles.from(fileCollection)
    }

    public fun getInputFiles(): FileCollection {
        return inputFiles
    }
}
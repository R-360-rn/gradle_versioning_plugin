package main.kotlin.gradle.plugins.versioningclient

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

open class GenerateVersionFileTask() : DefaultTask() {

    init {
        group = "Versioning"
        description = "Generate version.txt"
    }

    @TaskAction
    fun generateVersionFile() {
        val fileName = "version.txt"
        val fileDir = Paths.get(project.layout.buildDirectory.toString(), "version")
                            .toString()
        createDirectoryIfNotExists(fileDir)
        val filePath = Paths.get(fileDir, fileName)
                            .toAbsolutePath()
                            .toString()
        try {
            println("Generating file $filePath")
            val formattedDateTime = getUtcDateTimeNow()
            val currentVersion = project.findProperty("currentVersion")
            val gitHash = getGitCommitHash()
            val stdOut = File(filePath)
            val toWrite = """
            current_version: $currentVersion
            git_commit_hash: $gitHash
            build_timestamp: $formattedDateTime
            """.trimIndent()
            stdOut.writeText(toWrite)
            println("Generated $filePath:\n$toWrite")
        }
        catch(e: Exception) {
            println("Error during generation of $filePath")
            e.printStackTrace()
        }

    }

    private fun getGitCommitHash(): String {
        val processBuilder = ProcessBuilder("git", "rev-parse", "HEAD")
        processBuilder.directory(project.projectDir)
        val process = processBuilder.start()
        val reader = process.inputStream.bufferedReader()
        val gitHash = reader.readLine()
        process.waitFor()
        return gitHash ?: ""
    }

    private fun getUtcDateTimeNow(): String {
        val formattedDateTime = LocalDateTime.now()
            .atZone(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
        return formattedDateTime.toString()
    }

fun createDirectoryIfNotExists(directoryPath: String) {
    val dirPath: Path = Paths.get(directoryPath)

    if (!Files.exists(dirPath)) {
        try {
            Files.createDirectories(dirPath)
            println("Directory created: $dirPath")
        } catch (e: Exception) {
            println("Error creating directory: $dirPath")
            e.printStackTrace()
        }
    } else {
        println("Directory already exists: $dirPath")
    }
}

}
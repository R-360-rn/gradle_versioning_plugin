package main.kotlin.gradle.plugins.versioningclient

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import org.gradle.api.provider.Provider
import org.gradle.api.file.RegularFile
import java.io.IOException



open class GenerateGitCommitHashTask : DefaultTask() {
    @OutputFile
    private var gitCommitHashFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun generateGitCommitHash() {
        val gitCommitHashFileString = gitCommitHashFile.get().toString()
        logger.quiet("Generating file {}", gitCommitHashFileString)
        val gitCommitHash = getGitCommitHash()
        gitCommitHashFile.get().asFile.writeText("git_commit_hash: $gitCommitHash")
        logger.quiet("Completed generating file {}", gitCommitHashFileString)

    }

    fun getGitCommitHashFile(): RegularFileProperty {
        return gitCommitHashFile
    }

    fun setGitCommitHashFile(file: Provider<RegularFile>) {
        gitCommitHashFile.convention(file)
    }

    @Input
    public fun getGitCommitHash(): String {
        val processBuilder = ProcessBuilder("git", "rev-parse", "HEAD")
        processBuilder.directory(project.projectDir)
        val process = processBuilder.start()
        val reader = process.inputStream.bufferedReader()
        val gitHash = reader.readLine()
        process.waitFor()
        return gitHash ?: ""
    }

    @Input
    public fun isGitInstalled(): Boolean {
        val err_msg = "Git is not installed. Please install Git and try again."
        try {
            logger.quiet("Validate if git command line utility is installed")
            val process = ProcessBuilder("git", "--version")
                .redirectErrorStream(true)
                .start()

            val exitCode = process.waitFor()

            // If the exit code is 0, it means the 'git' command exists
            val result = exitCode == 0
            logger.quiet("Validation done. Result is: {}", result)
            return result
        } catch (e: IOException) {
            logger.error(err_msg)
            throw GradleException(err_msg)
        } catch (e: InterruptedException) {
            logger.error(err_msg)
            throw GradleException(err_msg)
        }
    }


}

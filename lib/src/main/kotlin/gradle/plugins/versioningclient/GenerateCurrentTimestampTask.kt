package main.kotlin.gradle.plugins.versioningclient

import org.gradle.api.DefaultTask
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


open class GenerateCurrentTimestampTask : DefaultTask() {
    @OutputFile
     private var timestampFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun generateCurrentTimestamp() {
        val timestampFileString = timestampFile.get().toString()
        logger.quiet("Generating file {}", timestampFileString)
        val formattedDateTime = getUtcDateTimeNow()
        timestampFile.get().asFile.writeText("build_timestamp: $formattedDateTime")
        logger.quiet("Completed generating file {}", timestampFileString)

    }

    fun getTimestampFile(): RegularFileProperty {
        return timestampFile
    }

    fun setTimestampFile(file: Provider<RegularFile>) {
        timestampFile.convention(file)
    }

    @Input
    public fun getUtcDateTimeNow(): String {
        val formattedDateTime = LocalDateTime.now()
            .atZone(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
        return formattedDateTime.toString()
    }
}

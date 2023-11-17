val PLUGIN_ID = "custom-plugin-versioningclient"
val PLUGIN_VERSION = "1.0.0"
val PLUGIN_NAME = PLUGIN_ID

plugins {
    kotlin("jvm") version "1.8.0"
    id("java-gradle-plugin")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create(PLUGIN_ID) {
            id = PLUGIN_ID
            implementationClass = "main.kotlin.gradle.plugins.versioningclient.VersioningClientPlugin"
            version = PLUGIN_VERSION
        }
    }
}

tasks.jar {
    archiveBaseName.set(PLUGIN_NAME)
}

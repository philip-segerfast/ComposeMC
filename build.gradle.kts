val modVersion: String by project
val modGroupId: String by project

plugins {
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    alias(libs.plugins.jetbrainsCompose) apply false

    id("idea")
    alias(libs.plugins.neogradle) apply false

    // Adds the Kotlin Gradle plugin
    id("org.jetbrains.kotlin.jvm") version "2.0.0" apply false
    // OPTIONAL Kotlin Serialization plugin
    //id 'org.jetbrains.kotlin.plugin.serialization' version '2.0.0'

//    id("org.jetbrains.kotlinx.atomicfu") version "0.25.0" apply false
}

version = modVersion
group = modGroupId

allprojects {
    repositories {
        // Add KFF Maven repository
        maven("https://thedarkcolour.github.io/KotlinForForge/") {
            name = "Kotlin for Forge"
            content { includeGroup("thedarkcolour") }
        }
        maven("https://plugins.gradle.org/m2/")
        mavenCentral()
        google()
    }
}


tasks.named<Wrapper>("wrapper").configure {
    // Define wrapper values here to not have to always do so when updating gradlew.properties.
    // Switching this to Wrapper.DistributionType.ALL will download the full gradle sources that comes with
    // documentation attached on cursor hover of gradle classes and methods. However, this comes with increased
    // file size for Gradle. If you do switch this to ALL, run the Gradle wrapper task twice afterwards.
    // (Verify by checking gradle/wrapper/gradle-wrapper.properties to see if distributionUrl now points to `-all`)
    distributionType = Wrapper.DistributionType.ALL
}
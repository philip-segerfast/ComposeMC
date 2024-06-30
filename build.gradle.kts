val modVersion: String by project
val modGroupId: String by project

plugins {
    // Adds the Kotlin Gradle plugin
    alias(libs.plugins.dokka) apply false
    // OPTIONAL Kotlin Serialization plugin
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.compose.compiler.plugin) apply false
}

version = modVersion
group = modGroupId

allprojects {
    repositories {
        mavenLocal()
        // Add KFF Maven repository
        maven("https://thedarkcolour.github.io/KotlinForForge/") {
            name = "Kotlin for Forge"
        }
        maven("https://plugins.gradle.org/m2/")
        mavenCentral()
        google()
    }

    //minecraft.accessTransformers.file rootProject.file('src/main/resources/META-INF/accesstransformer.cfg')
    //minecraft.accessTransformers.entry public net.minecraft.client.Minecraft textureManager # textureManager
}

tasks.named<Wrapper>("wrapper").configure {
    // Define wrapper values here to not have to always do so when updating gradlew.properties.
    // Switching this to Wrapper.DistributionType.ALL will download the full gradle sources that comes with
    // documentation attached on cursor hover of gradle classes and methods. However, this comes with increased
    // file size for Gradle. If you do switch this to ALL, run the Gradle wrapper task twice afterwards.
    // (Verify by checking gradle/wrapper/gradle-wrapper.properties to see if distributionUrl now points to `-all`)
    distributionType = Wrapper.DistributionType.ALL
}
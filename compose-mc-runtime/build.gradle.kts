val modVersion: String by project
val modGroupId: String by project

val modId: String by project
val modName: String by project
val modLicense: String by project
val modAuthors: String by project
val neoVersion: String by project
val modDescription: String by project
val neoVersionRange: String by project
val minecraftVersion: String by project
val loaderVersionRange: String by project
val minecraftVersionRange: String by project

plugins {
    kotlin("jvm")
//    id("org.jetbrains.kotlin.plugin.compose")
//    id("org.jetbrains.dokka")
    alias(libs.plugins.dokka)
    alias(libs.plugins.compose.compiler.plugin)

    // Maven
    id("maven-publish")
    // Forge
    id("net.neoforged.gradle.userdev") version("7.0.145")
    id("idea")
}

group = "com.example.examplemod"
version = "1.0.0"

base {
    archivesName = modId
}

// Mojang ships Java 21 to end users starting in 1.20.5, so mods should target Java 21.
//java.toolchain.languageVersion = JavaLanguageVersion.of(21)

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Specify the version of Minecraft to use.
    // Depending on the plugin applied there are several options. We will assume you applied the userdev plugin as shown above.
    // The group for userdev is net.neoforged, the module name is neoforge, and the version is the same as the neoforge version.
    // You can however also use the vanilla plugin (net.neoforged.gradle.vanilla) to use a version of Minecraft without the neoforge loader.
    // And its provides the option to then use net.minecraft as the group, and one of; client, server or joined as the module name, plus the game version as version.
    // For all intends and purposes: You can treat this dependency as if it is a normal library you would use.
    implementation("net.neoforged:neoforge:$neoVersion")

    // Adds KFF as dependency and Kotlin libs
    implementation("thedarkcolour:kotlinforforge-neoforge:5.3.0")

    implementation(libs.kotlin.plugin.core)
    implementation(libs.kotlin.plugin.compose)
    implementation(libs.maven.publish.gradlePlugin)
    implementation(libs.spotless.gradlePlugin)
    implementation(libs.binary.compatibility.validator.gradlePlugin)
}

// This block of code expands all declared replace properties in the specified resource targets.
// A missing property will result in an error. Properties are expanded using ${} Groovy notation.
// When "copyIdeResources" is enabled, this will also run before the game launches in IDE environments.
// See https://docs.gradle.org/current/dsl/org.gradle.language.jvm.tasks.ProcessResources.html

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8" // Use the UTF-8 charset for Java compilation
}

tasks.withType<ProcessResources>().configureEach {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to minecraftVersionRange,
        "neo_version" to neoVersion,
        "neo_version_range" to neoVersionRange,
        "loader_version_range" to loaderVersionRange,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to modVersion,
        "mod_authors" to modAuthors,
        "mod_description" to modDescription
    )

    inputs.properties(replaceProperties)

    filesMatching(listOf("META-INF/neoforge.mods.toml")) {
        expand(replaceProperties)
    }
}

// Example configuration to allow publishing using the maven-publish plugin
publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("file://${project.projectDir}/repo")
        }
    }
}

// Default run configurations.
// These can be tweaked, removed, or duplicated as needed.

runs {
    // applies to all the run configs below
    configureEach {
        // Recommended logging data for a userdev environment
        // The markers can be added/remove as needed separated by commas.
        // "SCAN": For mods scan.
        // "REGISTRIES": For firing of registry events.
        // "REGISTRYDUMP": For getting the contents of all registries.
        systemProperty("forge.logging.markers", "REGISTRIES")

        // Recommended logging level for the console
        // You can set various levels here.
        // Please read: https://stackoverflow.com/questions/2031163/when-to-use-the-different-log-levels
        systemProperty("forge.logging.console.level", "debug")

        modSource(project.sourceSets.main.get())
    }

//    client {
//        // Comma-separated list of namespaces to load gametests from. Empty = all namespaces.
//        systemProperty 'forge.enabledGameTestNamespaces', project.mod_id
//    }

//    server {
//        systemProperty 'forge.enabledGameTestNamespaces', project.mod_id
//        programArgument '--nogui'
//    }

    // This run config launches GameTestServer and runs all registered gametests, then exits.
    // By default, the server will crash when no gametests are provided.
    // The gametest system is also enabled by default for other run configs under the /test command.
//    gameTestServer {
//        systemProperty 'forge.enabledGameTestNamespaces', project.mod_id
//    }

//    data {
//        // example of overriding the workingDirectory set in configureEach above, uncomment if you want to use it
//        // workingDirectory project.file('run-data')
//
//        // Specify the modid for data generation, where to output the resulting resource, and where to look for existing resources.
//        programArguments.addAll '--mod', project.mod_id, '--all', '--output', file('src/generated/resources/').getAbsolutePath(), '--existing', file('src/main/resources/').getAbsolutePath()
//    }
}

// Include resources generated by data generators.
sourceSets.main.configure {
    resources.srcDir("src/generated/resources")
}

dependencies {
    testImplementation(kotlin("test"))
    api(libs.compose.runtime.desktop)
    api(libs.compose.ui.desktop)
    api(libs.kotlinx.coroutines.core)
    implementation(libs.mordant)
    implementation(libs.codepoints)
}

// Sets up a dependency configuration called 'localRuntime'.
// This configuration should be used instead of 'runtimeOnly' to declare
// a dependency that will be present for runtime testing but that is
// "optional", meaning it will not be pulled by dependents of this mod.
configurations {
    runtimeClasspath.configure {
        extendsFrom(localRuntime.get())
    }
}

tasks.test {
    useJUnitPlatform()
}

// IDEA no longer automatically downloads sources/javadoc jars for dependencies, so we need to explicitly enable the behavior.
idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

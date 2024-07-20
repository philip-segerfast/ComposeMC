import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.neogradle)
    `maven-publish`
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
java.withSourcesJar()

repositories {
    maven("https://maven.neoforged.net/releases")
    // use mojang libraries without NeoGradle
    maven("https://repo.minebench.de/")
}

dependencies {
    implementation(libs.neoforge)
    implementation(libs.fancymodloader)

    api(libs.compose.runtime)
//    jarJarLib(libs.compose.runtime)
}

tasks.withType<Jar> {
    manifest.attributes("FMLModType" to "GAMELIBRARY")
}

//val minecraftVersion: String by project
//
//java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))
//
//jarJar.enable()
//
//// Only publish "-all" variant
//configurations {
//    apiElements {
//        artifacts.clear()
//    }
//    runtimeElements {
//        // Include subprojects as transitive runtime dependencies
//        setExtendsFrom(hashSetOf(configurations.getByName("api")))
//        // Publish the jarJar ONLY
//        artifacts.clear()
//        outgoing.artifact(tasks.jarJar)
//    }
//}
//
//extensions.getByType(net.minecraftforge.gradle.userdev.UserDevExtension::class).apply {
//    mappings("official", minecraftVersion)
//}
//
//fun DependencyHandler.jarJarLib(dependencyNotation: Provider<out ExternalModuleDependency>) {
//    val dep = dependencyNotation.get().copy()
//    jarJar("${dep.group}:${dep.name}:[${dep.version},)") {
//        jarJar.pin(this, dep.version!!)
//        isTransitive = false
//    }
//}
//
//tasks {
//    // The packaged jarjar will contain this manifest file with these contents.
//    jarJar.configure {
//        manifest {
//            attributes(
//                "Automatic-Module-Name" to "composelib",
//                "FMLModType" to "LIBRARY"
//            )
//        }
//    }
//
//    whenTaskAdded {
//        // Disable reobfJar
//        if (name == "reobfJar") {
//            enabled = false
//        }
//        // Fight ForgeGradle and Forge crashing when MOD_CLASSES don't exist
//        if (name == "prepareRuns") {
//            doFirst {
//                sourceSets.main.get().output.files.forEach(File::mkdirs)
//            }
//        }
//    }
//
//    withType<KotlinCompile> {
//        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
//    }
//
//    assemble {
//        dependsOn(jarJar)
//    }
//}
//
//fun DependencyHandler.minecraft(dependencyNotation: Any): Dependency? = add("minecraft", dependencyNotation)

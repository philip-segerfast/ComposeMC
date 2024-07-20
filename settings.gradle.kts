pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenCentral()
        google()
    }
    plugins {
        id ("org.gradle.toolchains.foojay-resolver-convention") version ("0.5.0")
    }
}

plugins {
    id ("org.gradle.toolchains.foojay-resolver-convention") version ("0.5.0")
}

include("minecraft-compose")
include("compose-lib")
//include("demo")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
    kotlin("jvm")
}

group = "com.example.examplemod"
version = "1.0.0"

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
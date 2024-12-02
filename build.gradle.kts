plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    id("io.papermc.paperweight.userdev") version "1.7.5"
}

group = "me.beshenii"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.google.code.gson:gson:2.11.0")
    paperweight.paperDevBundle("1.21.1-R0.1-SNAPSHOT")
}


java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.jar {

    doFirst {
        from(configurations.runtimeClasspath.get().filter {
            it.path.contains("beshenii")
        }.map { if (it.isDirectory) it else zipTree(it) })
    }


    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.assemble {
    dependsOn("reobfJar")
}

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.gremlin-gradle") version "0.0.1-SNAPSHOT"
    id("xyz.jpenilla.run-paper") version "2.2.0"
}

repositories {
    maven("https://repo.jpenilla.xyz/snapshots/")
}

fun DependencyHandler.runtimeDownloadApi(dependencyNotation: Any) {
    api(dependencyNotation)
    runtimeDownload(dependencyNotation)
}

dependencies {
    runtimeDownloadApi(projects.ticketsApi)
    runtimeDownloadApi(projects.ticketsCore)

    compileOnly(libs.paper)

    runtimeDownloadApi(libs.cloud.paper)
}

configurations.runtimeDownload {
    exclude("io.papermc.paper")
    exclude("net.kyori", "adventure-api")
    exclude("net.kyori", "adventure-text-minimessage")
    exclude("net.kyori", "adventure-text-serializer-plain")
    exclude("org.slf4j", "slf4j-api")
    exclude("org.ow2.asm")
}

tasks {
    runServer {
        minecraftVersion("1.20.2")
        downloadPlugins {
            url("https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar")
        }
    }

    withType<ShadowJar> {
        dependencies {
            include(project(":tickets-api"))
            include(project(":tickets-core"))
            include(dependency(libs.gremlin.get().toString()))
        }

        relocate("xyz.jpenilla.gremlin", "broccolai.tickets.lib.xyz.jpenilla.gremlin")

        archiveFileName.set(project.name + ".jar")
        destinationDirectory.set(project.rootProject.layout.buildDirectory.dir("libs"))
    }

    build {
        dependsOn(shadowJar)
    }

    writeDependencies {
        repos.set(listOf(
                "https://repo.papermc.io/repository/maven-public/",
                "https://repo.broccol.ai/releases",
                "https://oss.sonatype.org/content/repositories/snapshots/",
        ))
    }
}


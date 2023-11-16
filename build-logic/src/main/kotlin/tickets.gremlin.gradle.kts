plugins {
    id("java-library")
    id("xyz.jpenilla.gremlin-gradle")
    id("com.github.johnrengelman.shadow")
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
    shadowJar {
        dependencies {
            include(project(":tickets-api"))
            include(project(":tickets-common"))
            include(project(":tickets-minecraft-common"))
            include(dependency("xyz.jpenilla:gremlin-runtime:0.0.3"))
        }

        relocate("xyz.jpenilla.gremlin", "love.broccolai.tickets.lib.xyz.jpenilla.gremlin")

        archiveFileName.set(project.name + ".jar")
    }

    build {
        dependsOn(shadowJar)
    }
}

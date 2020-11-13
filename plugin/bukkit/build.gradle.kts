import org.apache.tools.ant.filters.ReplaceTokens

dependencies {
    api(project(":tickets-core"))

    // Spigot
    compileOnly("org.spigotmc:spigot-api:1.16.1-R0.1-SNAPSHOT")

    // Kyori/adventure platform
    api("net.kyori:adventure-platform-bukkit:4.0.0-SNAPSHOT")

    // Cloud
    api("cloud.commandframework:cloud-paper:1.2.0-SNAPSHOT")

    // Paper lib for async tp
    api("io.papermc:paperlib:1.0.5")
}

tasks {
    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }

    shadowJar {
        archiveClassifier.set(null as String?)
        archiveFileName.set(project.name + ".jar")
        destinationDirectory.set(rootProject.tasks.shadowJar.get().destinationDirectory.get())
    }
}

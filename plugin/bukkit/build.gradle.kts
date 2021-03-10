dependencies {
    api(project(":tickets-core"))

    // Spigot
    compileOnly("org.spigotmc", "spigot-api", Versions.SPIGOT)

    // Kyori/adventure platform
    api("net.kyori", "adventure-platform-bukkit", Versions.KYORI_PLATFORM)

    // Cloud
    api("cloud.commandframework", "cloud-paper", Versions.CLOUD)

    // Paper lib for async tp
    api("io.papermc", "paperlib", Versions.PAPER_LIB)
}

tasks {

    build {
        dependsOn(named("shadowJar"))
    }

    processResources {
        expand("version" to rootProject.version)
    }

    shadowJar {
        fun relocates(vararg dependencies: String) {
            dependencies.forEach {
                val split = it.split('.')
                val name = split.last()
                relocate(it, "${rootProject.group}.dependencies.$name")
            }
        }

        dependencies {
            exclude(dependency("com.google.guava:"))
            exclude(dependency("com.google.errorprone:"))
            exclude(dependency("org.checkerframework:"))
            exclude(dependency("org.jetbrains:"))
            exclude(dependency("org.intellij:"))
        }

        relocates(
                "com.intellectualsites.http",
                "com.github.benmanes.caffeine",
                "org.antlr",
                "org.objectweb",
                "org.slf4j",
                "org.jdbi",
                "io.leangen.geantyref",
                "io.papermc.lib",
                "cloud.commandframework",
                "net.kyori.adventure",
                "net.kyori.event",
                "net.kyori.examination",
                "broccolai.corn"
        )

        archiveClassifier.set(null as String?)
        archiveFileName.set(project.name + ".jar")
        destinationDirectory.set(rootProject.tasks.shadowJar.get().destinationDirectory.get())
    }
}

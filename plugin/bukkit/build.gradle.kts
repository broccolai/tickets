dependencies {
    api(projects.ticketsApi)
    api(projects.ticketsCore)

    compileOnly(libs.spigot)
    api(libs.paper.lib)
    api(libs.adventure.bukkit)
    api(libs.cloud.paper)
}

tasks {

    build {
        dependsOn(named("shadowJar"))
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
                "net.kyori",
                "broccolai.corn",
                "org.yaml",
                "org.aopalliance",
                "org.spongepowered",
                "org.h2",
                "org.flywaydb",
                "com.google.inject",
                "com.google.j2objc",
                "com.zaxxer"
        )

        archiveClassifier.set(null as String?)
        archiveFileName.set(project.name + ".jar")
        destinationDirectory.set(rootProject.tasks.shadowJar.get().destinationDirectory.get())
    }
}

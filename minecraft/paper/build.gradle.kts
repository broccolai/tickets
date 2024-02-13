plugins {
    id("tickets.base")
    id("tickets.gremlin")
    id("xyz.jpenilla.run-paper") version "2.2.3"
}

fun DependencyHandler.runtimeDownloadApi(dependencyNotation: Any) {
    api(dependencyNotation)
    runtimeDownload(dependencyNotation)
}

dependencies {
    runtimeDownloadApi(projects.ticketsMinecraftCommon)

    compileOnly(libs.paper.api)
    runtimeDownloadApi(libs.cloud.paper)
}

tasks {
    runServer {
        minecraftVersion("1.20.2")
        dependsOn(jar)
    }

    writeDependencies {
        repos.set(
            listOf(
                "https://repo.broccol.ai/snapshots/",
                "https://repo.papermc.io/repository/maven-public/",
                "https://repo.maven.apache.org/maven2/"
            )
        )
    }
}

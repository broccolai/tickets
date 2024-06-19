plugins {
    id("tickets.base")
    id("tickets.gremlin")
    id("xyz.jpenilla.run-paper") version "2.2.4"
    id("xyz.jpenilla.resource-factory") version "1.1.1"
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.1.1"
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
        minecraftVersion("1.20.4")
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

paperPluginYaml {
    name = "tickets"
    main = "love.broccolai.tickets.minecraft.paper.PaperTicketsPlugin"
    loader = "love.broccolai.tickets.lib.xyz.jpenilla.gremlin.runtime.platformsupport.DefaultsPaperPluginLoader"
    apiVersion = "1.20"
    authors = listOf("broccolai")
    version = rootProject.version.toString()
}

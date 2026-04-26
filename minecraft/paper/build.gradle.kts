plugins {
    id("tickets.base")
    id("tickets.gremlin")
    alias(libs.plugins.run.paper)
    alias(libs.plugins.resource.factory)
    alias(libs.plugins.resource.factory.paper.convention)
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
        minecraftVersion(libs.versions.minecraft.run.get())
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
    apiVersion = libs.versions.paper.plugin.api.get()
    authors = listOf("broccolai")
    version = rootProject.version.toString()
}

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin
import net.kyori.indra.repository.sonatypeSnapshots

plugins {
    id("net.kyori.indra")
    id("net.kyori.indra.publishing")
    id("net.kyori.indra.checkstyle")
    id("com.github.johnrengelman.shadow")
    id("com.github.ben-manes.versions")
}

group = "broccolai.tickets"
version = "5.2.0-SNAPSHOT"

subprojects {
    apply {
        plugin<ShadowPlugin>()
        plugin<IndraPlugin>()
        plugin<IndraCheckstylePlugin>()
        plugin<IndraPublishingPlugin>()
    }

    repositories {
        mavenCentral()
        sonatypeSnapshots()

        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://mvn.intellectualsites.com/content/repositories/snapshots")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.broccol.ai")
        maven("https://repo.broccol.ai/snapshots")
    }

    tasks {

        indra {
            gpl3OnlyLicense()
            publishReleasesTo("broccolai", "https://repo.broccol.ai/releases")

            javaVersions {
                target(11)
            }

            github("broccolai", "tickets") {
                ci(true)
            }
        }

        processResources {
            expand("version" to rootProject.version)
        }

    }
}

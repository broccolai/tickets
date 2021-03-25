import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin
import net.kyori.indra.sonatypeSnapshots

plugins {
    id("net.kyori.indra") version "1.3.1"
    id("net.kyori.indra.publishing") version "1.3.1"
    id("net.kyori.indra.checkstyle") version "1.3.1"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("com.github.ben-manes.versions") version "0.38.0"
}

group = "broccolai.tickets"
version = "5.1.0-SNAPSHOT"

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
        maven("https://repo.spongepowered.org/repository/maven-public/")
        maven("https://repo.broccol.ai")
        maven("https://repo.broccol.ai/snapshots")
    }

    tasks {

        indra {
            gpl3OnlyLicense()
            publishReleasesTo("broccolai", "https://repo.broccol.ai/releases")

            javaVersions {
                target.set(11)
            }

            github("broccolai", "tickets") {
                ci = true
            }
        }

        processResources {
            expand("version" to rootProject.version)
        }

    }
}

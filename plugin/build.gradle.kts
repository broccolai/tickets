import ca.stellardrift.build.common.paper
import ca.stellardrift.build.common.spigot
import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin
import net.kyori.indra.repository.sonatypeSnapshots
import net.ltgt.gradle.errorprone.ErrorPronePlugin

plugins {
    id("net.kyori.indra")
    id("net.kyori.indra.publishing")
    id("net.kyori.indra.checkstyle")
    id("ca.stellardrift.opinionated")
    id("com.github.johnrengelman.shadow")
    id("com.github.ben-manes.versions")
    id("net.ltgt.errorprone")
}

group = "broccolai.tickets"
version = "6.0.0-SNAPSHOT"

subprojects {
    apply<ShadowPlugin>()
    apply<IndraPlugin>()
    apply<IndraCheckstylePlugin>()
    apply<IndraPublishingPlugin>()
    apply<ErrorPronePlugin>()

    repositories {
        mavenCentral()
        sonatypeSnapshots()
        spigot()
        paper()

        maven("https://mvn.intellectualsites.com/content/repositories/snapshots")
        maven("https://nexus.velocitypowered.com/repository/maven-public/")
        maven("https://repo.broccol.ai")
        maven("https://repo.broccol.ai/snapshots")
        maven("https://nexus.mardroemmar.dev/repository/maven-snapshots")
        mavenLocal()
    }

    dependencies {
        errorprone(rootProject.libs.errorprone)
    }

    tasks {

        indra {
            gpl3OnlyLicense()
            publishReleasesTo("broccolai", "https://repo.broccol.ai/releases")

            javaVersions {
                target(16)
            }

            github("broccolai", "tickets") {
                ci(true)
                issues(true)
            }
        }

        processResources {
            expand("version" to rootProject.version)
        }

    }
}

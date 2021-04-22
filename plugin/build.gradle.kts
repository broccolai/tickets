import ca.stellardrift.build.common.paper
import ca.stellardrift.build.common.sponge
import ca.stellardrift.build.common.spigot
import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin
import net.kyori.indra.repository.sonatypeSnapshots

plugins {
    id("net.kyori.indra")
    id("net.kyori.indra.publishing")
    id("net.kyori.indra.checkstyle")
    id("ca.stellardrift.opinionated")
    id("com.github.johnrengelman.shadow")
    id("com.github.ben-manes.versions")
}

group = "broccolai.tickets"
version = "5.2.0-SNAPSHOT"

subprojects {
    apply<ShadowPlugin>()
    apply<IndraPlugin>()
    apply<IndraCheckstylePlugin>()
    apply<IndraPublishingPlugin>()

    repositories {
        mavenCentral()
        sonatypeSnapshots()
        spigot()
        sponge()
        paper()

        maven("https://mvn.intellectualsites.com/content/repositories/snapshots")
        maven("https://repo.broccol.ai")
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
                issues(true)
            }
        }

        processResources {
            expand("version" to rootProject.version)
        }

    }
}

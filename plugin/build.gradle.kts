import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin

plugins {
    id("net.kyori.indra") version "3.1.3"
    id("net.kyori.indra.publishing") version "3.1.3"
    id("net.kyori.indra.checkstyle") version "3.1.3"
    id("com.github.ben-manes.versions") version "0.38.0"
}

group = "broccolai.tickets"
version = "5.3.0"

subprojects {
    apply {
        plugin<IndraPlugin>()
        plugin<IndraCheckstylePlugin>()
        plugin<IndraPublishingPlugin>()
    }

    indra {
        javaVersions {
            target(17)
        }

        gpl3OnlyLicense()
        publishReleasesTo("broccolai", "https://repo.broccol.ai/releases")
    }

    repositories {
        mavenCentral()
        sonatype.ossSnapshots()
        sonatype.s01Snapshots()

        maven("https://s01.oss.sonatype.org/content/repositories/releases/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.broccol.ai/releases")
        maven("https://repo.broccol.ai/snapshots")
    }

    tasks {
        processResources {
            expand("version" to rootProject.version)
        }
    }
}

tasks {
    withType<Jar> {
        onlyIf { false }
    }
}

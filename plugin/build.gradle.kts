import net.kyori.indra.IndraPublishingPlugin
import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import net.kyori.indra.sonatypeSnapshots

plugins {
    id("net.kyori.indra") version "1.3.1"
    id("net.kyori.indra.publishing") version "1.3.1"
    id("net.kyori.indra.checkstyle") version "1.3.1"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "broccolai.tickets"
version = rootProject.version

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
    }

    dependencies {
        // Checker-qual annotations
        compileOnlyApi("org.checkerframework:checker-qual:3.5.0")
    }

    tasks {
        build {
            dependsOn(named("shadowJar"))
        }

        indra {
            gpl3OnlyLicense()
            publishReleasesTo("broccolai", "https://repo.broccol.ai/releases")

            javaVersions {
                target.set(8)
            }

            github("broccolai", "tickets") {
                ci = true
            }
        }

        shadowJar {
            dependencies {
                exclude(dependency("com.google.guava:guava:21.0"))
            }

            fun reloc(vararg deps: String) {
                for (i in deps.indices step 2) {
                    relocate(deps[i], project.group.toString() + ".lib." + deps[i + 1])
                }
            }

            reloc(
                    "cloud.commandframework", "cloud",
                    "com.intellectualsites.http", "http",
                    "io.leangen.geantyref", "geantyref",
                    "io.papermc.lib", "paperlib",
                    "broccolai.corn", "corn"
            )


            archiveFileName.set(project.name + ".jar")
            mergeServiceFiles()
            minimize()
        }
    }
}

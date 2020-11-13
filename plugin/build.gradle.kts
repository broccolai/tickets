import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin

plugins {
    id("java")
    id("java-library")
    id("checkstyle")
    id("idea")
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

version = "4.0.0"

allprojects {
    group = "broccolai.tickets"
    version = rootProject.version

    repositories {
        mavenLocal()
        mavenCentral()

        maven {
            name = "Spigot repository"
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }

        maven {
            name = "Sonatype public"
            url = uri("https://oss.sonatype.org/content/repositories/public/")
        }

        maven {
            name = "Sonatype Snapshots"
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }

        maven {
            name = "Intellectual Sites"
            url = uri("https://mvn.intellectualsites.com/content/repositories/snapshots")
        }

        maven {
            name = "papermc"
            url = uri("https://papermc.io/repo/repository/maven-public/")
        }

        maven {
            name = "GitHub Packages"
            url = uri("https://maven.pkg.github.com/broccolai/corn")
            credentials {
                username = System.getenv("GH_USERNAME") ?: System.getenv("GITHUB_ACTOR")
                password = System.getenv("GH_TOKEN") ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }

}

subprojects {
    apply {
        plugin<JavaPlugin>()
        plugin<JavaLibraryPlugin>()
        plugin<ShadowPlugin>()
        plugin<CheckstylePlugin>()
        plugin<IdeaPlugin>()
    }

    dependencies {
        // Checkstyle
        checkstyle("ca.stellardrift:stylecheck:0.1-SNAPSHOT")

        // Checker-qual annotations
        compileOnlyApi("org.checkerframework:checker-qual:3.5.0")
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = sourceCompatibility
    }

    tasks {
        compileJava {
            options.apply {
                isFork = true
                compilerArgs.add("-Xlint:all")
                compilerArgs.add("-parameters")
            }
        }

        build {
            dependsOn(named("shadowJar"))
        }

        checkstyle {
            val configRoot = File(rootProject.projectDir, ".checkstyle")
            toolVersion = "8.34"
            configDirectory.set(configRoot)
            configProperties["basedir"] = configRoot.absolutePath
        }
    }
}

tasks {
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

if (file("$rootDir/local.gradle").exists()) {
    apply(from = "$rootDir/local.gradle")
}

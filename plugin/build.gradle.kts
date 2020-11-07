import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("checkstyle")
}

version = "3.2.0"
group = "broccolai.tickets"

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://mvnrepository.com/artifact/org.jetbrains/annotations")
    maven("https://mvn.intellectualsites.com/content/repositories/snapshots")
    maven {
        name = "papermc"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/broccolai/corn")
        credentials {
            username = System.getenv("GH_USERNAME") ?: System.getenv("GITHUB_ACTOR")
            password = System.getenv("GH_TOKEN") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    checkstyle("ca.stellardrift:stylecheck:0.1-SNAPSHOT")
    compileOnly("org.checkerframework:checker-qual:3.5.0")

    api("org.jdbi:jdbi3-core:3.16.0")
    api("com.github.ben-manes.caffeine:caffeine:2.8.6")
    api("org.slf4j:slf4j-simple:1.7.13")
    api("broccolai:corn-core:1.1.1")
    api("broccolai:corn-spigot:1.1.1")
    api("com.intellectualsites.http:HTTP4J:1.3-SNAPSHOT")
    api("cloud.commandframework:cloud-paper:1.2.0-SNAPSHOT")
    api("io.papermc:paperlib:1.0.5")
    api("net.kyori:event-api:4.0.0-SNAPSHOT")
    api("net.kyori:event-method-asm:4.0.0-SNAPSHOT")

    compileOnly("org.spigotmc:spigot-api:1.16.1-R0.1-SNAPSHOT")
}

configure<JavaPluginConvention> {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    shadowJar {
        val base = project.group.toString() + ".lib."

        dependencies {
            exclude(dependency("com.google.guava:guava:21.0"))
        }

        relocate("cloud.commandframework", base + "cloud")
        relocate("com.intellectualsites.http", base + "http")
        relocate("io.leangen.geantyref", base + "geantyref")

        relocate("io.papermc.lib", base + "paperlib")
        relocate("co.aikar.idb", base + "idb")

        relocate("broccolai.corn.core", base + "corn.core")
        relocate("broccolai.corn.spigot", base + "corn.spigot")

        relocate("com.zaxxer.hikari", base + "hikari")

        archiveFileName.set(project.name + ".jar")
        mergeServiceFiles()
        minimize()
    }

    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }

    build {
        dependsOn(shadowJar)
    }

    checkstyle {
        val configRoot = File(rootProject.projectDir, ".checkstyle")
        toolVersion = "8.34"
        configDirectory.set(configRoot)
        configProperties["basedir"] = configRoot.absolutePath
    }
}

if (file("$rootDir/local.gradle").exists()) {
    apply(from = "$rootDir/local.gradle")
}

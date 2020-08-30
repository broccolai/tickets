import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

version = "3.1.1"
group = "broccolai.tickets"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://mvnrepository.com/artifact/org.jetbrains/annotations")
    maven("https://mvn.intellectualsites.com/content/repositories/snapshots")
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
    implementation("org.jetbrains:annotations:19.0.0")

    api("co.aikar:taskchain-bukkit:3.7.2")
    api("co.aikar:acf-paper:0.5.0-SNAPSHOT")
    api("co.aikar:idb-core:1.0.0-SNAPSHOT")
    api("org.slf4j:slf4j-simple:1.7.13")
    api("com.zaxxer:HikariCP:2.7.9")
    api("broccolai:corn-core:1.0.0")
    api("broccolai:corn-spigot:1.0.0")
    api("com.intellectualsites.http:HTTP4J:1.3-SNAPSHOT")

    compileOnly("org.spigotmc:spigot-api:1.16.1-R0.1-SNAPSHOT")
}

configure<JavaPluginConvention> {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    shadowJar {
        val base = project.group.toString() + ".lib."

        relocate("co.aikar.taskchain", base + "taskchain")
        relocate("co.aikar.commands", base + "commands")
        relocate("co.aikar.locales", base + "locales")
        relocate("co.aikar.idb", base + "idb")

        relocate("broccolai.corn.core", base + "corn.core")

        relocate("com.zaxxer.hikari", base + "hikari")
        relocate("net.jodah.expiringmap", base + "expiringmap")

        archiveFileName.set(project.name + ".jar")
        mergeServiceFiles()
        minimize()
    }

    compileJava {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
        options.isFork = true
        options.forkOptions.executable = "javac"
    }

    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }

    build {
        dependsOn(shadowJar)
    }
}

if (file("$rootDir/local.gradle").exists()) {
    apply(from = "$rootDir/local.gradle")
}
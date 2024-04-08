plugins {
    id("tickets.base")

    alias(libs.plugins.spring.plugin.boot)
    alias(libs.plugins.graal.native.buildtools)
}

plugins.apply("io.spring.dependency-management")

extra["flyway.version"] = "10.11.0"

graalvmNative {
    binaries.all {
        resources.autodetect()
    }
    binaries {
        getByName("main") {
            sharedLibrary = false
            mainClass = "love.broccolai.tickets.spring.TicketsApplication"
            useFatJar = true
            javaLauncher =
                javaToolchains.launcherFor {
                    languageVersion = JavaLanguageVersion.of(21)
                    vendor = JvmVendorSpec.matching("GraalVM Community")
                }
        }
    }
    testSupport = false
    toolchainDetection = false
}

dependencies {
    implementation(projects.ticketsCommon)
    api("org.springframework.boot:spring-boot-starter-web")
    api(libs.spring.boot.autoconfigure)
    api(libs.spring.shell)
    api(platform(libs.spring.shell.dependencies))

    api(libs.gson)
}

tasks {
    compileAotJava {
        // I couldn't figure out the warnings in generated code
        options.compilerArgs.clear()
    }

    bootRun {
        workingDir = projectDir.resolve("run")
    }
}

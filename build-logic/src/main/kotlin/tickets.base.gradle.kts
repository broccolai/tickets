plugins {
    id("java-library")
    id("net.kyori.indra")
    id("net.kyori.indra.checkstyle")
}

indra {
    javaVersions {
        minimumToolchain(21)
        target(21)
    }

    gpl3OnlyLicense()
}

tasks.compileJava {
    options.compilerArgs.add("-Xlint:-processing")
    options.compilerArgs.add("-parameters")
}

dependencies {
    compileOnlyApi(libs.jspecify)
}


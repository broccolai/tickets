dependencies {
    api(project(":tickets-api"))

    // Minecraft expectations
    compileOnlyApi("com.google.code.gson", "gson", Versions.GSON)

    // Kyroi/Adventure
    api("net.kyori", "adventure-platform-api", Versions.KYORI_PLATFORM)
    api("net.kyori", "adventure-text-minimessage", Versions.MINI)

    // Jdbi
    api("org.jdbi", "jdbi3-core", Versions.JDBI)
    api("com.github.ben-manes.caffeine", "caffeine", Versions.CAFFEINE)

    // Corn
    api("broccolai.corn", "corn-core", Versions.CORN)

    // Command library
    api("cloud.commandframework", "cloud-core", Versions.CLOUD)
    api("cloud.commandframework", "cloud-minecraft-extras", Versions.CLOUD)

    // Http library
    api("com.intellectualsites.http", "HTTP4J", Versions.HTTP4J)

    // Logging
    api("org.slf4j", "slf4j-simple", Versions.SLF4J)

    // Flyway
    api("com.zaxxer", "HikariCP", Versions.HIKARI)
    api("org.flywaydb", "flyway-core", Versions.FLYWAY)
    api("com.h2database", "h2", Versions.H2)

    // Guice
    api("com.google.inject", "guice", Versions.GUICE)

    // Configurate
    api("org.spongepowered", "configurate-yaml", Versions.CONFIGURATE)

    api("com.google.inject.extensions", "guice-assistedinject", Versions.GUICE) {
        isTransitive = false
    }
}

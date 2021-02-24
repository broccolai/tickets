dependencies {
    api(project(":tickets-api"))

    // Minecraft expectations
    compileOnlyApi("com.google.guava:guava:21.0")
    compileOnlyApi("com.google.code.gson:gson:2.8.0")

    // Kyori/event
    api("net.kyori:event-api:4.0.0-SNAPSHOT")
    api("net.kyori:event-method-asm:4.0.0-SNAPSHOT")

    // Kyroi/Adventure
    api("net.kyori:adventure-platform-api:4.0.0-SNAPSHOT")
    api("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")

    // Jdbi
    api("org.jdbi:jdbi3-core:3.16.0")
    api("com.github.ben-manes.caffeine:caffeine:2.8.6")

    // Corn
    api("broccolai.corn:corn-core:1.0.0")

    // Command library
    api("cloud.commandframework:cloud-core:1.4.0")
    api("cloud.commandframework:cloud-minecraft-extras:1.4.0")

    // Http library
    api("com.intellectualsites.http:HTTP4J:1.3-SNAPSHOT")

    // Logging
    api("org.slf4j:slf4j-simple:1.7.13")

    // Flyway
    implementation("com.zaxxer:HikariCP:3.4.5")
    implementation("org.flywaydb:flyway-core:7.5.2")

    // Guice
    implementation("com.google.inject:guice:5.0.0-BETA-1")
}

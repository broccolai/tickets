dependencies {
    // Minecraft expectations
    compileOnlyApi("com.google.guava:guava:21.0")
    compileOnlyApi("com.google.code.gson:gson:2.8.0")

    // Kyori/event
    api("net.kyori:event-api:4.0.0-SNAPSHOT")
    api("net.kyori:event-method-asm:4.0.0-SNAPSHOT")

    // Kyroi/Adventure
    api("net.kyori:adventure-api:4.2.0-SNAPSHOT")
    api("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")

    // Jdbi
    api("org.jdbi:jdbi3-core:3.16.0")
    api("com.github.ben-manes.caffeine:caffeine:2.8.6")

    // Corn
    api("broccolai:corn-spigot:1.1.1")

    // Command library
    api("cloud.commandframework:cloud-core:1.2.0")

    // Http library
    api("com.intellectualsites.http:HTTP4J:1.3-SNAPSHOT")

    // Logging
    api("org.slf4j:slf4j-simple:1.7.13")
}

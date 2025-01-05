plugins {
    id("tickets.base")
}

dependencies {
    api(projects.ticketsCommon)

    api(libs.cloud.core)
    api(libs.cloud.brigadier)
    api(libs.adventure.api)
    api(libs.adventure.minimessage)
    api(libs.moonshine.standard)

    compileOnly("com.mojang:brigadier:1.0.17")
}

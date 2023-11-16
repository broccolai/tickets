plugins {
    id("tickets.base")
}

dependencies {
    api(projects.ticketsCommon)

    api(libs.cloud.core)
    api(libs.adventure.api)
}

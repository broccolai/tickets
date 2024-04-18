plugins {
    id("tickets.base")
    id("tickets.testing")
}

dependencies {
    api(projects.ticketsApi)

    api(libs.bundles.guice)
    api(libs.bundles.jdbi)
    api(libs.bundles.database)

    api(libs.corn.trove)
    api(libs.slf4j.api)
}

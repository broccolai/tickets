dependencies {
    api(projects.ticketsApi)

    api(libs.jdbi.core)
    api(libs.jdbi.gson)
    api(libs.corn.trove)
    api(libs.slf4j)
    api(libs.hikari)
    api(libs.flyway)
    api(libs.h2)

    api(libs.bundles.guice)

    testImplementation(libs.jdbi.testing)
}

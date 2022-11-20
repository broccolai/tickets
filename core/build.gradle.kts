dependencies {
    api(projects.ticketsApi)

    api(libs.jdbi.core)
    api(libs.caffeine)
    api(libs.corn.misc)
    api(libs.cloud.extras)
    api(libs.http4j)
    api(libs.slf4j)
    api(libs.hikari)
    api(libs.flyway)
    api(libs.h2)
    api(libs.configurate)

    api(libs.bundles.guice)

    testImplementation(libs.jdbi.testing)
}

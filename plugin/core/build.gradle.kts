dependencies {
    api(project(":tickets-api"))

    api(libs.adventure.platform)
    api(libs.jdbi)
    api(libs.caffeine)
    api(libs.corn)
    api(libs.cloud.extras)
    api(libs.http4j)
    api(libs.slf4j)
    api(libs.hikari)
    api(libs.flyway)
    api(libs.h2)
    api(libs.configurate)

    api(libs.bundles.guice)
}

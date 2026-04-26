plugins {
    id("tickets.base")
}

dependencies {
    api(libs.configurate)
    compileOnly(libs.bundles.database)
    compileOnly(libs.jackson.annotations)
}

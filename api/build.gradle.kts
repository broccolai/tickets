plugins {
    id("tickets.base")
}

dependencies {
    api(libs.configurate)
    compileOnly(libs.bundles.database)
    compileOnly("com.fasterxml.jackson.core", "jackson-annotations", "2.16.1")
}

rootProject.name = "tickets"

include("api", "core", "bukkit")

project(":api").name = "tickets-api"
project(":core").name = "tickets-core"
project(":bukkit").name = "tickets-bukkit"

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "tickets"

include("api", "core", "bukkit", "sponge7")

project(":api").name = "tickets-api"
project(":core").name = "tickets-core"
project(":bukkit").name = "tickets-bukkit"
project(":sponge7").name = "tickets-sponge7"

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

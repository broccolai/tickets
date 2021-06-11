plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "5.0.0"
}

rootProject.name = "tickets"

include("api", "core", "bukkit", "velocity")

project(":api").name = "tickets-api"
project(":core").name = "tickets-core"
project(":bukkit").name = "tickets-bukkit"
project(":velocity").name = "tickets-velocity"

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

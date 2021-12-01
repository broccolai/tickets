plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "5.0.0"
}

rootProject.name = "tickets"

include("api", "core")

project(":api").name = "tickets-api"
project(":core").name = "tickets-core"

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

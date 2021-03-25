pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.stellardrift.ca/repository/snapshots/")
    }
}

plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "5.0.0-SNAPSHOT"
}

rootProject.name = "tickets"

include("api", "core", "bukkit", "sponge")

project(":api").name = "tickets-api"
project(":core").name = "tickets-core"
project(":bukkit").name = "tickets-bukkit"
project(":sponge").name = "tickets-sponge"

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

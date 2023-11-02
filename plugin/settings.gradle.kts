pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.jpenilla.xyz/snapshots/")
    }
}

plugins {
    id("ca.stellardrift.polyglot-version-catalogs") version "6.0.0"
}

rootProject.name = "tickets"

include("api", "core", "paper")

project(":api").name = "tickets-api"
project(":core").name = "tickets-core"
project(":paper").name = "tickets-paper"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

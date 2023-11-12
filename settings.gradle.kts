rootProject.name = "tickets"

include("api", "core")

project(":api").name = "tickets-api"
project(":core").name = "tickets-core"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

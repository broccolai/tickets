rootProject.name = "tickets"

include("api", "common")

project(":api").name = "tickets-api"
project(":common").name = "tickets-common"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositories {
        mavenCentral()

        maven("https://repo.broccol.ai/snapshots") {
            mavenContent {
                snapshotsOnly()
                includeGroup("cloud.commandframework")
                includeGroup("love.broccolai.corn")
            }
        }

        maven("https://oss.sonatype.org/content/repositories/snapshots/") {
            mavenContent { snapshotsOnly() }
        }

        maven("https://repo.papermc.io/repository/maven-public/")
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

pluginManagement {
    includeBuild("build-logic")
}

rootProject.name = "tickets-parent"

setupSubproject(name = "api")
setupSubproject(name = "common")

setupSubproject(folder = "minecraft", name = "common")

fun setupSubproject(folder: String? = null, name: String) {
    val formattedName = listOfNotNull("tickets", folder, name)
        .joinToString("-")

    include(name)

    val project = project(":$name")
    project.name = formattedName

    folder?.let {
        project.projectDir = file("$folder/$name")
    }
}

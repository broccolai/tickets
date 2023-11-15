enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

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

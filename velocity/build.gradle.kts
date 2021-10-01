dependencies {
    api(projects.ticketsApi)
    api(projects.ticketsCore)

    compileOnly(libs.velocity)
    api(libs.cloud.velocity)
}

tasks {

    build {
        dependsOn(named("shadowJar"))
    }

    shadowJar {
        archiveClassifier.set(null as String?)
        archiveFileName.set(project.name + ".jar")
        destinationDirectory.set(rootProject.tasks.shadowJar.get().destinationDirectory.get())
    }
}

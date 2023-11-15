import com.adarshr.gradle.testlogger.TestLoggerPlugin
import com.adarshr.gradle.testlogger.theme.ThemeType
import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.IndraPublishingPlugin
import net.ltgt.gradle.errorprone.ErrorPronePlugin

plugins {
    jacoco

    alias(libs.plugins.indra.base)
    alias(libs.plugins.indra.publishing) apply false
    alias(libs.plugins.indra.checkstyle) apply false
    alias(libs.plugins.errorprone)
    alias(libs.plugins.test.logger)
    alias(libs.plugins.versions)
}

group = "love.broccolai.tickets"
version = "6.0.0-SNAPSHOT"

subprojects {
    apply<IndraPlugin>()
    apply<IndraCheckstylePlugin>()
    apply<IndraPublishingPlugin>()
    apply<ErrorPronePlugin>()
    apply<TestLoggerPlugin>()
    apply<JacocoPlugin>()

    repositories {
        mavenCentral()
        sonatype.ossSnapshots()

        maven("https://repo.papermc.io/repository/maven-public")
        maven("https://repo.broccol.ai/snapshots")
    }

    dependencies {
        errorprone(rootProject.libs.errorprone)

        compileOnlyApi(rootProject.libs.jspecify)

        testImplementation(rootProject.libs.mockito)
        testImplementation(rootProject.libs.truth.core)
        testImplementation(rootProject.libs.truth.java.eight)

        testImplementation(rootProject.libs.junit.api)
        testImplementation(rootProject.libs.junit.engine)
        testRuntimeOnly(rootProject.libs.junit.platform)
    }

    configurations.all {
        exclude(group = "org.checkerframework", module = "checker-qual")
        exclude(group = "org.jetbrains", module = "annotations")
    }

    tasks {

        indra {
            gpl3OnlyLicense()
            publishReleasesTo("broccolai", "https://repo.broccol.ai/releases")

            javaVersions {
                target(21)
                testWith(21)
            }

            github("broccolai", "tickets") {
                ci(true)
                issues(true)
            }
        }

        jacocoTestReport {
            reports {
                html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
            }
        }

        testlogger {
            theme = ThemeType.MOCHA_PARALLEL
            showPassed = true
        }

        compileJava {
            options.compilerArgs.add("-parameters")
        }

        test {
            jvmArgs("-Xshare:off")
        }

        processResources {
            expand("version" to rootProject.version)
        }
    }
}

tasks.withType<Jar> {
    onlyIf { false }
}

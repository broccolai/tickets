import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    id("java-library")
    id("com.adarshr.test-logger")
    id("jacoco")
}

tasks.test {
    jvmArgs("-Xshare:off")
}

testlogger {
    theme = ThemeType.MOCHA_PARALLEL
    showPassed = true
}

dependencies {
    testImplementation(libs.bundles.testing)
    testRuntimeOnly(libs.junit.platform)
}

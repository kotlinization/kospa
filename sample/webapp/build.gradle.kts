plugins {
    kotlin("js")
}

dependencies{
    implementation(project(":"))
}

kotlin.js() {
    binaries.executable()
    browser {
        webpackTask {
            destinationDirectory = file("$rootDir/scripts/")
        }
    }
}
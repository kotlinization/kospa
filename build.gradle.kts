plugins {
    kotlin("js") version "1.3.72"
    `maven-publish`
}


group = "org.github.kotlinizer"
version = "0.0.1-alpha1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.8")
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.20.0")
    testImplementation(kotlin("test-js"))
}

kotlin.target.browser {}
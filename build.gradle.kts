plugins {
    kotlin("js") version "1.4.10"
    `maven-publish`
}


group = "org.github.kotlinizer"
version = "0.0.1-alpha2"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-js:1.0.0-RC")
    testImplementation(kotlin("test-js"))
}

kotlin.js().browser {}

publishing {
    publications {
        val tmpVersion = version
        create(project.name, MavenPublication::class) {
            pom {
                packaging = "jar"
            }
            artifactId = project.name
            version = tmpVersion.toString()
            artifact(tasks.getByName("jsSourcesJar"))
        }
    }
}
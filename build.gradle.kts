plugins {
    kotlin("js") version "1.8.0"
    `maven-publish`
}


group = "org.github.kotlinizer"
version = "0.0.1-alpha2"

allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-js:1.5.0")
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
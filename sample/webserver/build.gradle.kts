plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation("io.ktor:ktor-server-cio:2.2.4")
    implementation("io.ktor:ktor-server-html-builder:2.2.4")
    implementation("io.ktor:ktor-server-call-logging:2.2.4")
    implementation("ch.qos.logback:logback-classic:1.4.6")
}

application {
    mainClass.set("sample.webserver.MainKt")
}
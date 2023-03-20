plugins {
    kotlin("multiplatform")
}


kotlin {
    js{
        browser()
    }
    jvm()
}
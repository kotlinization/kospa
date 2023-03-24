package sample.webserver

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import java.io.File
import java.nio.file.FileSystems

fun main() {
    embeddedServer(CIO, port = 3000) {
        install(CallLogging)
        routing {
            route("webapp.js") {
                handle {
                    call.respondFile(getScriptFile())
                }
            }
            route("webapp.js.map") {
                handle {
                    call.respondFile(getScriptMapFile())
                }
            }
            route("favicon.ico") {
                handle {
                    call.respondBytes(byteArrayOf())
                }
            }
            route("/", HttpMethod.Get) {
                handle {
                    call.respondHtml {
                        head {
                            title("Sample app")
                            script(src = "webapp.js") {}
                        }
                        body {
                            div {
                                text("Loading script.")
                            }
                        }
                    }
                }
            }
        }
    }.start(true)
}

private fun getScriptFile(): File {
    return FileSystems.getDefault().getPath("scripts/webapp.js").toFile()
}

private fun getScriptMapFile(): File {
    return FileSystems.getDefault().getPath("scripts/webapp.js.map").toFile()
}
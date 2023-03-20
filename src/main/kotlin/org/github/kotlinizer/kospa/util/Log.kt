package org.github.kotlinizer.kospa.util

object Log {

    fun e(tag: String, message: String) {
        console.error(createLogMessage("E", tag, message))
    }

    fun v(tag: String, message: String) {
        console.info(createLogMessage("V", tag, message))
    }

    private fun createLogMessage(level: String, tag: String, message: String): String {
        return "$level\t$tag\t$message"
    }
}

package sample.webapp

import org.github.kotlinizer.kospa.System
import sample.webapp.activity.AlertActivity
import sample.webapp.activity.MainActivity
import sample.webapp.activity.ResultActivity

suspend fun main() {
    System.startApplication {
        activity(MainActivity::class) {
            launcher = true
        }
        activity(ResultActivity::class)
        activity(AlertActivity::class)
    }
}

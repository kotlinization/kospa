package sample.webapp.activity

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.button
import kotlinx.html.dom.create
import kotlinx.html.js.body
import kotlinx.html.js.onClickFunction
import kotlinx.html.label
import org.github.kotlinizer.kospa.app.Activity
import org.github.kotlinizer.kospa.context.Context
import org.github.kotlinizer.kospa.view.View

class AlertActivity : Activity() {

    override fun onCreate() {
        super.onCreate()
        setContentView(AlertView(this))
    }
}

private class AlertView(private val alertActivity: AlertActivity) : View(alertActivity) {

    override fun createHTMLElement() = document.create.body {
        label { text("ALERT") }
        button {
            text("BACK")
            onClickFunction = {
                alertActivity.finish()
            }
        }
    }
}
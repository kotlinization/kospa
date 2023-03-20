package sample.webapp.activity

import kotlinx.browser.document
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.js.onClickFunction
import kotlinx.html.label
import org.github.kotlinizer.kospa.app.Activity
import org.github.kotlinizer.kospa.context.Context
import org.github.kotlinizer.kospa.context.Intent
import org.github.kotlinizer.kospa.view.View

class MainActivity : Activity() {

    override fun onCreate() {
        super.onCreate()
        setContentView(MainView(this))
    }
}

private class MainView(context: Context) : View(context) {

    override fun createHTMLElement() = document.create.div {
        label { text("MAIN CLASS") }
        button {
            text("ALERT BUTTON")
            onClickFunction = {
                context.startActivity(Intent(context, AlertActivity::class))
            }
        }
    }
}
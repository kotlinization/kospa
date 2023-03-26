package sample.webapp.activity

import kotlinx.browser.document
import kotlinx.html.button
import kotlinx.html.dom.create
import kotlinx.html.js.body
import kotlinx.html.js.onClickFunction
import kotlinx.html.label
import org.github.kotlinizer.kospa.app.Activity
import org.github.kotlinizer.kospa.content.Intent
import org.github.kotlinizer.kospa.os.Bundle
import org.github.kotlinizer.kospa.view.View

class AlertActivity : Activity() {

    val messageId by lazy {
        intent.getIntExtra("message", 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(AlertView(this))
    }
}

private class AlertView(private val alertActivity: AlertActivity) : View(alertActivity) {

    override fun createHTMLElement() = document.create.body {
        label { text("ALERT") }
        label { text("Message: ${alertActivity.messageId}") }
        button {
            text("BACK")
            onClickFunction = {
                alertActivity.finish()
            }
        }
        button {
            text("NEXT")
            onClickFunction = {
                alertActivity.startActivity(
                    Intent(alertActivity, AlertActivity::class).putExtra(
                        "message",
                        alertActivity.messageId + 1
                    )
                )
            }
        }
    }
}
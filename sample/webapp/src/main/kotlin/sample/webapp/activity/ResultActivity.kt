package sample.webapp.activity

import kotlinx.browser.document
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.js.body
import kotlinx.html.js.onClickFunction
import kotlinx.html.label
import kotlinx.html.textInput
import org.github.kotlinizer.kospa.app.Activity
import org.github.kotlinizer.kospa.os.Bundle
import org.github.kotlinizer.kospa.view.View
import org.w3c.dom.HTMLElement

class ResultActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ResultView(this))
    }
}

private class ResultView(private val resultActivity: ResultActivity) : View(resultActivity) {

    override fun createHTMLElement(): HTMLElement = document.create.body {
        div { label { text("Result") } }
        div { textInput { } }
        div {
            button {
                text("OK")
                onClickFunction = {
                    resultActivity.setResult(Activity.RESULT_OK)
                    resultActivity.finish()
                }
            }
            button {
                text("Cancel")
                onClickFunction = {
                    resultActivity.setResult(Activity.RESULT_CANCELED)
                    resultActivity.finish()
                }
            }
        }
    }
}
package sample.webapp.activity

import kotlinx.browser.document
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.js.onClickFunction
import kotlinx.html.label
import org.github.kotlinizer.kospa.app.Activity
import org.github.kotlinizer.kospa.content.Intent
import org.github.kotlinizer.kospa.os.Bundle
import org.github.kotlinizer.kospa.util.Log
import org.github.kotlinizer.kospa.view.View

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(MainView(this))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.v("MainActivity", "Activity result. Code: $requestCode, result: $resultCode")
    }
}

private class MainView(private val mainActivity: MainActivity) : View(mainActivity) {

    override fun createHTMLElement() = document.create.div {
        label { text("MAIN CLASS") }
        button {
            text("Get result")
            onClickFunction = {
                val intent = Intent(context, ResultActivity::class)
                mainActivity.startActivityForResult(intent, 1)
            }
        }
        button {
            text("ALERT BUTTON")
            onClickFunction = {
                val intent = Intent(context, AlertActivity::class)
                    .putExtra("message", 1)
                context.startActivity(intent)
            }
        }
    }
}
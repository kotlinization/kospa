package org.github.kotlinizer.kospa.app

import kotlinx.browser.document
import kotlinx.html.body
import kotlinx.html.dom.create
import org.github.kotlinizer.kospa.content.Intent
import org.github.kotlinizer.kospa.context.ContextWrapper
import org.github.kotlinizer.kospa.os.Bundle
import org.github.kotlinizer.kospa.service.ActivityManager
import org.github.kotlinizer.kospa.view.View

abstract class Activity : ContextWrapper() {

    companion object {
        private val emptyBodyElement by lazy {
            document.create.body()
        }
    }

    lateinit var intent: Intent

    internal var id: String = ""

    private var contentView: View? = null

    open fun onCreate(savedInstanceState: Bundle?) {}

    open fun onStart() {
        document.body = contentView?.bodyHtmlElement ?: emptyBodyElement
    }

    open fun onResume() {}

    open fun onPause() {}

    open fun onStop() {
        document.body = emptyBodyElement
    }

    open fun onDestroy() {}

    protected fun setContentView(view: View) {
        contentView = view
    }

    fun finish() {
        getSystemService(ActivityManager::class)
            .finishActivity(this)
    }
}

package org.github.kotlinizer.kospa.app

import kotlinx.browser.document
import kotlinx.html.body
import kotlinx.html.dom.create
import org.github.kotlinizer.kospa.context.ContextWrapper
import org.github.kotlinizer.kospa.service.ActivityManager
import org.github.kotlinizer.kospa.view.View

abstract class Activity : ContextWrapper() {

    open fun onCreate() {}

    protected fun setContentView(view: View) {
        document.body = if (view.element.tagName.equals("body", ignoreCase = true)) {
            view.element
        } else {
            document.create.body().apply {
                appendChild(view.element)
            }
        }
    }

    fun finish() {
        getSystemService(ActivityManager::class)
            .finishActivity(this)
    }
}

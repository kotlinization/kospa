package org.github.kotlinizer.kospa.view

import kotlinx.browser.document
import kotlinx.html.body
import kotlinx.html.dom.create
import org.github.kotlinizer.kospa.context.Context
import org.w3c.dom.HTMLElement

abstract class View(val context: Context) {

    internal val bodyHtmlElement: HTMLElement by lazy {
        val createdElement = createHTMLElement()
        if (createdElement.tagName.equals("body", ignoreCase = true)) {
            createdElement
        } else {
            document.create.body().apply {
                appendChild(createdElement)
            }
        }
    }

    protected abstract fun createHTMLElement(): HTMLElement
}
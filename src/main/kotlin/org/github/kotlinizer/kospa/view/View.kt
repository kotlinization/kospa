package org.github.kotlinizer.kospa.view

import org.github.kotlinizer.kospa.context.Context
import org.w3c.dom.HTMLElement

abstract class View(val context: Context) {

    internal val element: HTMLElement by lazy {
        createHTMLElement()
    }

    protected abstract fun createHTMLElement(): HTMLElement
}
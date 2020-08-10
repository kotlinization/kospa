package org.github.kotlinizer.kospa

import kotlinx.html.DIV
import kotlinx.html.dom.create
import kotlinx.html.js.div
import org.w3c.dom.Element
import kotlin.browser.document

abstract class Fragment : UIElement() {

    lateinit var parentPage: Page
        private set

    val element: Element by lazy {
        document.create.div(block = createElements)
    }

    protected abstract val createElements: DIV.() -> Unit

    fun show(page: Page) {
        parentPage = page
        elementShown()
    }
}
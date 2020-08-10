package org.github.kotlinizer.kospa

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.dom.create
import kotlinx.html.js.link
import kotlinx.html.js.onLoadFunction
import org.w3c.dom.asList
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.collections.set

object AppManager {

    var activePage: Page = EmptyPage()
        private set

    private val viewConstructors = mutableMapOf<String, () -> Page>()

    private val fragmentConstructors = mutableMapOf<String, () -> Fragment>()

    private val domContentLoaded = CompletableDeferred<Unit>()

    private val documentElement by lazy {
        document.documentElement ?: throw Throwable("No document throwable.")
    }

    private val headElement by lazy {
        document.getElementsByTagName("head")[0] ?: throw Throwable("No head element.")
    }

    init {
        document.addEventListener("DOMContentLoaded", object : EventListener {
            override fun handleEvent(event: Event) {
                domContentLoaded.complete(Unit)
                document.removeEventListener("DOMContentLoaded", this)
            }
        })
    }

    fun registerView(viewClassName: String, construct: () -> Page) {
        viewConstructors[viewClassName] = construct
    }

    fun registerFragment(fragmentClassName: String, construct: () -> Fragment) {
        fragmentConstructors[fragmentClassName] = construct
    }

    fun createFragment(fragmentClassName: String): Fragment {
        return fragmentConstructors[fragmentClassName]?.invoke()
            ?: throw IllegalArgumentException("Fragment constructor for fragment: $fragmentClassName not added.")
    }

    fun start(viewClassName: String, construct: (() -> Page)? = null) {
        if (construct != null) {
            viewConstructors[viewClassName] = construct
        }
        GlobalScope.launch {
            domContentLoaded.await()
            if (Session.firstLoad) {
                replaceView(viewClassName)
            } else {
                val viewName = Session.currentViewClass
                if (viewName == null) {
                    console.error("View should not be null here.")
                    return@launch
                }
                replaceView(viewName)
            }
        }
    }

    suspend fun replaceView(viewClassName: String) {
        domContentLoaded.await()
        val view = viewConstructors[viewClassName]?.invoke()
        if (view == null) {
            console.error("No view constructor registered for view: $viewClassName")
            return
        }
        activePage.destroy()
        addStylesheets(view.stylesheets - activePage.stylesheets)
        removeStylesheets(activePage.stylesheets - view.stylesheets)

        documentElement
            .getElementsByTagName("body")
            .asList()
            .forEach { node ->
                documentElement.removeChild(node)
            }

        documentElement.appendChild(view.element)
        activePage = view
        view.show()
        Session.currentViewClass = viewClassName
    }

    suspend fun addStylesheets(stylesheets: List<String>) {
        domContentLoaded.await()
        stylesheets.forEach { stylesheetHref ->
            val deferred = CompletableDeferred<Unit>()
            val node = document.create.link {
                rel = "stylesheet"
                href = stylesheetHref
                onLoadFunction = {
                    deferred.complete(Unit)
                }
            }
            headElement.appendChild(node)
            deferred.await()
            node.onloadend = null
        }
    }

    private suspend fun removeStylesheets(stylesheets: List<String>) {
        domContentLoaded.await()
        stylesheets.forEach { stylesheetHref ->
            headElement
                .getElementsByTagName("link")
                .asList()
                .forEach { node ->
                    if (node.nodeValue == stylesheetHref) {
                        headElement.removeChild(node)
                    }
                }
        }
    }
}
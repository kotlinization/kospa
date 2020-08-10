package org.github.kotlinizer.kospa

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.html.CommonAttributeGroupFacade
import kotlinx.html.id
import org.w3c.dom.Element
import kotlin.browser.document

abstract class UIElement {

    companion object {
        private var generateId = 0L
    }

    protected val localScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    private val bindTasks = mutableListOf<() -> Unit>()

    fun destroy() {
        localScope.cancel()
        onDestroyed()
    }

    protected fun elementShown() {
        bindTasks.forEach { it() }
        bindTasks.clear()
        onShown()
    }

    protected open fun onShown() {}

    protected open fun onDestroyed() {}

    protected fun CommonAttributeGroupFacade.bind(binder: (element: Element) -> Unit) {
        bind<Element>(binder)
    }

    protected fun <E> CommonAttributeGroupFacade.bind(
        binder: (element: E) -> Unit
    ) {
        val elementId = runCatching { id }.getOrElse {
            "id${generateId++}".also { id = it }
        }
        bindTasks.add {
            val element = document.getElementById(elementId)
            if (element == null) {
                console.error("Unable to find element.")
                return@add
            }
            localScope.launch {
                binder(element.unsafeCast<E>())
            }
        }
    }

    protected fun <T> CommonAttributeGroupFacade.bind(
        flow: Flow<T>,
        binder: (element: Element, data: T) -> Unit
    ) {
        bind<Element, T>(flow, binder)
    }

    protected fun <E, T> CommonAttributeGroupFacade.bind(
        flow: Flow<T>,
        binder: (element: E, data: T) -> Unit
    ) {
        val elementId = runCatching { id }.getOrElse {
            "id${generateId++}".also { id = it }
        }
        bindTasks.add {
            val element = document.getElementById(elementId)
            if (element == null) {
                console.error("Unable to find element.")
                return@add
            }
            localScope.launch {
                flow.collect {
                    binder(element.unsafeCast<E>(), it)
                }
            }
        }
    }
}
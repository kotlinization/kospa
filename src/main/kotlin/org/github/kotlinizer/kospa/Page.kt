package org.github.kotlinizer.kospa

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.html.BODY
import kotlinx.html.dom.create
import kotlinx.html.js.body
import org.w3c.dom.Element
import org.w3c.dom.asList
import kotlinx.browser.document
import kotlinx.serialization.json.JsonNull

abstract class Page(
    protected val pageParams: PageParams = PageParams(JsonNull),
    private val initialFragmentName: String? = null,
    val stylesheets: List<String> = emptyList()
) : UIElement() {

    val currentFragmentStateFlow: StateFlow<Fragment?>
        get() = fragmentMutableStateFlow

    val fragmentDivId = "fragmentDivId"

    val element: Element by lazy {
        document.create.body(block = createElements)
    }

    protected abstract val createElements: BODY.() -> Unit

    private val fragmentMutableStateFlow by lazy {
        MutableStateFlow<Fragment?>(null)
    }

    fun replaceFragment(fragmentClassName: String): Boolean {
        currentFragmentStateFlow.value?.let {
            if (it::class.name == fragmentClassName) {
                return false
            }
        }
        val fragment = AppManager.createFragment(fragmentClassName)
        document.getElementById(fragmentDivId)?.let { node ->
            node.children.asList().forEach { child ->
                node.removeChild(child)
            }
            node.appendChild(fragment.element)
        }
        currentFragmentStateFlow.value?.destroy()
        fragment.show(this)
        Session.setCurrentFragment(this::class.name, fragment::class.name)
        fragmentMutableStateFlow.value = fragment
        return true
    }

    fun show() {
        val shownFragment = Session.getCurrentFragment(this::class.name)
        if (shownFragment != null) {
            replaceFragment(shownFragment)
        } else if (initialFragmentName != null) {
            replaceFragment(initialFragmentName)
        }
        elementShown()
    }
}
package org.github.kotlinizer.kospa

import kotlinx.browser.sessionStorage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Session {

    val firstLoad: Boolean
        get() = sessionStorage.length == 0

    var currentPageClass: String? by StorageStringProperty()

    var currentPageParams: JsonElement by StorageMapProperty()

    private var currentViewFragment: JsonElement by StorageMapProperty()

    fun getCurrentFragment(viewClassName: String): String? {
        return null
//        return currentViewFragment[viewClassName]?.jsonPrimitive?.content
    }

    fun setCurrentFragment(viewClassName: String, currentFragment: String?) {
//        currentViewFragment = if (currentFragment == null) {
//            currentViewFragment.conte.copy(currentViewFragment.content - viewClassName)
//        } else {
//            currentViewFragment.copy(currentViewFragment.content + (viewClassName to Json.parseJson(currentFragment)))
//        }
    }

    private class StorageBooleanProperty<in R>(
        private val defaultValue: Boolean
    ) : ReadWriteProperty<R, Boolean> {

        override fun getValue(thisRef: R, property: KProperty<*>): Boolean {
            return sessionStorage[property.name]?.toBoolean() ?: defaultValue
        }

        override fun setValue(thisRef: R, property: KProperty<*>, value: Boolean) {
            sessionStorage[property.name] = value.toString()
        }
    }

    class StorageStringProperty<in R> : ReadWriteProperty<R, String?> {

        override fun getValue(thisRef: R, property: KProperty<*>): String? {
            return sessionStorage[property.name]
        }

        override fun setValue(thisRef: R, property: KProperty<*>, value: String?) {
            if (value == null) {
                sessionStorage.removeItem(property.name)
            } else {
                sessionStorage[property.name] = value
            }
        }
    }

    class StorageListProperty<T, in R>(
        private val generateString: (T) -> String,
        private val fromString: (String) -> T
    ) : ReadWriteProperty<R, List<T>> {

        override fun getValue(thisRef: R, property: KProperty<*>): List<T> {
            return sessionStorage[property.name]?.split("&")?.map(fromString) ?: emptyList()
        }

        override fun setValue(thisRef: R, property: KProperty<*>, value: List<T>) {
            if (value.isEmpty()) {
                sessionStorage.removeItem(property.name)
            } else {
                sessionStorage[property.name] = value.joinToString(separator = "&", transform = generateString)
            }
        }
    }

    private class StorageMapProperty<in R> : ReadWriteProperty<R, JsonElement> {

        override fun getValue(thisRef: R, property: KProperty<*>): JsonElement {
            val saved = sessionStorage[property.name] ?: return JsonObject(emptyMap())
            return Json.decodeFromString(saved)
        }

        override fun setValue(thisRef: R, property: KProperty<*>, value: JsonElement) {
            sessionStorage[property.name] = value.toString()
        }
    }
}
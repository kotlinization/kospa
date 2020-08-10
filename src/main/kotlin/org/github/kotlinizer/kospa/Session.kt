package org.github.kotlinizer.kospa

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.browser.sessionStorage
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Session {

    val firstLoad: Boolean
        get() = sessionStorage.length == 0

    var currentViewClass: String? by StorageStringProperty()

    private var currentViewFragment: JsonObject by StorageMapProperty()

    fun getCurrentFragment(viewClassName: String): String? {
        return currentViewFragment[viewClassName]?.content
    }

    fun setCurrentFragment(viewClassName: String, currentFragment: String?) {
        currentViewFragment = if (currentFragment == null) {
            currentViewFragment.copy(currentViewFragment.content - viewClassName)
        } else {
            currentViewFragment.copy(currentViewFragment.content + (viewClassName to Json.parseJson(currentFragment)))
        }
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

    private class StorageStringProperty<in R> : ReadWriteProperty<R, String?> {

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

    private class StorageMapProperty<in R> : ReadWriteProperty<R, JsonObject> {

        override fun getValue(thisRef: R, property: KProperty<*>): JsonObject {
            val saved = sessionStorage[property.name] ?: return JsonObject(emptyMap())
            return Json.parseJson(saved).jsonObject
        }

        override fun setValue(thisRef: R, property: KProperty<*>, value: JsonObject) {
            sessionStorage[property.name] = value.toString()
        }
    }
}
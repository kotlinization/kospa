package org.github.kotlinizer.kospa.util

internal fun <T : Any> JsClass<T>.newInstance(): T {
    @Suppress("UNUSED_PARAMETER")
    inline fun callConstructor(constructor: dynamic) = js("new constructor()")
    return callConstructor(asDynamic()) as T
}
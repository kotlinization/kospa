package org.github.kotlinizer.kospa.os

import org.github.kotlinizer.kospa.external.decodeURIComponent
import org.github.kotlinizer.kospa.external.encodeURIComponent

class Bundle {

    companion object {
        internal fun fromUrlEncodedData(data: String): Bundle {
            val bundle = Bundle()
            data.split("&").forEach { parameter ->
                val extras = parameter.split("=").takeIf { it.size == 2 } ?: return@forEach
                bundle.dataMap[decodeURIComponent(extras[0])] = decodeURIComponent(extras[1])
            }
            return bundle
        }
    }

    private val dataMap = mutableMapOf<String, Any>()

    fun putInt(name: String, value: Int) {
        dataMap[name] = value
    }

    fun putAll(bundle: Bundle) {
        dataMap.putAll(bundle.dataMap)
    }

    fun getInt(name: String): Int {
        return getInt(name, 0)
    }

    fun getInt(name: String, defaultValue: Int): Int {
        return when (val value = dataMap[name]) {
            is Int -> value
            is String -> value.toIntOrNull() ?: defaultValue
            else -> defaultValue
        }
    }

    internal fun getUrlEncodedData(): String {
        return dataMap.entries.joinToString("&") { (key, value) ->
            "${encodeURIComponent(key)}=${encodeURIComponent(getStringValue(value))}"
        }
    }

    private fun getStringValue(any: Any): String {
        return when (any) {
            is Int -> any.toString()
            is String -> any
            else -> throw IllegalArgumentException("Class: ${any::class.simpleName} not supported.")
        }
    }
}
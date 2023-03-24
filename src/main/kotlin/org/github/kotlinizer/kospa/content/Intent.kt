package org.github.kotlinizer.kospa.content

import org.github.kotlinizer.kospa.app.Activity
import org.github.kotlinizer.kospa.context.Context
import org.github.kotlinizer.kospa.os.Bundle
import org.github.kotlinizer.kospa.service.ManifestManager
import kotlin.reflect.KClass

class Intent(context: Context, internal val activityClass: KClass<out Activity>) {

    init {
        val manifestManager = context.getSystemService(ManifestManager::class)
        if (manifestManager.getEntry(activityClass) == null) {
            throw IllegalArgumentException("Class ${activityClass.simpleName} is not registered in Manifest.")
        }
    }

    var extras: Bundle? = null
        private set

    fun putExtra(name: String, value: Int): Intent {
        withExtras { putInt(name, value) }
        return this
    }

    fun putExtras(extras: Bundle): Intent {
        withExtras { putAll(extras) }
        return this
    }

    fun getIntExtra(name: String, defaultValue: Int): Int {
        return extras?.getInt(name, defaultValue) ?: defaultValue
    }

    private fun withExtras(update: Bundle.() -> Unit) {
        if (extras?.update() == null) {
            extras = Bundle().apply(update)
        }
    }
}
package org.github.kotlinizer.kospa.context

import org.github.kotlinizer.kospa.app.Activity
import org.github.kotlinizer.kospa.service.ManifestManager
import kotlin.reflect.KClass

class Intent(context: Context, internal val activityClass: KClass<out Activity>) {

    init {
        val manifestManager = context.getSystemService(ManifestManager::class)
        if (manifestManager.getEntry(activityClass) == null) {
            throw IllegalArgumentException("Class ${activityClass.simpleName} is not registered in Manifest.")
        }
    }
}
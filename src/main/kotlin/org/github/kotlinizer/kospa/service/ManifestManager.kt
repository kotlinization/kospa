package org.github.kotlinizer.kospa.service

import org.github.kotlinizer.kospa.app.Activity
import org.github.kotlinizer.kospa.context.Context
import kotlin.reflect.KClass

class ManifestManager internal constructor(context: Context, builder: ManifestBuilder.() -> Unit) :
    SystemService(context) {

    internal val launcherActivity: KClass<out Activity>?
        get() = activities.firstOrNull(Entry::launcher)?.activityClass


    private val activities: List<Entry>

    init {
        val manifestBuilder = ManifestBuilder()
        builder(manifestBuilder)
        activities = manifestBuilder.activities
    }

    internal fun getEntry(activityClass: KClass<out Activity>): Entry? {
        return activities.firstOrNull { it.activityClass == activityClass }
    }

    internal fun getEntry(index: Int): Entry? {
        return activities.getOrNull(index)
    }

    internal fun getEntry(path: String): Entry? {
        return activities.firstOrNull { it.path == path }
    }

    class ManifestBuilder internal constructor() {

        internal val activities = mutableListOf<Entry>()

        fun <T : Activity> activity(activityClass: KClass<T>, config: ActivityConfiguration.() -> Unit = {}) {
            val activityConfig = ActivityConfiguration().apply {
                path = activityClass.simpleName ?: ""
            }
            config(activityConfig)
            activities.add(
                Entry(
                    activityClass = activityClass,
                    launcher = activityConfig.launcher,
                    showInUrl = activityConfig.showInUrl,
                    path = activityConfig.path
                )
            )
        }

    }

    class ActivityConfiguration internal constructor() {
        var launcher = false
        var showInUrl = true
        var path = ""
    }

    internal data class Entry(
        val activityClass: KClass<out Activity>,
        val launcher: Boolean,
        val showInUrl: Boolean,
        val path: String
    )
}
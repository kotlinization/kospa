package org.github.kotlinizer.kospa.service

import kotlinx.browser.window
import org.github.kotlinizer.kospa.app.Activity
import org.github.kotlinizer.kospa.context.Context
import org.github.kotlinizer.kospa.util.Log
import org.w3c.dom.HashChangeEvent
import kotlin.reflect.KClass

private const val TAG = "ActivityManager"

class ActivityManager internal constructor(context: Context) : SystemService(context) {

    private val manifestManager by lazy { getSystemService(ManifestManager::class) }

    override fun onStart() {
        val launcherActivity = manifestManager.launcherActivity
        if (launcherActivity == null) {
            Log.e(TAG, "Launcher activity is not defined.")
            return
        }

        val path = window.location.href.split("#").lastOrNull()
        val pathEntry = path?.let { manifestManager.getEntry(it) }

        window.onhashchange = ::onHashChanged

        if (pathEntry != null) {
            startActivity(pathEntry.activityClass)
        } else {
            startActivity(launcherActivity)
        }
    }

    internal fun startActivity(activityClass: KClass<out Activity>) {
        val entry = manifestManager.getEntry(activityClass)
        if (entry == null) {
            Log.e(TAG, "Activity: ${activityClass.simpleName} is not registered in Manifest.")
            return
        }

        launchActivity(entry)
    }

    internal fun finishActivity(activity: Activity) {
        window.history.back()
    }

    private fun onHashChanged(event: HashChangeEvent) {
        val path = event.newURL.split("#").lastOrNull()
        val pathEntry = path?.let { manifestManager.getEntry(it) } ?: return

        launchActivity(pathEntry)
    }

    private fun launchActivity(entry: ManifestManager.Entry) {
        val activity = entry.activityClass.js.newInstance()
        activity.attachBaseContext(getApplicationContext())
        setLocation(if (entry.showInUrl) entry.path else "")
        activity.onCreate()
    }

    private fun setLocation(path: String) {
        window.location.href = "#$path"
    }
}

private fun <T : Any> JsClass<T>.newInstance(): T {
    @Suppress("UNUSED_PARAMETER")
    inline fun callConstructor(constructor: dynamic) = js("new constructor()")
    return callConstructor(asDynamic()) as T
}
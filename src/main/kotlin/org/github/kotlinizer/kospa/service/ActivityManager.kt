package org.github.kotlinizer.kospa.service

import kotlinx.browser.window
import org.github.kotlinizer.kospa.app.Activity
import org.github.kotlinizer.kospa.content.Intent
import org.github.kotlinizer.kospa.context.Context
import org.github.kotlinizer.kospa.os.Bundle
import org.github.kotlinizer.kospa.util.Log
import org.w3c.dom.HashChangeEvent

private const val TAG = "ActivityManager"

class ActivityManager internal constructor(context: Context) : SystemService(context) {

    private val manifestManager by lazy { getSystemService(ManifestManager::class) }

    private var currentLocation: String = ""

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
            startActivity(Intent(applicationContext, pathEntry.activityClass))
        } else {
            startActivity(Intent(applicationContext, launcherActivity))
        }
    }

    override fun startActivity(intent: Intent) {
        val entry = manifestManager.getEntry(intent.activityClass)
        if (entry == null) {
            Log.e(TAG, "Activity: ${intent.activityClass.simpleName} is not registered in Manifest.")
            return
        }

        launchActivity(entry, intent)
    }

    internal fun finishActivity(activity: Activity) {
        window.history.back()

    }

    private fun onHashChanged(event: HashChangeEvent) {
        if (currentLocation != "" && window.location.href.endsWith("#$currentLocation")) {
            return
        }

        val path = event.newURL.split("#").lastOrNull()
        val pathData = path?.split("?")
        val pathEntry = pathData?.firstOrNull()?.let { manifestManager.getEntry(it) } ?: return

        val bundle = pathData.getOrNull(1)?.let(Bundle::fromUrlEncodedData)
        var intent = Intent(applicationContext, pathEntry.activityClass)
        if (bundle != null) {
            intent = intent.putExtras(bundle)
        }
        launchActivity(pathEntry, intent)
    }

    private fun launchActivity(entry: ManifestManager.Entry, intent: Intent) {
        val activity = entry.activityClass.js.newInstance()
        activity.intent = intent
        activity.attachBaseContext(applicationContext)

        Log.v(
            TAG,
            "Launching activity: ${entry.activityClass.simpleName}, extras: ${intent.extras?.getUrlEncodedData()}"
        )

        val arguments = intent.extras?.getUrlEncodedData() ?: ""
        currentLocation = if (entry.showInUrl) {
            entry.path + if (arguments.isEmpty()) "" else "?$arguments"
        } else {
            ""
        }
        window.location.href = "#$currentLocation"
        activity.onCreate(null)
    }
}

private fun <T : Any> JsClass<T>.newInstance(): T {
    @Suppress("UNUSED_PARAMETER")
    inline fun callConstructor(constructor: dynamic) = js("new constructor()")
    return callConstructor(asDynamic()) as T
}
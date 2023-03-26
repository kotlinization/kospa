package org.github.kotlinizer.kospa.service

import kotlinx.browser.window
import org.github.kotlinizer.kospa.app.Activity
import org.github.kotlinizer.kospa.content.Intent
import org.github.kotlinizer.kospa.context.Context
import org.github.kotlinizer.kospa.os.Bundle
import org.github.kotlinizer.kospa.util.Log
import org.github.kotlinizer.kospa.util.newInstance
import org.w3c.dom.HashChangeEvent

private const val TAG = "ActivityManager"

class ActivityManager internal constructor(context: Context) : SystemService(context) {
    companion object {
        private var activityIdGenerator = 0
    }

    private val manifestManager by lazy { getSystemService(ManifestManager::class) }

    private val activityStack by lazy { ActivityStack() }

    override fun onStart() {
        val launcherActivity = manifestManager.launcherActivity
        if (launcherActivity == null) {
            Log.e(TAG, "Launcher activity is not defined.")
            return
        }

        window.onhashchange = ::onHashChanged

        reconstructActivity(window.location.href)
        if (activityStack.currentActivity == null) {
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
        reconstructActivity(event.newURL)
    }

    private fun reconstructActivity(url: String) {
        val id = window.history.state?.toString()
        val currentActivity = activityStack.currentActivity
        if (currentActivity != null && currentActivity.id == id) {
            return
        }

        val oldActivity = id?.run(activityStack::getById)
        if (oldActivity != null) {
            currentActivity?.onPause()
            currentActivity?.onStop()

            oldActivity.onStart()
            oldActivity.onResume()
            activityStack.moveToTop(oldActivity)
        } else {
            val path = url.split("#").lastOrNull()
            val pathData = path?.split("?")
            val pathEntry = pathData?.firstOrNull()?.let { manifestManager.getEntry(it) } ?: return

            val bundle = pathData.getOrNull(1)?.let(Bundle::fromUrlEncodedData)
            var intent = Intent(applicationContext, pathEntry.activityClass)
            if (bundle != null) {
                intent = intent.putExtras(bundle)
            }
            launchActivity(pathEntry, intent)
        }
    }

    private fun launchActivity(entry: ManifestManager.Entry, intent: Intent) {
        val activity = entry.activityClass.js.newInstance()
        activity.id = (activityIdGenerator++).toString()
        activity.intent = intent
        activity.attachBaseContext(applicationContext)
        activityStack.push(activity)

        val arguments = intent.extras?.getUrlEncodedData() ?: ""
        val currentLocation = if (entry.showInUrl) {
            entry.path + if (arguments.isEmpty()) "" else "?$arguments"
        } else {
            ""
        }
        window.location.href = "#$currentLocation"
        window.history.replaceState(activity.id, "")
        activity.onCreate(null)

        activityStack.currentActivity?.onPause()
        activityStack.currentActivity?.onStop()

        activity.onStart()
        activity.onResume()
    }
}

private class ActivityStack {

    var currentActivity: Activity? = null
        private set

    private val activities = mutableListOf<Activity>()

    fun getById(id: String): Activity? {
        return activities.firstOrNull { it.id == id }
    }

    fun push(activity: Activity) {
        activities.add(activity)
        currentActivity = activity
    }

    fun moveToTop(activity: Activity) {
        activities.remove(activity)
        push(activity)
    }
}
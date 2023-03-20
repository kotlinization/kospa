package org.github.kotlinizer.kospa

import kotlinx.browser.document
import kotlinx.coroutines.CompletableDeferred
import org.github.kotlinizer.kospa.app.Application
import org.github.kotlinizer.kospa.context.Context
import org.github.kotlinizer.kospa.context.Intent
import org.github.kotlinizer.kospa.service.ActivityManager
import org.github.kotlinizer.kospa.service.ManifestManager
import org.github.kotlinizer.kospa.service.SystemService
import org.github.kotlinizer.kospa.util.Log
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener

object System {

    private val domContentLoaded = CompletableDeferred<Unit>()

    init {
        document.addEventListener("DOMContentLoaded", object : EventListener {
            override fun handleEvent(event: Event) {
                domContentLoaded.complete(Unit)
                document.removeEventListener("DOMContentLoaded", this)
            }
        })
    }

    suspend fun startApplication(
        application: Application = Application(),
        manifestRegistration: ManifestManager.ManifestBuilder.() -> Unit
    ) {
        val context = SystemContext(application)
        application.attachBaseContext(context)

        val systemService = listOf(
            ManifestManager(context, manifestRegistration),
            ActivityManager(context)
        )

        systemService.forEach(context::registerSystemService)

        domContentLoaded.await()

        systemService.forEach(SystemService::onStart)
    }
}

private class SystemContext(private val application: Application) : Context() {

    override fun getApplicationContext(): Context {
        return application
    }

    override fun startActivity(intent: Intent) {
        getSystemService(ActivityManager::class).startActivity(intent.activityClass)
    }

}
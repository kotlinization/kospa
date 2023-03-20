package org.github.kotlinizer.kospa.context

import org.github.kotlinizer.kospa.service.SystemService
import kotlin.reflect.KClass

abstract class Context {

    private val systemServices = mutableMapOf<KClass<out SystemService>, SystemService>()

    abstract fun getApplicationContext(): Context

    @Suppress("UNCHECKED_CAST")
    open fun <T : SystemService> getSystemService(systemServiceClass: KClass<T>): T {
        return systemServices[systemServiceClass] as T
    }

    abstract fun startActivity(intent: Intent)

    internal open fun registerSystemService(systemService: SystemService) {
        if (systemServices.containsKey(systemService::class)) {
            throw IllegalStateException("System service: ${systemService::class.simpleName} already registered!")
        }
        systemServices[systemService::class] = systemService
    }
}
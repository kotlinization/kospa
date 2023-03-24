package org.github.kotlinizer.kospa.context

import org.github.kotlinizer.kospa.content.Intent
import org.github.kotlinizer.kospa.service.SystemService
import kotlin.reflect.KClass

abstract class ContextWrapper(base: Context? = null) : Context() {

    private lateinit var base: Context

    init {
        if (base != null) {
            this.base = base
        }
    }

    internal fun attachBaseContext(base: Context) {
        if (this::base.isInitialized) {
            throw IllegalStateException("Base context is already attached.")
        }
        this.base = base
    }

    override val applicationContext: Context
        get() = base.applicationContext

    override fun <T : SystemService> getSystemService(systemServiceClass: KClass<T>): T {
        return base.getSystemService(systemServiceClass)
    }

    override fun startActivity(intent: Intent) {
        base.startActivity(intent)
    }

    override fun registerSystemService(systemService: SystemService) {
        base.registerSystemService(systemService)
    }
}
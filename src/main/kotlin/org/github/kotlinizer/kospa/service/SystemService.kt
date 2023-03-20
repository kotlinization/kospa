package org.github.kotlinizer.kospa.service

import org.github.kotlinizer.kospa.context.Context
import org.github.kotlinizer.kospa.context.ContextWrapper

abstract class SystemService(context: Context) : ContextWrapper(context) {

    internal open fun onStart() {

    }
}
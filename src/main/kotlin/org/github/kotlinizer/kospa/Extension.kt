package org.github.kotlinizer.kospa

import kotlin.reflect.KClass

val <T : Page> KClass<T>.name
    get() = simpleName ?: throw IllegalArgumentException("View must not be anonymous.")

val <T : Fragment> KClass<T>.name
    get() = simpleName ?: throw IllegalArgumentException("Fragment must not be anonymous.")
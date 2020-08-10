package org.github.kotlinizer.kospa

import kotlinx.html.BODY

internal class EmptyPage : Page() {

    override val createElements: BODY.() -> Unit = {

    }
}
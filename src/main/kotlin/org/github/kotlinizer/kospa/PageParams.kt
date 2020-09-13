package org.github.kotlinizer.kospa

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

data class PageParams(
    val data: JsonElement = JsonNull
)
package com.nerosec.noxx.commons.extensions

import com.nerosec.noxx.commons.persistence.entity.EntityType

object StringExtensions {

    fun String.isEntityId(vararg entityTypes: EntityType): Boolean {
        val entityTypes = if (entityTypes.isEmpty()) EntityType.entries.toSet() else entityTypes.toSet()
        val entityTypesRegexGroup = "(${entityTypes.joinToString("|") { it.value }})"
        return matches(Regex("$entityTypesRegexGroup.[0-9A-F]{8}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{12}"))
    }
}
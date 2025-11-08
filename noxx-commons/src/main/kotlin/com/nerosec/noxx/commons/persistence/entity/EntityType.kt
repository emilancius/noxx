package com.nerosec.noxx.commons.persistence.entity

import java.util.UUID

enum class EntityType(val value: String) {
    PLACEHOLDER("PLACEHOLDER"); // TODO: EntityType.PLACEHOLDER is to be removed.

    fun generateEntityId(): String = "$value.${UUID.randomUUID()}".uppercase()
}
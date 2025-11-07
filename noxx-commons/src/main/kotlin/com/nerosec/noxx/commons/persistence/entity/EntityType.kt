package com.nerosec.noxx.commons.persistence.entity

import java.util.UUID

enum class EntityType {
    PLACEHOLDER; // TODO: EntityType.PLACEHOLDER is to be removed.

    fun generateEntityId(): String = "${name.replace('_', '-')}.${UUID.randomUUID()}".uppercase()
}
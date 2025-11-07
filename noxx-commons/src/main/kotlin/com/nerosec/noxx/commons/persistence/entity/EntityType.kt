package com.nerosec.noxx.commons.persistence.entity

import java.util.UUID

enum class EntityType {
    ;

    fun generateEntityId(): String = "${name.replace('_', '-')}.${UUID.randomUUID()}".uppercase()
}
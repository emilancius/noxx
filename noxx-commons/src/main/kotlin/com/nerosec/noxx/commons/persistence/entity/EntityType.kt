package com.nerosec.noxx.commons.persistence.entity

import java.util.UUID

enum class EntityType(val value: String) {
    ACTION("ACTION");

    fun generateEntityId(): String = "$value.${UUID.randomUUID()}".uppercase()
}
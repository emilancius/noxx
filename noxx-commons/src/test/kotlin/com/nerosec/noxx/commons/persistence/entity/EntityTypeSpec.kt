package com.nerosec.noxx.commons.persistence.entity

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EntityTypeSpec {

    @Test
    fun `generateEntityId - generates entity id, based on entity type`() {
        EntityType.entries.forEach {
            val entityId = it.generateEntityId()
            assertEquals(37 + it.name.length, entityId.length)
            assertEquals(it.name, entityId.substringBefore('.').replace('-', '_'))
        }
    }
}
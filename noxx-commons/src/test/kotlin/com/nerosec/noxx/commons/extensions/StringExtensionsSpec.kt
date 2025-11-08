package com.nerosec.noxx.commons.extensions

import com.nerosec.noxx.commons.extensions.StringExtensions.isEntityId
import com.nerosec.noxx.commons.persistence.entity.EntityType
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class StringExtensionsSpec {

    @Test
    fun `isEntityId - determines if string is entity id of any of specified entity types`() {
        EntityType.entries.forEach { entityType ->
            assertTrue { entityType.generateEntityId().isEntityId(entityType) }
        }
        assertFalse("<non_entity_id_string>".isEntityId())
    }
}
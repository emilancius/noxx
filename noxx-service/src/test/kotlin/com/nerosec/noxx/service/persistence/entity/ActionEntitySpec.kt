package com.nerosec.noxx.service.persistence.entity

import com.nerosec.noxx.commons.persistence.entity.EntityType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

class ActionEntitySpec {

    companion object {

    }

    @Test
    fun `copy - creates a copy of an ActionEntity`() {
        val created = Instant.now().minus(5, ChronoUnit.MINUTES)
        val startStepId = UUID.randomUUID().toString().uppercase()
        val catchStepId = UUID.randomUUID().toString().uppercase()
        val startStep = ActionEntity.Step(id = startStepId, name = "<start_step_name>")
        val catchStep = ActionEntity.Step(id = catchStepId, name = "<catch_step_name>")
        val entity = ActionEntity(
            id = EntityType.ACTION.generateEntityId(),
            name = "<name>",
            description = "<description>",
            version = 1,
            startStepId = startStepId,
            catchStepId = catchStepId,
            timeout = 3_600_000,
            steps = setOf(startStep, catchStep),
            etag = System.nanoTime().toString(),
            created = created,
            lastUpdated = created.plus(5, ChronoUnit.MINUTES)
        )
        val copy = entity.copy()
        assertFalse { entity === copy }
        assertEquals(entity.id, copy.id)
        assertEquals(entity.name, copy.name)
        assertEquals(entity.description, copy.description)
        assertEquals(entity.version, copy.version)
        assertEquals(entity.startStepId, copy.startStepId)
        assertEquals(entity.catchStepId, copy.catchStepId)
        assertEquals(entity.timeout, copy.timeout)
        assertEquals(entity.steps?.size, copy.steps?.size)
        assertEquals(entity.steps, copy.steps)
        assertEquals(entity.etag, copy.etag)
        assertEquals(entity.created, copy.created)
        assertEquals(entity.lastUpdated, copy.lastUpdated)
    }

    @Test
    fun `Step#copy - creates a copy of a Step`() {
        val step = ActionEntity.Step(
            id = UUID.randomUUID().toString().uppercase(),
            name = "<name>",
            description = "<description>",
            timeout = 60_000,
            retryCount = 5
        )
        val copy = step.copy()
        assertFalse { step === copy }
        assertEquals(step.id, copy.id)
        assertEquals(step.name, copy.name)
        assertEquals(step.description, copy.description)
        assertEquals(step.timeout, copy.timeout)
        assertEquals(step.retryCount, copy.retryCount)
    }

    @Test
    fun `Step#create - creates a Step`() {
        val step = ActionEntity.Step.create("<name>")
        assertTrue { step.id != null }
        assertEquals("<name>", step.name)
        assertTrue { step.description == null }
        assertEquals(ActionEntity.Step.DEFAULT_TIMEOUT_MS, step.timeout)
        assertEquals(ActionEntity.Step.DEFAULT_RETRY_COUNT, step.retryCount)
    }
}
package com.nerosec.noxx.service.persistence.entity

import com.nerosec.noxx.commons.persistence.entity.BaseEntity
import com.nerosec.noxx.commons.persistence.entity.EntityType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = ActionEntity.TABLE)
class ActionEntity() : BaseEntity(EntityType.ACTION) {

    companion object {
        const val TABLE = "ACTIONS"

        const val COL_NAME = "NAME"
        const val COL_DESCRIPTION = "DESCRIPTION"
        const val COL_VERSION = "VERSION"
        const val COL_START_STEP_ID = "START_STEP_ID"
        const val COL_CATCH_STEP_ID = "CATCH_STEP_ID"
        const val COL_TIMEOUT_MS = "TIMEOUT_MS"
        const val COL_STEPS = "STEPS"

        const val DEFAULT_VERSION: Long = 1
        const val DEFAULT_TIMEOUT_MS: Long = 3_600_000
    }

    @Column(name = COL_NAME)
    var name: String? = null

    @Column(name = COL_DESCRIPTION)
    var description: String? = null

    @Column(name = COL_VERSION)
    var version: Long? = null

    @Column(name = COL_START_STEP_ID)
    var startStepId: String? = null

    @Column(name = COL_CATCH_STEP_ID)
    var catchStepId: String? = null

    @Column(name = COL_TIMEOUT_MS)
    var timeout: Long? = null

    @Column(name = COL_STEPS)
    var steps: Set<Step>? = null

    constructor(
        id: String? = null,
        name: String? = null,
        description: String? = null,
        version: Long? = DEFAULT_VERSION,
        startStepId: String? = null,
        catchStepId: String? = null,
        timeout: Long? = DEFAULT_TIMEOUT_MS,
        steps: Set<Step>? = null,
        etag: String? = null,
        created: Instant? = null,
        lastUpdated: Instant? = null
    ) : this() {
        this.id = id
        this.name = name
        this.description = description
        this.version = version
        this.startStepId = startStepId
        this.catchStepId = catchStepId
        this.timeout = timeout
        this.steps = steps
        this.etag = etag
        this.created = created
        this.lastUpdated = lastUpdated
    }

    fun copy(
        id: String? = this.id,
        name: String? = this.name,
        description: String? = this.description,
        version: Long? = this.version,
        startStepId: String? = this.startStepId,
        catchStepId: String? = this.catchStepId,
        timeout: Long? = this.timeout,
        steps: Set<Step>? = this.steps,
        etag: String? = this.etag,
        created: Instant? = this.created,
        lastUpdated: Instant? = this.lastUpdated
    ): ActionEntity = ActionEntity(
        id = id,
        name = name,
        description = description,
        version = version,
        startStepId = startStepId,
        catchStepId = catchStepId,
        timeout = timeout,
        steps = steps?.toSet(),
        etag = etag,
        created = created,
        lastUpdated = lastUpdated
    )

    class Step() {

        var id: String? = null
        var name: String? = null
        var description: String? = null
        var timeout: Long? = null
        var retryCount: Long? = null

        companion object {
            const val DEFAULT_TIMEOUT_MS: Long = 60_000
            const val DEFAULT_RETRY_COUNT: Long = 5

            fun create(
                name: String,
                description: String? = null,
                timeout: Long = DEFAULT_TIMEOUT_MS,
                retryCount: Long = DEFAULT_RETRY_COUNT
            ): Step = Step(
                id = generateId(),
                name = name,
                description = description,
                timeout = timeout,
                retryCount = retryCount
            )

            private fun generateId(): String = UUID.randomUUID().toString().uppercase()
        }

        constructor(
            id: String? = null,
            name: String? = null,
            description: String? = null,
            timeout: Long? = DEFAULT_TIMEOUT_MS,
            retryCount: Long? = DEFAULT_RETRY_COUNT
        ) : this() {
            this.id = id ?: generateId()
            this.name = name
            this.description = description
            this.timeout = timeout
            this.retryCount = retryCount
        }

        fun copy(
            id: String? = this.id,
            name: String? = this.name,
            description: String? = this.description,
            timeout: Long? = this.timeout,
            retryCount: Long? = this.retryCount
        ): Step = Step(
            id = id,
            name = name,
            description = description,
            timeout = timeout,
            retryCount = retryCount
        )
    }
}
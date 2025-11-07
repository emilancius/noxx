package com.nerosec.noxx.commons.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.Instant

@MappedSuperclass
abstract class BaseEntity(val entityType: EntityType) {

    companion object {
        const val COL_ID = "ID"
        const val COL_ETAG = "ETAG"
        const val COL_CREATED_AT = "CREATED_AT"
        const val COL_LAST_UPDATED_AT = "LAST_UPDATED_AT"
    }

    @Id
    @Column(name = COL_ID)
    var id: String? = null

    @Column(name = COL_ETAG)
    var etag: String? = null

    @Column(name = COL_CREATED_AT)
    var created: Instant? = null

    @Column(name = COL_LAST_UPDATED_AT)
    var lastUpdated: Instant? = null

    @PrePersist
    fun beforeCreate() {
        if (id == null) id = generateId()
        if (etag == null) etag = generateEtag()
        if (created == null) created = Instant.now()
    }

    @PreUpdate
    fun beforeUpdate() {
        etag = generateEtag()
        lastUpdated = Instant.now()
    }

    private fun generateId(): String = entityType.generateEntityId()

    private fun generateEtag(): String = System.nanoTime().toString()
}
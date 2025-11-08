package com.nerosec.noxx.commons.service

import com.nerosec.noxx.commons.exceptions.EntityException
import com.nerosec.noxx.commons.persistence.entity.BaseEntity
import com.nerosec.noxx.commons.persistence.entity.BaseEntity_
import com.nerosec.noxx.commons.persistence.entity.EntityType
import com.nerosec.noxx.commons.persistence.repository.BaseRepository
import com.nerosec.noxx.commons.preconditions.Preconditions.requireArgument
import com.nerosec.noxx.commons.preconditions.Preconditions.requireArgumentIsEntityId
import com.nerosec.noxx.commons.preconditions.Preconditions.requireArgumentIsInCollection
import com.nerosec.noxx.commons.preconditions.Preconditions.requireArgumentIsSpecified
import com.nerosec.noxx.commons.preconditions.Preconditions.requireState
import jakarta.persistence.metamodel.SingularAttribute
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification

abstract class BaseService<T : BaseEntity>(private val repository: BaseRepository<T>) {

    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_PAGE_SIZE = 50
        const val DEFAULT_PROPERTY_TO_SORT_BY = BaseEntity_.PROP_CREATED_AT
        val DEFAULT_SORT_ORDER = Sort.Direction.ASC
        val DEFAULT_PROPERTIES_TO_QUERY_BY = HashMap<String, Any?>()

        private const val MIN_PAGE = 1
        private const val MIN_PAGE_SIZE = 1
        private const val MAX_PAGE_SIZE = 100
    }

    abstract fun getEntityType(): EntityType

    abstract fun getEntityPropertyTypes(): Map<String, SingularAttribute<in T, out Any>>

    abstract fun getEntitySortByProperties(): Set<String>

    abstract fun getEntityQueryByProperties(): Set<String>

    abstract fun getListSpec(queryBy: Map<String, Any?>): Specification<T>?

    protected fun getEntityById(id: String): T {
        requireArgumentIsSpecified("id", id)
        requireArgumentIsEntityId("id", id, getEntityType())
        return try {
            repository.getReferenceById(id)
        } catch (exception: Exception) {
            throw EntityException("Entity \"$id\" could not be found.", exception)
        }
    }

    protected fun getEntities(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        sortBy: String = DEFAULT_PROPERTY_TO_SORT_BY,
        sortOrder: Sort.Direction = DEFAULT_SORT_ORDER,
        queryBy: Map<String, Any?> = DEFAULT_PROPERTIES_TO_QUERY_BY
    ): Page<T> {
        requireArgument({ page > (MIN_PAGE - 1) }) { "Argument's \"page\" value ($page) cannot be less than $MIN_PAGE." }
        requireArgument({ pageSize in MIN_PAGE_SIZE..MIN_PAGE_SIZE }) { "Argument's \"pageSize\" value ($pageSize) must be in [$MIN_PAGE_SIZE; $MAX_PAGE_SIZE] range." }
        requireArgumentIsInCollection("sortBy", sortBy, getEntitySortByProperties()) { "Argument's \"sortBy\" value ($sortBy) is not a supported property to sort by." }
        val entityQueryByProperties = getEntityQueryByProperties()
        queryBy.keys.forEach {
            requireArgumentIsInCollection("queryBy", it, entityQueryByProperties) { "Argument \"queryBy\" contains a property ($it), that is not supported property to query by." }
        }
        val spec = getListSpec(queryBy) ?: createListSpec(queryBy)
        return repository.findAll(spec, PageRequest.of(page - 1, pageSize, Sort.by(sortOrder, sortBy)))
    }

    protected fun checkEtag(entity: T, etag: String?, message: (() -> String?)? = null) =
        etag?.let { requireState({ it == entity.etag }) { message?.invoke() ?: "Etag mismatch." } }

    private fun createListSpec(queryBy: Map<String, Any?>): Specification<T> =
        Specification<T> { root, _, cb ->
            val propertyTypes = getEntityPropertyTypes()
            val predicates = queryBy.map { (property, value) -> propertyTypes[property]?.let { cb.equal(root[it], value) } }
            cb.and(*predicates.toTypedArray())
        }
}
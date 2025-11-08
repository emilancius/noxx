package com.nerosec.noxx.commons.preconditions

import com.nerosec.noxx.commons.exceptions.ArgumentException
import com.nerosec.noxx.commons.exceptions.StateException
import com.nerosec.noxx.commons.extensions.StringExtensions.isEntityId
import com.nerosec.noxx.commons.persistence.entity.EntityType

object Preconditions {

    fun require(condition: () -> Boolean, exception: Exception) {
        if (!condition.invoke()) throw exception
    }

    fun requireArgument(condition: () -> Boolean, message: (() -> String?)? = null) = require(condition, ArgumentException(message?.invoke()))

    fun requireArgumentIsSpecified(name: String, argument: Any?, message: (() -> String?)? = null) =
        requireArgument({ argument != null && (if (argument is String) !argument.isEmpty() else true) }) {
            message?.invoke() ?: "Argument \"$name\" is required, but not specified."
        }

    fun requireArgumentIsEntityId(name: String, argument: String, entityType: EntityType, message: (() -> String?)? = null) =
        requireArgument({ argument.isEntityId(entityType) }) {
            message?.invoke() ?: "Argument's \"$name\" value ($argument) is not an entity id for an entity of type \"${entityType.value}\"."
        }

    fun requireArgumentIsInCollection(name: String, argument: Any?, collection: Collection<Any?>, message: (() -> String?)? = null) =
        requireArgument({ argument in collection }) {
            message?.invoke() ?: "Argument's \"$name\" value ($argument) must be one of $collection."
        }

    fun requireState(condition: () -> Boolean, message: (() -> String?)? = null) = require(condition, StateException(message?.invoke()))
}

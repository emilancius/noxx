package com.nerosec.noxx.commons.preconditions

import com.nerosec.noxx.commons.exceptions.ArgumentException
import com.nerosec.noxx.commons.exceptions.StateException
import com.nerosec.noxx.commons.persistence.entity.EntityType
import com.nerosec.noxx.commons.preconditions.Preconditions.require
import com.nerosec.noxx.commons.preconditions.Preconditions.requireArgument
import com.nerosec.noxx.commons.preconditions.Preconditions.requireArgumentIsEntityId
import com.nerosec.noxx.commons.preconditions.Preconditions.requireArgumentIsInCollection
import com.nerosec.noxx.commons.preconditions.Preconditions.requireArgumentIsSpecified
import com.nerosec.noxx.commons.preconditions.Preconditions.requireState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PreconditionsSpec {

    companion object {
        private const val TEST_ERROR_MESSAGE = "<error_message>"
        private const val TEST_ARGUMENT_NAME = "<argument_name>"
        private const val TEST_NON_ENTITY_ID_STRING = "<non_entity_id_string>"
    }

    @Test
    fun `require - produces specified exception if condition is not met`() {
        var exception = assertThrows<Exception> {
            require(createCondition(ConditionType.FAILING), Exception(TEST_ERROR_MESSAGE))
        }
        assertEquals(TEST_ERROR_MESSAGE, exception.message)
        assertTrue { exception.cause == null }

        exception = assertThrows<Exception> {
            require(createCondition(ConditionType.FAILING), Exception())
        }
        assertTrue { exception.message == null }
    }

    @Test
    fun `require - exception is not produced if precondition is met`() {
        require(createCondition(ConditionType.SUCCEEDING), Exception(TEST_ERROR_MESSAGE))
    }

    @Test
    fun `requireArgument - produces ArgumentException if condition is not met`() {
        var exception = assertThrows<ArgumentException> {
            requireArgument(createCondition(ConditionType.FAILING)) { TEST_ERROR_MESSAGE }
        }
        assertEquals(TEST_ERROR_MESSAGE, exception.message)
        assertTrue { exception.cause == null }

        exception = assertThrows<ArgumentException> {
            requireArgument(createCondition(ConditionType.FAILING))
        }
        assertTrue(exception.message == null)
    }

    @Test
    fun `requireArgument - exception is not produced if precondition is met`() {
        requireArgument(createCondition(ConditionType.SUCCEEDING)) { TEST_ERROR_MESSAGE }
    }

    @Test
    fun `requireArgumentIsSpecified - produces ArgumentException if argument is not specified`() {
        arrayOf<Any?>(null, "").forEach {
            var exception = assertThrows<ArgumentException> {
                requireArgumentIsSpecified(TEST_ARGUMENT_NAME, it)
            }
            assertExceptionContainsMessage(exception)
            exception = assertThrows<ArgumentException> {
                requireArgumentIsSpecified(TEST_ARGUMENT_NAME, it) { null }
            }
            assertExceptionContainsMessage(exception)
            exception = assertThrows<ArgumentException> {
                requireArgumentIsSpecified(TEST_ARGUMENT_NAME, null) { TEST_ERROR_MESSAGE }
            }
            assertEquals(TEST_ERROR_MESSAGE, exception.message)
        }
    }

    @Test
    fun `requireArgumentIsSpecified - exception is not produced if argument is specified`() {
        requireArgumentIsSpecified(TEST_ARGUMENT_NAME, "<string>")
    }

    @Test
    fun `requireArgumentIsEntityId - produces ArgumentException if specified argument is not an entity id for an entity of specified type`() {
        EntityType.entries.forEach {
            var exception = assertThrows<ArgumentException> {
                requireArgumentIsEntityId(TEST_ARGUMENT_NAME, TEST_NON_ENTITY_ID_STRING, it)
            }
            assertExceptionContainsMessage(exception)
            exception = assertThrows<ArgumentException> {
                requireArgumentIsEntityId(TEST_ARGUMENT_NAME, TEST_NON_ENTITY_ID_STRING, it) { null }
            }
            assertExceptionContainsMessage(exception)
            exception = assertThrows<ArgumentException> {
                requireArgumentIsEntityId(TEST_ARGUMENT_NAME, TEST_NON_ENTITY_ID_STRING, it) { TEST_ERROR_MESSAGE }
            }
            assertEquals(TEST_ERROR_MESSAGE, exception.message)
        }
    }

    @Test
    fun `requireArgumentIsEntityId - exception is not produced if specified argument is an entity id for an entity of specified type`() {
        EntityType.entries.forEach {
            requireArgumentIsEntityId(TEST_ARGUMENT_NAME, it.generateEntityId(), it)
        }
    }

    @Test
    fun `requireArgumentIsInCollection - produces ArgumentException if specified argument is not in specified collection`() {
        val collection = setOf(1, 2, 3, 4, 5)
        var exception = assertThrows<ArgumentException> {
            requireArgumentIsInCollection(TEST_ARGUMENT_NAME, 6, collection)
        }
        assertExceptionContainsMessage(exception)
        exception = assertThrows<ArgumentException> {
            requireArgumentIsInCollection(TEST_ARGUMENT_NAME, 6, collection) { null }
        }
        assertExceptionContainsMessage(exception)
        exception = assertThrows<ArgumentException> {
            requireArgumentIsInCollection(TEST_ARGUMENT_NAME, 6, collection) { TEST_ERROR_MESSAGE }
        }
        assertEquals(TEST_ERROR_MESSAGE, exception.message)
    }

    @Test
    fun `requireArgumentIsInCollection - exception is not produced if specified argument is in specified collection`() {
        requireArgumentIsInCollection(TEST_ARGUMENT_NAME, 1, setOf(1, 2, 3))
    }

    @Test
    fun `requireState - produces StateException if condition is not met`() {
        var exception = assertThrows<StateException> {
            requireState(createCondition(ConditionType.FAILING)) { TEST_ERROR_MESSAGE }
        }
        assertEquals(TEST_ERROR_MESSAGE, exception.message)
        assertTrue { exception.cause == null }

        exception = assertThrows<StateException> {
            requireState(createCondition(ConditionType.FAILING))
        }
        assertTrue(exception.message == null)
    }

    @Test
    fun `requireState - exception is not produced if precondition is not met`() {
        requireState(createCondition(ConditionType.SUCCEEDING)) { TEST_ERROR_MESSAGE }
    }

    private fun assertExceptionContainsMessage(exception: Exception) {
        assertTrue {
            val message = exception.message
            message != null && !message.isEmpty()
        }
    }

    private enum class ConditionType {
        SUCCEEDING,
        FAILING
    }

    private fun createCondition(type: ConditionType): Function0<Boolean> = { type == ConditionType.SUCCEEDING }
}
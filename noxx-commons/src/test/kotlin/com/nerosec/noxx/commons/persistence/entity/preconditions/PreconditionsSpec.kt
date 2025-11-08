package com.nerosec.noxx.commons.persistence.entity.preconditions

import com.nerosec.noxx.commons.exceptions.ArgumentException
import com.nerosec.noxx.commons.exceptions.StateException
import com.nerosec.noxx.commons.preconditions.Preconditions.require
import com.nerosec.noxx.commons.preconditions.Preconditions.requireArgument
import com.nerosec.noxx.commons.preconditions.Preconditions.requireState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PreconditionsSpec {

    companion object {
        private const val TEST_ERROR_MESSAGE = "<error_message>"
    }

    @Test
    fun `require - produces specified exception if condition is not met`() {
        var exception = assertThrows<Exception> {
            require(createCondition(ConditionType.FAILING), Exception(TEST_ERROR_MESSAGE))
        }
        assertEquals(TEST_ERROR_MESSAGE, exception.message)
        assertTrue { exception.cause == null }

        exception = assertThrows<Exception> { require(createCondition(ConditionType.FAILING), Exception()) }
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

        exception = assertThrows<ArgumentException> { requireArgument(createCondition(ConditionType.FAILING)) }
        assertTrue(exception.message == null)
    }

    @Test
    fun `requireArgument - exception is not produced if precondition is met`() {
        requireArgument(createCondition(ConditionType.SUCCEEDING)) { TEST_ERROR_MESSAGE }
    }

    @Test
    fun `requireState - produces StateException if condition is not met`() {
        var exception = assertThrows<StateException> {
            requireState(createCondition(ConditionType.FAILING)) { TEST_ERROR_MESSAGE }
        }
        assertEquals(TEST_ERROR_MESSAGE, exception.message)
        assertTrue { exception.cause == null }

        exception = assertThrows<StateException> { requireState(createCondition(ConditionType.FAILING)) }
        assertTrue(exception.message == null)
    }

    @Test
    fun `requireState - exception is not produced if precondition is not met`() {
        requireState(createCondition(ConditionType.SUCCEEDING)) { TEST_ERROR_MESSAGE }
    }

    private enum class ConditionType {
        SUCCEEDING,
        FAILING
    }

    private fun createCondition(type: ConditionType): Function0<Boolean> = { type == ConditionType.SUCCEEDING }
}
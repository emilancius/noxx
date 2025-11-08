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
        private const val TEST_ERROR_MESSAGE = "<ERROR_MESSAGE>"
    }

    @Test
    fun `require - produces specified exception if condition is not met`() {
        var exception = assertThrows<Exception> { require(false, Exception(TEST_ERROR_MESSAGE)) }
        assertEquals(TEST_ERROR_MESSAGE, exception.message)
        exception = assertThrows<Exception> { require(false, Exception()) }
        assertTrue(exception.message == null)
    }

    @Test
    fun `require - exception is not produced if precondition is met`() {
        require(true, Exception(TEST_ERROR_MESSAGE))
    }

    @Test
    fun `requireArgument - produces ArgumentException if condition is not met`() {
        var exception = assertThrows<ArgumentException> { requireArgument(false) { TEST_ERROR_MESSAGE } }
        assertEquals(TEST_ERROR_MESSAGE, exception.message)
        exception = assertThrows<ArgumentException> { requireArgument(false) }
        assertTrue(exception.message == null)
    }

    @Test
    fun `requireArgument - exception is not produced if precondition is met`() {
        requireArgument(true) { TEST_ERROR_MESSAGE }
    }

    @Test
    fun `requireState - produces StateException if condition is not met`() {
        var exception = assertThrows<StateException> { requireState(false) { TEST_ERROR_MESSAGE } }
        assertEquals(TEST_ERROR_MESSAGE, exception.message)
        exception = assertThrows<StateException> { requireState(false) }
        assertTrue(exception.message == null)
    }

    @Test
    fun `requireState - exception is not produced if precondition is not met`() {
        requireState(true) { TEST_ERROR_MESSAGE }
    }
}
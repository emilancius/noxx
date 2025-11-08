package com.nerosec.noxx.commons.preconditions

import com.nerosec.noxx.commons.exceptions.ArgumentException
import com.nerosec.noxx.commons.exceptions.StateException

object Preconditions {

    fun require(condition: Boolean, exception: Exception) {
        if (!condition) throw exception
    }

    fun requireArgument(condition: Boolean, message: (() -> String?)? = null) = require(condition, ArgumentException(message?.invoke()))

    fun requireState(condition: Boolean, message: (() -> String?)? = null) = require(condition, StateException(message?.invoke()))
}

package com.demo.web.parsers

import io.undertow.util.BadRequestException

/**
 * @author Stefan Liu
 */
abstract class NumberParameterParser<T : Number> : ParameterParser<T> {
    override fun parse(value: String, defaultValue: String, required: Boolean): T? {
        val from = if (value.isBlank()) {
            defaultValue
        } else {
            value
        }
        try {
            val to = fromString(from)
            if (to == null && required) {
                throw BadRequestException("invalid number format")
            }
            return to
        } catch (e: Exception) {
            throw BadRequestException("invalid number format")
        }
    }

    abstract fun fromString(value: String): T?
}

object DoubleParameterParser : NumberParameterParser<Double>() {
    override fun fromString(value: String): Double? {
        return value.toDoubleOrNull()
    }
}

object IntParameterParser : NumberParameterParser<Int>() {
    override fun fromString(value: String): Int? {
        return value.toIntOrNull()
    }
}

object LongParameterParser : NumberParameterParser<Long>() {
    override fun fromString(value: String): Long? {
        return value.toLongOrNull()
    }
}

object FloatParameterParser : NumberParameterParser<Float>() {
    override fun fromString(value: String): Float? {
        return value.toFloatOrNull()
    }
}

package com.demo.web.parsers

import io.undertow.util.BadRequestException

/**
 * @author Stefan Liu
 */
object BooleanParameterParser : ParameterParser<Boolean> {
    override fun parse(value: String?, defaultValue: String, required: Boolean): Boolean? {
        val booleanValue = if (value.isNullOrBlank()) defaultValue else value
        if (booleanValue.equals("true", true)) {
            return true
        }
        if (booleanValue.equals("false", true)) {
            return false
        }
        val intValue = booleanValue.toIntOrNull()
        return if (intValue == null && required) {
            throw BadRequestException("invalid boolean format")
        } else if (intValue != null) {
            intValue > 0
        } else {
            null
        }
    }
}
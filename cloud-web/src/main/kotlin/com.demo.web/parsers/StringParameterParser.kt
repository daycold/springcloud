package com.demo.web.parsers

import io.undertow.util.BadRequestException

/**
 * @author Stefan Liu
 */
object StringParameterParser : ParameterParser<String> {
    override fun parse(value: String?, defaultValue: String, required: Boolean): String? {
        if (value.isNullOrEmpty()) {
            if (defaultValue.isEmpty()) {
                if (required) {
                    throw BadRequestException()
                }
                return null
            }
            return defaultValue
        } else {
            return value
        }
    }
}
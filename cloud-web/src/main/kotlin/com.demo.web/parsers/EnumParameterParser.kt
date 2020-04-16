package com.demo.web.parsers

import io.undertow.util.BadRequestException

/**
 * @author Stefan Liu
 */
class EnumParameterParser<K : Enum<*>>(clazz: Class<K>) : ParameterParser<K> {
    private val enums: Array<K> = clazz.enumConstants

    override fun parse(value: String, defaultValue: String, required: Boolean): K? {
        val v = if (value.isBlank()) defaultValue else value
        val en = enums.firstOrNull { enum ->
            enum.toString().equals(v, true)
        }
        if (en == null && required) {
            throw BadRequestException("invalid enum parameter")
        }
        return en
    }
}
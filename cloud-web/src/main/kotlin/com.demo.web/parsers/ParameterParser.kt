package com.demo.web.parsers

/**
 * @author Stefan Liu
 */
interface ParameterParser<T> {
    fun parse(value: String, defaultValue: String, required: Boolean): T?
}
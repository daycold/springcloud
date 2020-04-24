package com.demo.web.parsers

import java.lang.reflect.Type

/**
 * @author Stefan Liu
 */
interface ParserFactory {
    fun getParser(type: Type): ParameterParser<*>
}
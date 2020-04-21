package com.demo.web.parsers

/**
 * @author Stefan Liu
 */
interface ParserFactory {
    fun<T> getParser(clazz: Class<T>): ParameterParser<T>
}
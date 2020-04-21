package com.demo.web.parsers

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author Stefan Liu
 */
object DefaultParserFactory : ParserFactory {
    override fun getParser(type: Type): ParameterParser<*> {
        return when (type) {
            Long::class.java -> LongParameterParser
            Int::class.java -> IntParameterParser
            String::class.java -> StringParameterParser
            Float::class.java -> FloatParameterParser
            Double::class.java -> DoubleParameterParser
            Boolean::class.java -> BooleanParameterParser
            else -> {
                if (type is Class<*> && type.superclass == Enum::class.java) {
                    return EnumParameterParser(type as Class<Enum<*>>)
                }
                if (type is ParameterizedType && type.rawType == List::class.java) {
                    return when {
                        arrayOf(Int::class.java).contentEquals(type.actualTypeArguments) -> IntCollectionParameterParser
                        arrayOf(Long::class.java).contentEquals(type.actualTypeArguments) -> LongCollectionParameterParser
                        arrayOf(Float::class.java).contentEquals(type.actualTypeArguments) -> FloatCollectionParameterParser
                        arrayOf(Double::class.java).contentEquals(type.actualTypeArguments) -> DoubleCollectionParameterParser
                        arrayOf(Boolean::class.java).contentEquals(type.actualTypeArguments) -> BooleanCollectionParameterParser
                        arrayOf(String::class.java).contentEquals(type.actualTypeArguments) -> StringCollectionParameteParser
                        (type.actualTypeArguments.firstOrNull() as? Class<*>)
                            ?.superclass == Enum::class.java -> EnumCollectionParameterParser(type as Class<Enum<*>>)
                        else -> throw UnsupportedOperationException()
                    }
                }
                throw UnsupportedOperationException()
            }
        }
    }
}
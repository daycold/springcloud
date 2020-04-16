package com.demo.web.parsers

/**
 * @author Stefan Liu
 */
abstract class CollectionParameterParser<K : Any> : ParameterParser<Collection<K>> {
    override fun parse(value: String, defaultValue: String, required: Boolean): Collection<K>? {
        val actualValue = if (value.isBlank()) defaultValue else value
        if (actualValue.isBlank()) {
            return emptyList()
        }
        val values = actualValue.split(",")
        return values.mapNotNull { parseSingle(it) }
    }

    abstract fun parseSingle(value: String): K?
}

object IntCollectionParameterParser : CollectionParameterParser<Int>() {
    override fun parseSingle(value: String): Int? {
        return value.toIntOrNull()
    }
}

object LongCollectionParameterParser : CollectionParameterParser<Long>() {
    override fun parseSingle(value: String): Long? {
        return value.toLongOrNull()
    }
}

object DoubleCollectionParameterParser : CollectionParameterParser<Double>() {
    override fun parseSingle(value: String): Double? {
        return value.toDoubleOrNull()
    }
}

object FloatCollectionParameterParser : CollectionParameterParser<Float>() {
    override fun parseSingle(value: String): Float? {
        return value.toFloatOrNull()
    }
}

object BooleanCollectionParameterParser : CollectionParameterParser<Boolean>() {
    override fun parseSingle(value: String): Boolean? {
        return BooleanParameterParser.parse(value, value, false)
    }
}

class EnumCollectionParameterParser<T : Enum<*>>(clazz: Class<T>) : CollectionParameterParser<T>() {
    private val enumParser = EnumParameterParser(clazz)

    override fun parseSingle(value: String): T? {
        return enumParser.parse(value, value, false)
    }

}
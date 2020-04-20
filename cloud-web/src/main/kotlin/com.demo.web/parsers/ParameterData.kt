package com.demo.web.parsers

/**
 * @author Stefan Liu
 */
data class ParameterData<T>(
    val name: String,
    val type: Class<T>,
    val parser: ParameterParser<T>,
    val order: Int,
    val required: Boolean = true,
    val defaultValue: String = ""
) {
    companion object {
        fun <T> getParser(type: Class<T>): ParameterParser<T> {
            val parser = when (type) {
                Int::class.java -> IntParameterParser
                Long::class.java -> LongParameterParser
                Double::class.java -> DoubleParameterParser
                Float::class.java -> FloatParameterParser
                Boolean::class.java -> BooleanParameterParser
                Enum::class.java -> EnumParameterParser(type)
                else -> throw UnsupportedOperationException("unsupported parameter type")
            }
            return parser as ParameterParser<T>
        }

//        fun <T : Any> getCollectionParser(type: Class<T>): CollectionParameterParser<T> {
//            val parser = when (type) {
//                Int::class.java ->
//            }
//        }
    }
}
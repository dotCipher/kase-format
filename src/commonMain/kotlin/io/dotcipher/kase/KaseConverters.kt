package io.dotcipher.kase

sealed class KaseConverter {
    abstract fun convert(input: String): String
}

open class OneDirectionalConverter(private val sourceFormat: KaseFormat,
                                   private val destinationFormat: KaseFormat
): KaseConverter() {
    override fun convert(input: String): String = sourceFormat.convert(destinationFormat, input)
}

class BiDirectionalConverter(private val sourceFormat: KaseFormat,
                             private val destinationFormat: KaseFormat
): OneDirectionalConverter(sourceFormat, destinationFormat) {
    fun convertInverse(input: String): String = destinationFormat.convert(sourceFormat, input)
}

class DynamicConverter(private val destinationFormat: KaseFormat): KaseConverter() {
    override fun convert(input: String): String {
        // Determine input format, but assume best guess format based on a few standard choices
        val inputFormat = KaseFormat.determine(input)
        if (inputFormat != null) {
            return inputFormat.convert(destinationFormat, input)
        }
        // Give up
        throw IllegalArgumentException("Unable to determine input format of: $input")
    }
}
